package fr.tathan.graveyards;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.ToNumberPolicy;
import com.mojang.logging.LogUtils;
import fr.tathan.graveyards.common.config.CommonConfig;
import fr.tathan.graveyards.common.datas.GraveyardsDatas;
import fr.tathan.graveyards.common.network.packets.SyncDatapackPacket;
import fr.tathan.graveyards.common.registries.*;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.slf4j.Logger;

@Mod(Graveyards.MODID)
public class Graveyards {

    public static final String MODID = "graveyards";

    public static final Logger LOGGER = LogUtils.getLogger();

    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
            .create();


    public Graveyards(IEventBus modEventBus, ModContainer modContainer)
    {
        modEventBus.addListener(this::commonSetup);
        NeoForge.EVENT_BUS.addListener(Graveyards::onAddReloadListenerEvent);
        NeoForge.EVENT_BUS.addListener(Graveyards::onDatapackSync);

        modEventBus.addListener(Graveyards::buildContents);

        BlockRegistry.BLOCKS.register(modEventBus);
        ItemRegistry.ITEMS.register(modEventBus);
        AttachmentTypesRegistry.ATTACHMENT_TYPES.register(modEventBus);
        CriterionsRegistry.TRIGGER_TYPES.register(modEventBus);
        GravestoneActionRegistry.GRAVESTONE_ACTION.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, CommonConfig.BUILDER.build());
        if (FMLEnvironment.dist.isClient())
        {
            modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        }
    }


    private void commonSetup(final FMLCommonSetupEvent event)
    {
        LOGGER.info("Summoning the dead !!");
    }

    public static void onDatapackSync(OnDatapackSyncEvent event) {
        if (event.getPlayer() != null) {
            GraveyardsDatas.GRAVEYARDS.forEach((id, data) -> {
                PacketDistributor.sendToPlayer(event.getPlayer(), new SyncDatapackPacket(id, data));
            });
        } else {
            GraveyardsDatas.GRAVEYARDS.forEach((id, data) -> {
                PacketDistributor.sendToAllPlayers(new SyncDatapackPacket(id, data));
            });
        }
    }

    public static void onAddReloadListenerEvent(AddReloadListenerEvent event) {
        event.addListener(new GraveyardsDatas());
    }

    public static void buildContents(BuildCreativeModeTabContentsEvent event) {
        // Is this the tab we want to add to?
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(ItemRegistry.AMULET_OF_FORGIVENESS.get());
            event.accept(ItemRegistry.AMULET_OF_VISION.get());
        } else if (event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
            event.accept(ItemRegistry.GRAVEYARD.get());
        }
    }



}
