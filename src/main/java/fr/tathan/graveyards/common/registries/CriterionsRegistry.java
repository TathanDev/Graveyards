package fr.tathan.graveyards.common.registries;

import fr.tathan.graveyards.Graveyards;
import fr.tathan.graveyards.common.advancements.ActivateGravestoneTrigger;
import fr.tathan.graveyards.common.advancements.UpgradeGravestoneTrigger;
import fr.tathan.graveyards.common.advancements.UseAmuletTrigger;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class CriterionsRegistry {

    public static final DeferredRegister<CriterionTrigger<?>> TRIGGER_TYPES =
            DeferredRegister.create(Registries.TRIGGER_TYPE, Graveyards.MODID);

    public static final Supplier<ActivateGravestoneTrigger> ACTIVATE_GRAVESTONE =
            TRIGGER_TYPES.register("activate_gravestone", ActivateGravestoneTrigger::new);

    public static final Supplier<UpgradeGravestoneTrigger> UPGRADE_GRAVESTONE =
            TRIGGER_TYPES.register("upgrade_gravestone", UpgradeGravestoneTrigger::new);

    public static final Supplier<UseAmuletTrigger> USE_AMULET =
            TRIGGER_TYPES.register("use_amulet", UseAmuletTrigger::new);


}
