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

public class GravestoneDataProvider implements DataProvider {

    private final PackOutput.PathProvider pathProvider;
    private final Codec<GravestoneData> codec;
    private final ResourceKey<Registry<GravestoneData>> registry = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(Graveyards.MODID, "gravestones"));


    public GravestoneDataProvider(PackOutput packOutput) {
        this(packOutput, PackOutput.Target.DATA_PACK);
    }

    public GravestoneDataProvider(PackOutput packOutput, PackOutput.Target target) {
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
        registerGravestone(consumer, "graveyards:arachnophobe", 1, Optional.empty(),Optional.empty(), List.of(
                new GravestoneData.Monster(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.withDefaultNamespace("spider")), Optional.empty(), Optional.empty()),
                new GravestoneData.Monster(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.withDefaultNamespace("spider")), Optional.empty(), Optional.empty()),
                new GravestoneData.Monster(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.withDefaultNamespace("spider")), Optional.empty(), Optional.empty()),
                new GravestoneData.Monster(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.withDefaultNamespace("spider")), Optional.empty(), Optional.empty())
                ), new GravestoneData.Rewards(3, ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(Graveyards.MODID, "gravestones/level_1"))));

        registerGravestone(consumer, "graveyards:rising_of_the_dead", 1, Optional.of(List.of(graveyardsLocation("set_night"))),Optional.empty(), List.of(
                new GravestoneData.Monster(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.withDefaultNamespace("zombie")), Optional.of(
                        List.of(new GravestoneData.Monster.CustomAttribute(Attributes.MOVEMENT_SPEED.getKey(), 0.3),
                                new GravestoneData.Monster.CustomAttribute(Attributes.SCALE.getKey(), 1.6),
                                new GravestoneData.Monster.CustomAttribute(Attributes.ATTACK_DAMAGE.getKey(), 4.5))),
                        Optional.empty()),
                new GravestoneData.Monster(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.withDefaultNamespace("zombie")), Optional.empty(), Optional.empty()),
                new GravestoneData.Monster(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.withDefaultNamespace("zombie")), Optional.empty(), Optional.empty()),
                new GravestoneData.Monster(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.withDefaultNamespace("zombie")), Optional.empty(), Optional.empty())
        ), new GravestoneData.Rewards(3, ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(Graveyards.MODID, "gravestones/level_1"))));

        /** Level 2 **/
        registerGravestone(consumer, "graveyards:the_mummy_curse", 2, Optional.of(List.of(graveyardsLocation("set_night"), graveyardsLocation("strike"))),Optional.empty(), List.of(
                new GravestoneData.Monster(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.withDefaultNamespace("husk")), Optional.empty(), Optional.empty()),
                new GravestoneData.Monster(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.withDefaultNamespace("husk")), Optional.empty(),
                        Optional.of(List.of(new GravestoneData.Monster.MonsterItem( new ItemStack(Items.DIAMOND_SWORD), EquipmentSlot.MAINHAND)))),
                new GravestoneData.Monster(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.withDefaultNamespace("husk")), Optional.of(
                        List.of(new GravestoneData.Monster.CustomAttribute(Attributes.MOVEMENT_SPEED.getKey(), 0.3),
                                new GravestoneData.Monster.CustomAttribute(Attributes.SCALE.getKey(), 1.6),
                                new GravestoneData.Monster.CustomAttribute(Attributes.ATTACK_DAMAGE.getKey(), 4.5))
                        ),
                        Optional.of(List.of(new GravestoneData.Monster.MonsterItem( new ItemStack(Items.IRON_SWORD), EquipmentSlot.MAINHAND)))),
                new GravestoneData.Monster(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.withDefaultNamespace("husk")), Optional.of(
                        List.of(new GravestoneData.Monster.CustomAttribute(Attributes.MOVEMENT_SPEED.getKey(), 0.5),
                                new GravestoneData.Monster.CustomAttribute(Attributes.SCALE.getKey(), 0.8),
                                new GravestoneData.Monster.CustomAttribute(Attributes.ATTACK_DAMAGE.getKey(), 2))
                ), Optional.empty()),
                new GravestoneData.Monster(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.withDefaultNamespace("husk")), Optional.of(
                        List.of(new GravestoneData.Monster.CustomAttribute(Attributes.MOVEMENT_SPEED.getKey(), 0.5),
                                new GravestoneData.Monster.CustomAttribute(Attributes.SCALE.getKey(), 0.8),
                                new GravestoneData.Monster.CustomAttribute(Attributes.ATTACK_DAMAGE.getKey(), 2))
                ), Optional.empty()),
                new GravestoneData.Monster(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.withDefaultNamespace("husk")), Optional.empty(),
                        Optional.of(List.of(new GravestoneData.Monster.MonsterItem( new ItemStack(Items.IRON_AXE), EquipmentSlot.MAINHAND))))
        ), new GravestoneData.Rewards(5, ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(Graveyards.MODID, "gravestones/level_2"))));

        registerGravestone(consumer, "graveyards:dead_village", 2, Optional.of(List.of(graveyardsLocation("set_night"))),Optional.empty(), List.of(
                new GravestoneData.Monster(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.withDefaultNamespace("zombie_villager")), Optional.empty(), Optional.empty()),
                new GravestoneData.Monster(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.withDefaultNamespace("zombie_villager")), Optional.empty(), Optional.empty()),
                new GravestoneData.Monster(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.withDefaultNamespace("zombie_villager")), Optional.empty(),
                        Optional.of(List.of(new GravestoneData.Monster.MonsterItem( new ItemStack(Items.IRON_SWORD), EquipmentSlot.MAINHAND)))),
                new GravestoneData.Monster(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.withDefaultNamespace("zombie_villager")), Optional.empty(), Optional.empty()),
                new GravestoneData.Monster(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.withDefaultNamespace("zombie_villager")), Optional.empty(),
                        Optional.of(List.of(new GravestoneData.Monster.MonsterItem( new ItemStack(Items.IRON_AXE), EquipmentSlot.MAINHAND))))
        ), new GravestoneData.Rewards(5, ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(Graveyards.MODID, "gravestones/level_2"))));


        /** Level 3 **/
        registerGravestone(consumer, "graveyards:possessed_village", 3, Optional.of(List.of(graveyardsLocation("strike_gravestone"))), Optional.empty(), List.of(
                new GravestoneData.Monster(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.withDefaultNamespace("vindicator")), Optional.empty(), Optional.empty()),
                new GravestoneData.Monster(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.withDefaultNamespace("vex")), Optional.empty(), Optional.empty()),
                new GravestoneData.Monster(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.withDefaultNamespace("vex")), Optional.empty(), Optional.empty()),
                new GravestoneData.Monster(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.withDefaultNamespace("pillager")), Optional.empty(),
                        Optional.of(List.of(new GravestoneData.Monster.MonsterItem( new ItemStack(Items.CROSSBOW), EquipmentSlot.MAINHAND))))
        ), new GravestoneData.Rewards(10, ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(Graveyards.MODID, "gravestones/level_3"))));

        registerGravestone(consumer, "graveyards:ancient_army", 3, Optional.of(List.of(graveyardsLocation("strike_gravestone"))), Optional.empty(), List.of(
                new GravestoneData.Monster(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.withDefaultNamespace("warden")),
                        Optional.of(List.of(
                                new GravestoneData.Monster.CustomAttribute(Attributes.SCALE.getKey(), 0.8),
                                new GravestoneData.Monster.CustomAttribute(Attributes.ATTACK_DAMAGE.getKey(), 4),
                                new GravestoneData.Monster.CustomAttribute(Attributes.MOVEMENT_SPEED.getKey(), 0.4))),
                        Optional.empty()),
                new GravestoneData.Monster(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.withDefaultNamespace("warden")),
                        Optional.of(List.of(
                                new GravestoneData.Monster.CustomAttribute(Attributes.SCALE.getKey(), 0.8),
                                new GravestoneData.Monster.CustomAttribute(Attributes.ATTACK_DAMAGE.getKey(), 4),
                                new GravestoneData.Monster.CustomAttribute(Attributes.MOVEMENT_SPEED.getKey(), 0.4))),
                        Optional.empty()),
                new GravestoneData.Monster(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.withDefaultNamespace("warden")),
                        Optional.of(List.of(
                                new GravestoneData.Monster.CustomAttribute(Attributes.SCALE.getKey(), 1.6),
                                new GravestoneData.Monster.CustomAttribute(Attributes.MOVEMENT_SPEED.getKey(), 0.2))),
                        Optional.empty())
        ), new GravestoneData.Rewards(10, ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(Graveyards.MODID, "gravestones/level_3"))));

    };

    public static void registerGravestone(BiConsumer<ResourceLocation, GravestoneData> consumer, String id, int level, Optional<List<ResourceLocation>> actions, Optional<String> mod, List<GravestoneData.Monster> monsters, GravestoneData.Rewards rewards) {
        consumer.accept(ResourceLocation.parse(id), new GravestoneData(ResourceLocation.parse(id), level, actions, mod, monsters, rewards));
    }

    public static ResourceLocation graveyardsLocation(String path) {
        return ResourceLocation.fromNamespaceAndPath(Graveyards.MODID, path);
    }

}
