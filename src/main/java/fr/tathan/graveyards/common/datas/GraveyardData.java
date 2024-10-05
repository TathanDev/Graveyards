package fr.tathan.graveyards.common.datas;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.tathan.graveyards.common.network.packets.SyncDatapackPacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.List;

/**
 * All the infos about a battle
 */
public record GraveyardData(ResourceLocation id, int level, List<ResourceKey<EntityType<?>>> monsters, Rewards rewards) {

    public static final Codec<GraveyardData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("id").forGetter(GraveyardData::id),
            Codec.INT.fieldOf("level").forGetter(GraveyardData::level),
            ResourceKey.codec(Registries.ENTITY_TYPE).listOf().fieldOf("monsters").forGetter(GraveyardData::monsters),
            Rewards.CODEC.fieldOf("rewards").forGetter(GraveyardData::rewards)
    ).apply(instance, GraveyardData::new));

    public static final StreamCodec<ByteBuf, GraveyardData> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC,
            GraveyardData::id,
            ByteBufCodecs.INT,
            GraveyardData::level,
            ResourceKey.streamCodec(Registries.ENTITY_TYPE).apply(ByteBufCodecs.list()),
            GraveyardData::monsters,
            Rewards.STREAM_CODEC,
            GraveyardData::rewards,
            GraveyardData::new
    );

    public record Rewards(int experience, ResourceKey<LootTable> tableResource) {

        public static final Codec<Rewards> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.INT.fieldOf("experience").forGetter(Rewards::experience),
                ResourceKey.codec(Registries.LOOT_TABLE).fieldOf("loot_table").forGetter(Rewards::tableResource)
        ).apply(instance, Rewards::new));

        public ResourceKey<LootTable> lootTable() {
            return ResourceKey.create(Registries.LOOT_TABLE, this.tableResource.location());
        }

        public static final StreamCodec<ByteBuf, Rewards> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.INT,
                Rewards::experience,
                ResourceKey.streamCodec(Registries.LOOT_TABLE),
                Rewards::tableResource,
                Rewards::new
        );


    }
}
