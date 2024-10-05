package fr.tathan.graveyards.common.utils;

import fr.tathan.graveyards.common.datas.GraveyardData;
import fr.tathan.graveyards.common.datas.GraveyardsDatas;
import fr.tathan.graveyards.common.datas.PlayerFightData;
import fr.tathan.graveyards.common.registries.AttachmentTypesRegistry;
import fr.tathan.graveyards.common.registries.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
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

import java.util.*;
import java.util.stream.Collectors;

public class Utils {

    public static boolean isPlayerFighting(Player player) {
        return player.getData(AttachmentTypesRegistry.PLAYER_FIGHT_DATA).isFighting();
    }

    public static void startGraveyard(Player player, BlockPos pos, int level) {
        GraveyardData graveyardData = randomGraveyard(level);

        for (ResourceKey<EntityType<?>> monster : graveyardData.monsters()) {
            EntityType<?> entityType =  BuiltInRegistries.ENTITY_TYPE.get(monster.location());

            MonsterSummoner<?> monsterSummoner = new MonsterSummoner<>(entityType, player.level(), pos, player.getName().getString());
            monsterSummoner.summon();
        }


        player.setData(AttachmentTypesRegistry.PLAYER_FIGHT_DATA, new PlayerFightData(true, level, graveyardData.monsters().size(), pos, graveyardData.id()));

    }

    public static boolean decrementMonsters(Player player) {
        PlayerFightData data = player.getData(AttachmentTypesRegistry.PLAYER_FIGHT_DATA);
        int monstersLeft = data.monsters() - 1;
        player.setData(AttachmentTypesRegistry.PLAYER_FIGHT_DATA, new PlayerFightData(true, data.fightLevel(), monstersLeft, data.graveyardPos(), data.graveyardId()));
        return monstersLeft == 0;
    }

    public static void finishDuel(Player player) {
        PlayerFightData data = player.getData(AttachmentTypesRegistry.PLAYER_FIGHT_DATA);
        GraveyardData graveyardData = GraveyardsDatas.GRAVEYARDS.get(data.graveyardId());
        BlockPos pos = data.graveyardPos();


        if (graveyardData == null || player.level().isClientSide) {
            return;
        }

        // Remove the graveyard block
        if(player.level().getBlockState(data.graveyardPos()).is(BlockRegistry.GRAVEYARD)) {
            player.level().setBlock(data.graveyardPos(), Blocks.AIR.defaultBlockState(), 3);
        }

        //Give Experience to the player
        player.giveExperienceLevels(graveyardData.rewards().experience());

        List<ItemStack> stacks = getRewardItems(data.graveyardPos(), (ServerLevel) player.level(), graveyardData);
        Random random = new Random();
        for (ItemStack stack : stacks) {
            player.level().addFreshEntity(new ItemEntity(player.level(), pos.getX() + random.nextInt(3), pos.getY() + random.nextInt(3), pos.getZ() + random.nextInt(3), stack));
        }

        player.setData(AttachmentTypesRegistry.PLAYER_FIGHT_DATA, AttachmentTypesRegistry.DEFAULT_PLAYER_FIGHT_DATA);
    }

    public static GraveyardData randomGraveyard(int level) {

        List<GraveyardData> matchingGraveyardData = GraveyardsDatas.GRAVEYARDS.values().stream()
                .filter(data -> data.level() == level)
                .collect(Collectors.toList());

        return matchingGraveyardData.get(new Random().nextInt(matchingGraveyardData.size()));
    }



    public static List<ItemStack> getRewardItems(BlockPos pos, ServerLevel level, GraveyardData data) {
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
