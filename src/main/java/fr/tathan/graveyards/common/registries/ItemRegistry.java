package fr.tathan.graveyards.common.registries;

import fr.tathan.graveyards.Graveyards;
import fr.tathan.graveyards.common.item.AmuletOfForgivness;
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

    public static final DeferredItem<AmuletOfForgivness> AMULET_OF_FORGIVENESS = ITEMS.registerItem("amulet_of_forgiveness",
            AmuletOfForgivness::new, new Item.Properties());


}
