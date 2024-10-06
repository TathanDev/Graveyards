package fr.tathan.graveyards.common.datas;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.List;
import java.util.Optional;

/**
 * All the infos about a battle
 */
public record GravestoneData(ResourceLocation id, int level, Optional<List<ResourceLocation>> actions, Optional<String> modRequired, List<Monster> monsters, Rewards rewards) {

    public static final Codec<GravestoneData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("id").forGetter(GravestoneData::id),
            Codec.INT.fieldOf("level").forGetter(GravestoneData::level),
            ResourceLocation.CODEC.listOf().optionalFieldOf("actions").forGetter(GravestoneData::actions),
            Codec.STRING.optionalFieldOf("mod_required").forGetter(GravestoneData::modRequired),
            Monster.CODEC.listOf().fieldOf("monsters").forGetter(GravestoneData::monsters),
            Rewards.CODEC.fieldOf("rewards").forGetter(GravestoneData::rewards)
    ).apply(instance, GravestoneData::new));

    public static final StreamCodec<ByteBuf, GravestoneData> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC,
            GravestoneData::id,
            ByteBufCodecs.INT,
            GravestoneData::level,
            ByteBufCodecs.optional(ResourceLocation.STREAM_CODEC.apply(ByteBufCodecs.list())),
            GravestoneData::actions,
            ByteBufCodecs.optional(ByteBufCodecs.STRING_UTF8),
            GravestoneData::modRequired,
            Monster.STREAM_CODEC.apply(ByteBufCodecs.list()),
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

    public record Monster(ResourceKey<EntityType<?>> type, Optional<List<CustomAttribute>> attributes, Optional<List<MonsterItem>> items) {

        public static final Codec<Monster> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ResourceKey.codec(Registries.ENTITY_TYPE).fieldOf("type").forGetter(Monster::type),
                CustomAttribute.CODEC.listOf().optionalFieldOf("attributes").forGetter(Monster::attributes),
                MonsterItem.CODEC.listOf().optionalFieldOf("items").forGetter(Monster::items)
        ).apply(instance, Monster::new));

        public static final StreamCodec<ByteBuf, Monster> STREAM_CODEC = StreamCodec.composite(
                ResourceKey.streamCodec(Registries.ENTITY_TYPE),
                Monster::type,
                ByteBufCodecs.optional(CustomAttribute.STREAM_CODEC.apply(ByteBufCodecs.list())),
                Monster::attributes,
                ByteBufCodecs.optional(MonsterItem.STREAM_CODEC.apply(ByteBufCodecs.list())),
                Monster::items,
                Monster::new
        );


        public record CustomAttribute(ResourceKey<Attribute> attribute, double baseValue) {

            public static final Codec<CustomAttribute> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    ResourceKey.codec(Registries.ATTRIBUTE).fieldOf("attribute").forGetter(CustomAttribute::attribute),
                    Codec.DOUBLE.fieldOf("value").forGetter(CustomAttribute::baseValue)
            ).apply(instance, CustomAttribute::new));

            public static final StreamCodec<ByteBuf, CustomAttribute> STREAM_CODEC = StreamCodec.composite(
                    ResourceKey.streamCodec(Registries.ATTRIBUTE),
                    CustomAttribute::attribute,
                    ByteBufCodecs.DOUBLE,
                    CustomAttribute::baseValue,
                    CustomAttribute::new
            );
        }

        public record MonsterItem(ItemStack stack, EquipmentSlot slot) {

            public static final Codec<MonsterItem> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    ItemStack.CODEC.fieldOf("item").forGetter(MonsterItem::stack),
                    EquipmentSlot.CODEC.fieldOf("slot").forGetter(MonsterItem::slot)
            ).apply(instance, MonsterItem::new));

            public static final StreamCodec<ByteBuf, MonsterItem> STREAM_CODEC = StreamCodec.composite(
                    ByteBufCodecs.fromCodec(ItemStack.CODEC),
                    MonsterItem::stack,
                    ByteBufCodecs.fromCodec(EquipmentSlot.CODEC),
                    MonsterItem::slot,
                    MonsterItem::new
            );
        }
    }


}
