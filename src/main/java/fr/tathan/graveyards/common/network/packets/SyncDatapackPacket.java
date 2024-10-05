package fr.tathan.graveyards.common.network.packets;

import fr.tathan.graveyards.Graveyards;
import fr.tathan.graveyards.common.datas.GravestoneData;
import fr.tathan.graveyards.common.datas.GraveyardsDatas;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SyncDatapackPacket(ResourceLocation id, GravestoneData data) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<SyncDatapackPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Graveyards.MODID, "sync_datapack"));

    public static final StreamCodec<ByteBuf, SyncDatapackPacket> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC,
            SyncDatapackPacket::id,
            GravestoneData.STREAM_CODEC,
            SyncDatapackPacket::data,
            SyncDatapackPacket::new
    );

    public static void handleClient(SyncDatapackPacket packet, IPayloadContext context) {
        GraveyardsDatas.GRAVEYARDS.putIfAbsent(packet.id(), packet.data());
    }

    public static void handleServer(SyncDatapackPacket packet, IPayloadContext context) {
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
