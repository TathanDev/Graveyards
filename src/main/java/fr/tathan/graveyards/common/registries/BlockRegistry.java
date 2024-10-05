package fr.tathan.graveyards.common.registries;

import fr.tathan.graveyards.Graveyards;
import fr.tathan.graveyards.common.blocks.GraveyardBlock;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BlockRegistry {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(
            BuiltInRegistries.BLOCK,
            Graveyards.MODID
    );

    public static final DeferredHolder<Block, GraveyardBlock> GRAVEYARD = BLOCKS.register("gravestone" ,
        () -> new GraveyardBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).strength(2.0F).sound(SoundType.WOOD).noOcclusion()));


}
