package fr.tathan.graveyards.common.registries;

import fr.tathan.graveyards.Graveyards;
import fr.tathan.graveyards.common.gravestone_action.GravestoneAction;
import fr.tathan.graveyards.common.gravestone_action.SetNightAction;
import fr.tathan.graveyards.common.gravestone_action.StrikeGravestone;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class GravestoneActionRegistry {

    public static final DeferredRegister<GravestoneAction> GRAVESTONE_ACTION = DeferredRegister.create(GraveyardsRegistry.GRAVESTONE_ACTION, Graveyards.MODID);

    public static final Supplier<GravestoneAction> SET_NIGHT = GRAVESTONE_ACTION.register("set_night", SetNightAction::new);
    public static final Supplier<GravestoneAction> STRIKE_GRAVESTONE = GRAVESTONE_ACTION.register("strike_gravestone", StrikeGravestone::new);



}
