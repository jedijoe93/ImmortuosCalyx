package com.jedijoe.ImmortuosCalyx.Blocks;

import com.jedijoe.ImmortuosCalyx.Infection.InfectionManagerCapability;
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

public class InfectionScanner extends Block {
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public InfectionScanner() {
        super(Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).setRequiresTool());
        this.setDefaultState(getDefaultState().with(POWERED, false));
    }

    @Override
    public boolean canProvidePower(BlockState p_149744_1_) {
        return true;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
    }

    @Override
    public void onEntityWalk(World world, BlockPos blockPos, Entity entity) {
        BlockState blockState = world.getBlockState(blockPos);
        if(!world.isRemote()){
        blockState = redstoneStrength(entity, blockState);}
        world.setBlockState(blockPos, blockState.with(POWERED, blockState.get(POWERED)));
    }

    @Nonnull
    private BlockState redstoneStrength(Entity infected, BlockState state) {
        AtomicBoolean isinfected = new AtomicBoolean(false);
        infected.getCapability(InfectionManagerCapability.INSTANCE).ifPresent(h->{
            isinfected.set(h.getInfectionProgress() > 0);
        });
        boolean logic = isinfected.get();
        return state.with(POWERED, logic);
    }
    @Override
    public int getWeakPower (BlockState state, IBlockReader blockAccess, BlockPos pos, Direction side) { return state.get(POWERED) ? 15 : 0; }

}
