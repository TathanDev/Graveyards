package fr.tathan.graveyards.datagen.provider;

import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import fr.tathan.graveyards.Graveyards;
import fr.tathan.graveyards.common.datas.GraveyardData;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class BattleDataProvider implements DataProvider {

    private final PackOutput.PathProvider pathProvider;
    private final Codec<GraveyardData> codec;
    private final ResourceKey<Registry<GraveyardData>> registry = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(Graveyards.MODID, "graveyards"));


    public BattleDataProvider(PackOutput packOutput) {
        this(packOutput, PackOutput.Target.DATA_PACK);
    }

    public BattleDataProvider(PackOutput packOutput,  PackOutput.Target target) {
        this.pathProvider = packOutput.createPathProvider(target, registry.location().getPath());
        this.codec = GraveyardData.CODEC;
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
        return "Batlle Data Provider";
    }

    protected void build(BiConsumer<ResourceLocation, GraveyardData> consumer) {
        consumer.accept(ResourceLocation.fromNamespaceAndPath(Graveyards.MODID, "battle_data"), new GraveyardData(
                ResourceLocation.fromNamespaceAndPath(Graveyards.MODID, "battle_test"),
                1,
                List.of(
                        ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.withDefaultNamespace("zombie")),
                        ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.withDefaultNamespace("zombie"))
                ),
                new GraveyardData.Rewards(10, ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.withDefaultNamespace("entities/zombie")))
        ));

        consumer.accept(ResourceLocation.fromNamespaceAndPath(Graveyards.MODID, "arachnophobe"), new GraveyardData(
                ResourceLocation.fromNamespaceAndPath(Graveyards.MODID, "arachnophobe"),
                1,
                List.of(
                        ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.withDefaultNamespace("spider")),
                        ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.withDefaultNamespace("zombie"))
                ),
                new GraveyardData.Rewards(10, ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.withDefaultNamespace("entities/spider")))
        ));

    };


}
