package fr.tathan.graveyards.common.datas;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.List;
import java.util.Optional;

/**
 * All the infos about a battle
 */
public record GravestoneData(ResourceLocation id, int level, Optional<String> modRequired, List<ResourceKey<EntityType<?>>> monsters, Rewards rewards) {

    public static final Codec<GravestoneData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("id").forGetter(GravestoneData::id),

            Codec.INT.fieldOf("level").forGetter(GravestoneData::level),
            Codec.STRING.optionalFieldOf("mod_required").forGetter(GravestoneData::modRequired),
            ResourceKey.codec(Registries.ENTITY_TYPE).listOf().fieldOf("monsters").forGetter(GravestoneData::monsters),
            Rewards.CODEC.fieldOf("rewards").forGetter(GravestoneData::rewards)
    ).apply(instance, GravestoneData::new));

    public static final StreamCodec<ByteBuf, GravestoneData> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC,
            GravestoneData::id,
            ByteBufCodecs.INT,
            GravestoneData::level,
            ByteBufCodecs.optional(ByteBufCodecs.STRING_UTF8),
            GravestoneData::modRequired,
            ResourceKey.streamCodec(Registries.ENTITY_TYPE).apply(ByteBufCodecs.list()),
            GravestoneData::monsters,
            Rewards.STREAM_CODEC,
            GravestoneData::rewards,
            GravestoneData::new
    );


    public String getWinComponent() {
        return "gravestone." + this.id().getNamespace() + "." + this.id().getPath();
    }

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
