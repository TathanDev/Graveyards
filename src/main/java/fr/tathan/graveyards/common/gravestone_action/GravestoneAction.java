package fr.tathan.graveyards.common.gravestone_action;

import fr.tathan.graveyards.common.attributes.PlayerFightData;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public abstract class GravestoneAction {

    public GravestoneAction() {
    }

    public abstract void run(Player player,@Nullable PlayerFightData fightData);

}
