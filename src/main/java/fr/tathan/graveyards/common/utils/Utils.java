package fr.tathan.graveyards.common.utils;

import fr.tathan.graveyards.Graveyards;
import fr.tathan.graveyards.common.blocks.GraveyardBlock;
import fr.tathan.graveyards.common.datas.GravestoneData;
import fr.tathan.graveyards.common.datas.GraveyardsDatas;
import fr.tathan.graveyards.common.attributes.PlayerFightData;
import fr.tathan.graveyards.common.gravestone_action.GravestoneAction;
import fr.tathan.graveyards.common.registries.AttachmentTypesRegistry;
import fr.tathan.graveyards.common.registries.BlockRegistry;
import fr.tathan.graveyards.common.registries.GraveyardsRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.ModList;

import java.util.*;
import java.util.stream.Collectors;

public class Utils {

    public static boolean isPlayerFighting(Player player) {
        return player.getData(AttachmentTypesRegistry.PLAYER_FIGHT_DATA).isFighting();
    }

    public static void stopPlayerFighting(Player player) {
        player.setData(AttachmentTypesRegistry.PLAYER_FIGHT_DATA, new PlayerFightData(false, 0, 0, BlockPos.ZERO, ResourceLocation.fromNamespaceAndPath(Graveyards.MODID, "default")));
    }


    public static void startGraveyard(Player player, BlockPos pos, int level) {
        GravestoneData gravestoneData = randomGraveyard(level);

        for (GravestoneData.Monster monster : gravestoneData.monsters()) {
            EntityType<?> entityType =  BuiltInRegistries.ENTITY_TYPE.get(monster.type());

            MonsterSummoner<?> monsterSummoner = new MonsterSummoner<>(entityType, monster, player, pos);
            monsterSummoner.summon();
        }

        player.setData(AttachmentTypesRegistry.PLAYER_FIGHT_DATA, new PlayerFightData(true, level, gravestoneData.monsters().size(), pos, gravestoneData.id()));
        if (gravestoneData.actions().isPresent()) {
            runActions(gravestoneData.actions().get(), player);
        }
    }

    public static void runActions(List<ResourceLocation> actions, Player player) {
        actions.forEach((actionKey) -> {
            GravestoneAction action = GraveyardsRegistry.GRAVESTONE_ACTION.get(actionKey);
            if (action != null) {
                action.run(player, player.getData(AttachmentTypesRegistry.PLAYER_FIGHT_DATA));
            }
        });
    }

    public static boolean decrementMonsters(Player player) {
        PlayerFightData data = player.getData(AttachmentTypesRegistry.PLAYER_FIGHT_DATA);
        int monstersLeft = data.monsters() - 1;
        player.setData(AttachmentTypesRegistry.PLAYER_FIGHT_DATA, new PlayerFightData(true, data.fightLevel(), monstersLeft, data.graveyardPos(), data.graveyardId()));
        return monstersLeft == 0;
    }

    public static void finishDuel(Player player) {

        if(!isPlayerFighting(player)) return;

        PlayerFightData data = player.getData(AttachmentTypesRegistry.PLAYER_FIGHT_DATA);
        GravestoneData gravestoneData = GraveyardsDatas.GRAVEYARDS.get(data.graveyardId());
        BlockPos pos = data.graveyardPos();


        if (gravestoneData == null || player.level().isClientSide) {
            return;
        }

        // Remove the graveyard block
        if(player.level().getBlockState(data.graveyardPos()).getBlock() instanceof GraveyardBlock) {
            player.level().setBlock(data.graveyardPos(), Blocks.AIR.defaultBlockState(), 3);
        }

        //Give Experience to the player
        player.giveExperienceLevels(gravestoneData.rewards().experience());

        List<ItemStack> stacks = getRewardItems(data.graveyardPos(), (ServerLevel) player.level(), gravestoneData);
        Random random = new Random();
        for (ItemStack stack : stacks) {
            player.level().addFreshEntity(new ItemEntity(player.level(), pos.getX() + random.nextInt(3), pos.getY() + random.nextInt(3), pos.getZ() + random.nextInt(3), stack));
        }

        player.setData(AttachmentTypesRegistry.PLAYER_FIGHT_DATA, AttachmentTypesRegistry.DEFAULT_PLAYER_FIGHT_DATA);

        player.sendSystemMessage(Component.translatable(gravestoneData.getWinComponent(), player.getName().getString(), gravestoneData.level()));

    }

    public static GravestoneData randomGraveyard(int level) {

        List<GravestoneData> matchingGravestoneData = GraveyardsDatas.GRAVEYARDS.values().stream()
                .filter(data -> data.level() == level)
                .collect(Collectors.toList());

        GravestoneData gravestoneData = matchingGravestoneData.get(new Random().nextInt(matchingGravestoneData.size()));

        gravestoneData.modRequired().ifPresent(mod -> {
            if (!ModList.get().isLoaded(mod)) {
                randomGraveyard(level);
            }
        });

        return gravestoneData;
    }



    public static List<ItemStack> getRewardItems(BlockPos pos, ServerLevel level, GravestoneData data) {
        BlockState state = level.getBlockState(pos);
        LootParams.Builder params = (new LootParams.Builder(level)).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos)).withParameter(LootContextParams.TOOL, ItemStack.EMPTY);

        ResourceKey<LootTable> resourcekey = data.rewards().lootTable();
        if (resourcekey == BuiltInLootTables.EMPTY) {
            return Collections.emptyList();
        } else {
            LootParams lootparams = params.withParameter(LootContextParams.BLOCK_STATE, state).create(LootContextParamSets.BLOCK);
            LootTable loottable = level.getServer().reloadableRegistries().getLootTable(resourcekey);
            return loottable.getRandomItems(lootparams);
        }
    }

}
