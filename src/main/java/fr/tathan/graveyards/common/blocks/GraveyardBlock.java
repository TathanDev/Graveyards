package fr.tathan.graveyards.common.blocks;

import com.mojang.serialization.MapCodec;
import fr.tathan.graveyards.Graveyards;
import fr.tathan.graveyards.common.advancements.ActivateGravestoneTrigger;
import fr.tathan.graveyards.common.advancements.UpgradeGravestoneTrigger;
import fr.tathan.graveyards.common.attributes.PlayerFightData;
import fr.tathan.graveyards.common.registries.AttachmentTypesRegistry;
import fr.tathan.graveyards.common.registries.BlockRegistry;
import fr.tathan.graveyards.common.registries.TagsRegistry;
import fr.tathan.graveyards.common.utils.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GraveyardBlock extends Block {

    private static final VoxelShape NORTH_SHAPE;
    private static final VoxelShape EAST_SHAPE;

    public static final IntegerProperty LEVEL;
    public static final DirectionProperty FACING;

    public GraveyardBlock(Properties properties, int level) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(LEVEL, level));
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));

    }


    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {

        //if (stack.is(Items.AIR)) this.startGraveyard(player, state, pos);

        if(stack.is(Items.DIAMOND) && state.getValue(LEVEL) == 2) {
            BlockState newState = BlockRegistry.DIAMOND_GRAVEYARD.get().defaultBlockState().setValue(LEVEL, 3).setValue(FACING, state.getValue(FACING));
            level.setBlockAndUpdate(pos, newState);
            stack.shrink(1);
            if(player instanceof ServerPlayer) UpgradeGravestoneTrigger.Instance.trigger((ServerPlayer) player, newState.getValue(LEVEL));
        } else if(stack.is(Items.GOLD_INGOT) && state.getValue(LEVEL) == 1) {
            BlockState newState = BlockRegistry.GOLD_GRAVEYARD.get().defaultBlockState().setValue(LEVEL, 2).setValue(FACING, state.getValue(FACING));
            level.setBlockAndUpdate(pos, newState);
            stack.shrink(1);
            if(player instanceof ServerPlayer) UpgradeGravestoneTrigger.Instance.trigger((ServerPlayer) player, newState.getValue(LEVEL));
        } else {
            return super.useItemOn(stack, state, level, pos, player, hand, hitResult);

        }
        return ItemInteractionResult.SUCCESS;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {

        this.startGraveyard(player, state, pos);

        return super.useWithoutItem(state, level, pos, player, hitResult);

    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {

        if(state.getValue(FACING) == Direction.WEST || state.getValue(FACING) == Direction.EAST) {
            return EAST_SHAPE;
        }
        return NORTH_SHAPE;
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if(entity instanceof Player player) {
            this.startGraveyard(player, state, pos);
        }

        super.stepOn(level, pos, state, entity);
    }

    private void startGraveyard(Player player, BlockState state, BlockPos pos) {
        if(player.level().isClientSide || player.getUseItem().is(TagsRegistry.DONT_TRIGGER_GRAVESTONE)) return;


        PlayerFightData data = player.getData(AttachmentTypesRegistry.PLAYER_FIGHT_DATA);
        if(!data.isFighting()) {
            if(player instanceof ServerPlayer) ActivateGravestoneTrigger.Instance.trigger((ServerPlayer) player, state.getValue(LEVEL));
            Utils.startGraveyard(player, pos, state.getValue(LEVEL));
        }
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        this.startGraveyard(player, state, pos);

        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rot) {
        return (BlockState)state.setValue(FACING, rot.rotate((Direction)state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{FACING, LEVEL});
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        return 2;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState)this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getClockWise()).setValue(LEVEL, 1);
    }



    static {
        LEVEL = IntegerProperty.create("level", 1, 3);
        FACING = HorizontalDirectionalBlock.FACING;
        NORTH_SHAPE = Block.box(2, 0.0D, 1.0D, 14, 14, 3);
        EAST_SHAPE = Block.box(1.0D, 0.0D, 2.0D, 3.0D, 14.0D, 14.0D);
    }
}
