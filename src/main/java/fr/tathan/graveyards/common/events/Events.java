package fr.tathan.graveyards.common.events;

import fr.tathan.graveyards.Graveyards;
import fr.tathan.graveyards.common.datas.MonsterFightData;
import fr.tathan.graveyards.common.datas.PlayerFightData;
import fr.tathan.graveyards.common.registries.AttachmentTypesRegistry;
import fr.tathan.graveyards.common.utils.Utils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CraftingTableBlock;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = Graveyards.MODID)
public class Events {

    @SubscribeEvent
    public static void playerKillEntityEvent(LivingDeathEvent event) {
        DamageSource source = event.getSource();
        LivingEntity entity = event.getEntity();
        MonsterFightData data = entity.getData(AttachmentTypesRegistry.MONSTER_FIGHT_DATA);
        Player player = null;

        if (entity.level().isClientSide) return;

        if(source.getEntity() instanceof Player) {
            if(data.playerName().equals(((Player) source.getEntity()).getName().getString())) {
                player = (Player) source.getEntity();
            }
        } else {
            String playerName = data.playerName();
            player = entity.getServer().getPlayerList().getPlayerByName(playerName);
        }

        if(player != null) {
            if(Utils.decrementMonsters(player)) {
                player.sendSystemMessage(Component.literal("Congrats"));
                Utils.finishDuel(player);
            }
        }
    }


}
