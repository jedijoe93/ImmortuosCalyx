package com.jedijoe.mushroominfection.Infection;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
public class InfectionManager implements IInfectionManager, ICapabilityProvider, INBTSerializable<CompoundNBT> {
    protected int infectionProgress;
    public final LazyOptional<IInfectionManager> holder = LazyOptional.of(()->this);
    @Override
    public int getInfectionProgress() {
        return this.infectionProgress;
    }

    @Override
    public void setInfectionProgress(int infectionProgress) {
        this.infectionProgress = infectionProgress;
    }

    @Override
    public void addInfectionProgress(int infectionProgress) {
        this.infectionProgress += infectionProgress;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return holder.cast();
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();
        tag.putInt("infectionProgression", infectionProgress);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        infectionProgress = nbt.getInt("infectionProgression");
    }
}
