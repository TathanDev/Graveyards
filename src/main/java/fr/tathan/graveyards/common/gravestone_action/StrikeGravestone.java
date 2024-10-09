package fr.tathan.graveyards.common.gravestone_action;

import fr.tathan.graveyards.common.attributes.PlayerFightData;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class StrikeGravestone extends GravestoneAction {

    public StrikeGravestone() {
    }

    @Override
    public void run(Player player, PlayerFightData fightData) {
        if (player.level().isClientSide) return;
        BlockPos pos = fightData.graveyardPos();

        Level level = player.level();
        LightningBolt lightningBolt = new LightningBolt(EntityType.LIGHTNING_BOLT, level);
        lightningBolt.setPos(pos.getX(), pos.getY() + 3, pos.getZ());
        level.addFreshEntity(lightningBolt);
    }

}
