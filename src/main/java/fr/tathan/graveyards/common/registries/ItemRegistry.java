package fr.tathan.graveyards.common.registries;

import fr.tathan.graveyards.Graveyards;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ItemRegistry {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(
            Graveyards.MODID
    );


    public static final DeferredItem<BlockItem> GRAVEYARD = ITEMS.registerItem("gravestone",
           properties ->  new BlockItem(BlockRegistry.GRAVEYARD.get(), properties), new Item.Properties());


}
