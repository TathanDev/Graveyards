package fr.tathan.graveyards.common.registries;

import fr.tathan.graveyards.Graveyards;
import fr.tathan.graveyards.common.gravestone_action.GravestoneAction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

@EventBusSubscriber(modid = Graveyards.MODID, bus = EventBusSubscriber.Bus.MOD)
public class GraveyardsRegistry {


    public static final ResourceKey<Registry<GravestoneAction>> GRAVESTONE_ACTION_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(Graveyards.MODID, "spells"));
    public static final Registry<GravestoneAction> GRAVESTONE_ACTION = new RegistryBuilder<>(GRAVESTONE_ACTION_KEY)
            .sync(true)
            .defaultKey(ResourceLocation.fromNamespaceAndPath(Graveyards.MODID, "set_night"))
            .maxId(256)
            .create();

    @SubscribeEvent
    static void registerRegistries(NewRegistryEvent event) {
        event.register(GRAVESTONE_ACTION);
    }
}
