package fr.tathan.graveyards.datagen.provider;

import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import fr.tathan.graveyards.Graveyards;
import fr.tathan.graveyards.common.datas.GravestoneData;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class BattleDataProvider implements DataProvider {

    private final PackOutput.PathProvider pathProvider;
    private final Codec<GravestoneData> codec;
    private final ResourceKey<Registry<GravestoneData>> registry = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(Graveyards.MODID, "gravestones"));


    public BattleDataProvider(PackOutput packOutput) {
        this(packOutput, PackOutput.Target.DATA_PACK);
    }

    public BattleDataProvider(PackOutput packOutput,  PackOutput.Target target) {
        this.pathProvider = packOutput.createPathProvider(target, registry.location().getPath());
        this.codec = GravestoneData.CODEC;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        List<CompletableFuture<?>> futures = new ArrayList<>();
        build((key, value) ->
                futures.add(DataProvider.saveStable(
                        output,
                        codec.encodeStart(JsonOps.INSTANCE, value).getOrThrow(),
                        pathProvider.json(key)
                ))
        );

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

    @Override
    public String getName() {
        return "Gravestone Provider";
    }

    protected void build(BiConsumer<ResourceLocation, GravestoneData> consumer) {

        /** Level 1 **/
        registerGravestone(consumer, "graveyards:arachnophobe", 1, Optional.empty(), List.of(
                new GravestoneData.Monster(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.withDefaultNamespace("spider")), Optional.empty(), Optional.empty()),
                new GravestoneData.Monster(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.withDefaultNamespace("spider")), Optional.empty(), Optional.empty()),
                new GravestoneData.Monster(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.withDefaultNamespace("spider")), Optional.empty(), Optional.empty()),
                new GravestoneData.Monster(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.withDefaultNamespace("spider")), Optional.empty(), Optional.empty())
                ), new GravestoneData.Rewards(3, ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.withDefaultNamespace("chests/ancient_city"))));

        registerGravestone(consumer, "graveyards:rising_of_the_dead", 1, Optional.empty(), List.of(
                new GravestoneData.Monster(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.withDefaultNamespace("zombie")), Optional.of(
                        List.of(new GravestoneData.Monster.CustomAttribute(Attributes.MOVEMENT_SPEED.getKey(), 0.3),
                                new GravestoneData.Monster.CustomAttribute(Attributes.SCALE.getKey(), 1.6))),
                        Optional.empty()),
                new GravestoneData.Monster(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.withDefaultNamespace("zombie")), Optional.empty(), Optional.empty()),
                new GravestoneData.Monster(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.withDefaultNamespace("zombie")), Optional.empty(), Optional.empty()),
                new GravestoneData.Monster(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.withDefaultNamespace("zombie")), Optional.empty(), Optional.empty())
        ), new GravestoneData.Rewards(3, ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.withDefaultNamespace("chests/simple_dungeon"))));

        /** Level 2 **/
        registerGravestone(consumer, "graveyards:dead_village", 2, Optional.empty(), List.of(
                new GravestoneData.Monster(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.withDefaultNamespace("zombie_villager")), Optional.empty(), Optional.empty()),
                new GravestoneData.Monster(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.withDefaultNamespace("zombie_villager")), Optional.empty(), Optional.empty()),
                new GravestoneData.Monster(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.withDefaultNamespace("zombie_villager")), Optional.empty(),
                        Optional.of(List.of(new GravestoneData.Monster.MonsterItem( new ItemStack(Items.IRON_SWORD), EquipmentSlot.MAINHAND)))),
                new GravestoneData.Monster(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.withDefaultNamespace("zombie_villager")), Optional.empty(), Optional.empty()),
                new GravestoneData.Monster(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.withDefaultNamespace("zombie_villager")), Optional.empty(),
                        Optional.of(List.of(new GravestoneData.Monster.MonsterItem( new ItemStack(Items.IRON_AXE), EquipmentSlot.MAINHAND))))
        ), new GravestoneData.Rewards(5, ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.withDefaultNamespace("chests/simple_dungeon"))));


        /** Level 3 **/
        registerGravestone(consumer, "graveyards:possessed_village", 3, Optional.empty(), List.of(
                new GravestoneData.Monster(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.withDefaultNamespace("vindicator")), Optional.empty(), Optional.empty()),
                new GravestoneData.Monster(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.withDefaultNamespace("vex")), Optional.empty(), Optional.empty()),
                new GravestoneData.Monster(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.withDefaultNamespace("vex")), Optional.empty(), Optional.empty()),
                new GravestoneData.Monster(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.withDefaultNamespace("pillager")), Optional.empty(),
                        Optional.of(List.of(new GravestoneData.Monster.MonsterItem( new ItemStack(Items.CROSSBOW), EquipmentSlot.MAINHAND))))
        ), new GravestoneData.Rewards(10, ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.withDefaultNamespace("chests/simple_dungeon"))));

    };

    public static void registerGravestone(BiConsumer<ResourceLocation, GravestoneData> consumer, String id, int level, Optional<String> mod, List<GravestoneData.Monster> monsters, GravestoneData.Rewards rewards) {
        consumer.accept(ResourceLocation.parse(id), new GravestoneData(ResourceLocation.parse(id), level, mod, monsters, rewards));
    }


}
