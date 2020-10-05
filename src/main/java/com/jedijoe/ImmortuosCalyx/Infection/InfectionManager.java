package com.jedijoe.ImmortuosCalyx.Infection;

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
    protected int infectionTimer;
    protected float resistance;
    public final LazyOptional<IInfectionManager> holder = LazyOptional.of(()->this);
    @Override
    public int getInfectionProgress() { return this.infectionProgress; } //grabs the infection %
    @Override
    public void setInfectionProgress(int infectionProgress) { this.infectionProgress = infectionProgress; } //sets infection %. Maybe for a command later or something.
    @Override
    public void addInfectionProgress(int infectionProgress) { this.infectionProgress += infectionProgress; } //ticks infection % up
    @Override
    public int getInfectionTimer() {return infectionTimer; }
    @Override
    public void addInfectionTimer(int Time) { this.infectionTimer += Time; }

    @Override
    public void setInfectionTimer(int Time) { infectionTimer = Time; }

    @Override
    public float getResistance() { return resistance; }

    @Override
    public void addResistance(float resistance) { this.resistance += resistance;}

    @Override
    public void setResistance(float resistance) { this.resistance = resistance;}

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return holder.cast();
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();
        tag.putInt("infectionProgression", infectionProgress);
        tag.putInt("infectionTimer", infectionTimer);
        tag.putFloat("infectionResistance", resistance);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        infectionProgress = nbt.getInt("infectionProgression");
        infectionTimer = nbt.getInt("infectionTimer");
        resistance = nbt.getFloat("infectionResistance");
    }
}
