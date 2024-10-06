package fr.tathan.graveyards.common.gravestone_action;

import fr.tathan.graveyards.Graveyards;
import fr.tathan.graveyards.common.attributes.PlayerFightData;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.server.commands.TimeCommand;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;

import java.util.Random;

public class SetNightAction extends GravestoneAction {

    public SetNightAction() {
    }

    public void run(Player player, PlayerFightData fightData) {
        if (!player.level().isClientSide) {
            ServerLevel serverlevel = (ServerLevel) player.level();
            serverlevel.setDayTime(new Random().nextInt(13000, 19000));
        }

    }

}
