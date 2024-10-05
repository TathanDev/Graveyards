package fr.tathan.graveyards.common.blocks;

import com.mojang.serialization.MapCodec;
import fr.tathan.graveyards.common.datas.PlayerFightData;
import fr.tathan.graveyards.common.registries.AttachmentTypesRegistry;
import fr.tathan.graveyards.common.utils.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;

public class GraveyardBlock extends Block {

    public static final IntegerProperty LEVEL;
    public static final DirectionProperty FACING;

    public static final MapCodec<GraveyardBlock> CODEC = simpleCodec(GraveyardBlock::new);

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    public GraveyardBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(LEVEL, 1));
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));

    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {

        this.startGraveyard(player, state, pos);

        return super.useWithoutItem(state, level, pos, player, hitResult);

    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if(entity instanceof Player player) {
            this.startGraveyard(player, state, pos);
        }

        super.stepOn(level, pos, state, entity);
    }

    private void startGraveyard(Player player, BlockState state, BlockPos pos) {
        PlayerFightData data = player.getData(AttachmentTypesRegistry.PLAYER_FIGHT_DATA);
        if(!data.isFighting()) {
            Utils.startGraveyard(player, pos, state.getValue(LEVEL));
        }
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
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState)this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getClockWise()).setValue(LEVEL, 1);
    }

    static {
        LEVEL = IntegerProperty.create("level", 1, 3);
        FACING = HorizontalDirectionalBlock.FACING;

    }
}
