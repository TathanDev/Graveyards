package fr.tathan.graveyards.common.datas;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import fr.tathan.graveyards.Graveyards;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.HashMap;
import java.util.Map;

public class GraveyardsDatas extends SimpleJsonResourceReloadListener {

    public static final Map<ResourceLocation, GravestoneData> GRAVEYARDS = new HashMap<>();

    public GraveyardsDatas() {
        super(Graveyards.GSON, "gravestones");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resourceLocationJsonElementMap, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        GRAVEYARDS.clear();
        resourceLocationJsonElementMap.forEach((key, value) -> {
            JsonObject json = GsonHelper.convertToJsonObject(value, "gravestones");
            GravestoneData graveyard = GravestoneData.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow();
            Graveyards.LOGGER.info("Loaded gravestone: " + graveyard.id());
            GRAVEYARDS.put(graveyard.id(), graveyard);
        });
    }

}