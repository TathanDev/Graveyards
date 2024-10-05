package fr.tathan.graveyards.common.network;

import fr.tathan.graveyards.common.network.packets.SyncDatapackPacket;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = "graveyards", bus = EventBusSubscriber.Bus.MOD)
public class ModNetworking {

    @SubscribeEvent
    public static void register( RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playBidirectional(
                SyncDatapackPacket.TYPE,
                SyncDatapackPacket.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        SyncDatapackPacket::handleClient,
                        SyncDatapackPacket::handleServer
                )
        );
    }


}
