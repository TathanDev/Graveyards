package fr.tathan.graveyards.common.registries;

import fr.tathan.graveyards.Graveyards;
import fr.tathan.graveyards.common.item.AmuletOfForgivness;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class TagsRegistry {


    public static final TagKey<Item> DONT_TRIGGER_GRAVESTONE = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Graveyards.MODID, "dont_trigger_gravestone"));

}
