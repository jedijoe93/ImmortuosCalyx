package com.cartoonishvillain.ImmortuosCalyx.Blocks;

import com.cartoonishvillain.ImmortuosCalyx.Infection.InfectionManagerCapability;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nonnull;
import java.util.concurrent.atomic.AtomicBoolean;

import net.minecraft.block.AbstractBlock.Properties;

public class InfectionScanner extends Block {
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public InfectionScanner() {
        super(Properties.of(Material.STONE).strength(3f).harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops());
        this.registerDefaultState(defaultBlockState().setValue(POWERED, false));
    }

    @Override
    public boolean isSignalSource(BlockState p_149744_1_) {
        return true;
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
    }

    @Override
    public void stepOn(World world, BlockPos blockPos, Entity entity) {
        BlockState blockState = world.getBlockState(blockPos);
        if(!world.isClientSide()){
        blockState = redstoneStrength(entity, blockState);}
        world.setBlockAndUpdate(blockPos, blockState.setValue(POWERED, blockState.getValue(POWERED)));
    }

    @Nonnull
    private BlockState redstoneStrength(Entity infected, BlockState state) {
        AtomicBoolean isinfected = new AtomicBoolean(false);
        infected.getCapability(InfectionManagerCapability.INSTANCE).ifPresent(h->{
            isinfected.set(h.getInfectionProgress() > 0);
        });
        boolean logic = isinfected.get();
        return state.setValue(POWERED, logic);
    }
    @Override
    public int getSignal (BlockState state, IBlockReader blockAccess, BlockPos pos, Direction side) { return state.getValue(POWERED) ? 15 : 0; }

}
