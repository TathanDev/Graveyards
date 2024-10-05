package fr.tathan.graveyards.common.config;

import fr.tathan.graveyards.Graveyards;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = Graveyards.MODID, bus = EventBusSubscriber.Bus.MOD)
public class CommonConfig {

    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.IntValue GRAVEYARD_WEIGHT = BUILDER
            .comment("The change for a graveyard to spawn in a village")
            .defineInRange("graveyardWeight", 50, 0, 1000);

    public static int graveYardWeight;


    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        graveYardWeight = GRAVEYARD_WEIGHT.get();
    }


}
