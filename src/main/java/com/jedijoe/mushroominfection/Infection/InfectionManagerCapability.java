package com.jedijoe.mushroominfection.Infection;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

import javax.annotation.Nullable;

public class InfectionManagerCapability {
    @CapabilityInject(IInfectionManager.class)
    public static Capability<IInfectionManager> INSTANCE = null;

    public static class Storage implements Capability.IStorage<IInfectionManager>{

        @Nullable
        @Override
        public INBT writeNBT(Capability<IInfectionManager> capability, IInfectionManager instance, Direction side) {
            CompoundNBT tag = new CompoundNBT();
            tag.putInt("infectionProgression", instance.getInfectionProgress());
            return tag;
        }

        @Override
        public void readNBT(Capability<IInfectionManager> capability, IInfectionManager instance, Direction side, INBT nbt) {
            int infectionProgression = ((CompoundNBT) nbt).getInt("infectionProgression");
            instance.setInfectionProgress(infectionProgression);
        }
    }
}
