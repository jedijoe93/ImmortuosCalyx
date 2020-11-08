package com.jedijoe.ImmortuosCalyx.Infection;

import com.jedijoe.ImmortuosCalyx.ImmortuosCalyx;
import com.jedijoe.ImmortuosCalyx.Register;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Effects;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.atomic.AtomicInteger;

@Mod.EventBusSubscriber(modid = ImmortuosCalyx.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EntityInfectionEventManager {
    @SubscribeEvent
    public static void InfectionTicker(LivingEvent.LivingUpdateEvent event){
        LivingEntity entity = event.getEntityLiving();
        if(!(entity instanceof PlayerEntity)){
        entity.getCapability(InfectionManagerCapability.INSTANCE).ifPresent(h->{
            if(h.getInfectionProgress() >= 1){
                 h.addInfectionTimer(1);
                 int timer = -1;
                 if (entity instanceof VillagerEntity){timer = ImmortuosCalyx.config.VILLAGERINFECTIONTIMER.get();}
                 else {timer = ImmortuosCalyx.config.INFECTIONTIMER.get();}
                    if(h.getInfectionTimer() >= timer){
                       h.addInfectionProgress(1);
                        h.addInfectionTimer(-timer);
                    }
                }
            });
        }
    }

    @SubscribeEvent
    public static void InfectionLogic(LivingEvent.LivingUpdateEvent event){
        AtomicInteger infectionlevel = new AtomicInteger(0);
        LivingEntity entity = event.getEntityLiving();
        entity.getCapability(InfectionManagerCapability.INSTANCE).ifPresent(h->{
            infectionlevel.getAndSet(h.getInfectionProgress());
        });
        int level = infectionlevel.get();
        if(level > 0){
            if(entity instanceof VillagerEntity){

                if(level >= 25){
                    entity.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 5, 2, false, false));
                }  else if(level >= 5){
                    entity.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 5, 1, false, false));
                }
            }
        }
    }

    @SubscribeEvent
    public static void antiTrade(PlayerInteractEvent.EntityInteract event) {
        if(!event.getTarget().getEntityWorld().isRemote() && event.getHand() == Hand.MAIN_HAND && event.getTarget() instanceof VillagerEntity){
            VillagerEntity villager = (VillagerEntity) event.getTarget();
            villager.getCapability(InfectionManagerCapability.INSTANCE).ifPresent(h->{
                if(h.getInfectionProgress() >= 37){
                    event.setCanceled(true);
                    villager.setShakeHeadTicks(40);
                    villager.getEntityWorld().playSound(null, villager.getPosX(), villager.getPosY(), villager.getPosZ(), SoundEvents.ENTITY_VILLAGER_NO, SoundCategory.NEUTRAL, 1f, 1f);
                }
            });

        }
    }
}
