package com.jedijoe.ImmortuosCalyx.Entity;

import com.jedijoe.ImmortuosCalyx.Infection.InfectionManagerCapability;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.DrownedEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;

public class InfectedDiverEntity extends DrownedEntity {


    public InfectedDiverEntity(EntityType<InfectedDiverEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public static AttributeModifierMap.MutableAttribute customAttributes() {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.MAX_HEALTH, 20.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.2D)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 2D)
                .createMutableAttribute(Attributes.ZOMBIE_SPAWN_REINFORCEMENTS, 0D);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
        //this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 0.3D, false));

    }

    @Override
    protected int getExperiencePoints(PlayerEntity player) {
        return 5 + this.world.rand.nextInt(7);
    }

    ResourceLocation Ambient = new ResourceLocation("immortuoscalyx", "infected_idle");
    ResourceLocation Death = new ResourceLocation("immortuoscalyx", "infected_hurt");
    ResourceLocation Hurt = new ResourceLocation("immortuoscalyx", "infected_death");
    @Override
    protected SoundEvent getAmbientSound() { return new SoundEvent(Ambient); }

    @Override
    protected SoundEvent getDeathSound() {return new SoundEvent(Death); }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) { return new SoundEvent(Hurt); }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.ENTITY_ZOMBIE_STEP, 0.15F, 1.0F);
    }
    @Override
    public void setChild(boolean childZombie) {}
    @Override
    protected boolean shouldBurnInDay() {return false;}
    @Override
    public void livingTick() {
        super.livingTick();
        this.getCapability(InfectionManagerCapability.INSTANCE).ifPresent(h->{
            if(h.getInfectionProgress() < 100) h.setInfectionProgress(100);
        });
    }

    @Nonnull
    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
