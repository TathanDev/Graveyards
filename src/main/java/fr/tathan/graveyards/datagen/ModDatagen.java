package fr.tathan.graveyards.datagen;

import fr.tathan.graveyards.Graveyards;
import fr.tathan.graveyards.datagen.provider.AdvancementsProvider;
import fr.tathan.graveyards.datagen.provider.GravestoneDataProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = Graveyards.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModDatagen {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        // other providers here
        generator.addProvider(
                event.includeServer(),
                new GravestoneDataProvider(output)
        );
        generator.addProvider(
                event.includeServer(),
                new AdvancementsProvider(output, event.getLookupProvider(), existingFileHelper)
        );

    }


}
