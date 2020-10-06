package com.jedijoe.ImmortuosCalyx;

import com.jedijoe.ImmortuosCalyx.Infection.InfectionDamage;
import com.jedijoe.ImmortuosCalyx.Infection.InfectionManagerCapability;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = ImmortuosCalyx.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class NonInfectionEvents {
    @SubscribeEvent
    public static void selfUseCalyxide(PlayerInteractEvent.RightClickItem event){
        if(event.getEntity() instanceof PlayerEntity && event.getItemStack().getItem().equals(Register.CALYXANIDE.get())){
            PlayerEntity player = (PlayerEntity) event.getEntity();
            if(player.isCrouching()) {CalyxideCure(player); event.getItemStack().shrink(1);}


        }
    }

    @SubscribeEvent
    public static void selfUseAntiParasite(PlayerInteractEvent.RightClickItem event){
        if(event.getEntity() instanceof PlayerEntity && event.getItemStack().getItem().equals(Register.GENERALANTIPARASITIC.get())){
            PlayerEntity player = (PlayerEntity) event.getEntity();
            if(player.isCrouching()) {AntiParasiticCure(player); event.getItemStack().shrink(1);}
        }
    }

    @SubscribeEvent
    public static void selfUseCEggs(PlayerInteractEvent.RightClickItem event){
        if(event.getEntity() instanceof PlayerEntity && event.getItemStack().getItem().equals(Register.IMMORTUOSCALYXEGGS.get())){
            PlayerEntity player = (PlayerEntity) event.getEntity();
            if(player.isCrouching()) {
                player.getCapability(InfectionManagerCapability.INSTANCE).ifPresent(h->{
                    if(h.getInfectionProgress() == 0){h.addInfectionProgress(1);}
                });
                event.getItemStack().shrink(1);}
        }
    }

    @SubscribeEvent
    public static void useCalyxide(AttackEntityEvent event){
        Entity target = event.getTarget();
        PlayerEntity user = (PlayerEntity) event.getEntity();
        if(user.getHeldItemMainhand().getItem().equals(Register.CALYXANIDE.get())){
            user.getHeldItemMainhand().shrink(1);
            CalyxideCure(target);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void useAntiParasite(AttackEntityEvent event){
        Entity target = event.getTarget();
        PlayerEntity user = (PlayerEntity) event.getEntity();
        if(user.getHeldItemMainhand().getItem().equals(Register.GENERALANTIPARASITIC.get())){
            user.getHeldItemMainhand().shrink(1);
            AntiParasiticCure(target);
            event.setCanceled(true);
        }
    }
    @SubscribeEvent
    public static void useCEggs(AttackEntityEvent event){
        Entity target = event.getTarget();
        PlayerEntity user = (PlayerEntity) event.getEntity();
        if(user.getHeldItemMainhand().getItem().equals(Register.IMMORTUOSCALYXEGGS.get())){
            user.getHeldItemMainhand().shrink(1);
            target.getCapability(InfectionManagerCapability.INSTANCE).ifPresent(h->{
                if(h.getInfectionProgress() == 0){h.addInfectionProgress(1);}
            });
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void playerResistanceDownTick(TickEvent.PlayerTickEvent event){
        if(event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.START) {
            PlayerEntity playerEntity = event.player;
            playerEntity.getCapability(InfectionManagerCapability.INSTANCE).ifPresent(h->{
                h.addResistance(-0.001f);
                if(h.getResistance() < 1){h.setResistance(1);}
            });
        }
    }

    private static void AntiParasiticCure(Entity target) {
        target.getCapability(InfectionManagerCapability.INSTANCE).ifPresent(h->{
            if(h.getInfectionProgress() <= 40){//is only effective in curing in dormant phase
                h.addInfectionProgress(-10);
            }
            if(h.getInfectionProgress() < 0) h.setInfectionProgress(0);
            h.setResistance(5);
        });
        target.attackEntityFrom(InternalOrganDamage.causeInternalDamage(target), 2f);  //divide the reduced infection rate by 5, multiply by 4. 100 infection rate -> 25/5 = 5, * 4 = 20. Vanilla instant kill.
    }

    private static void CalyxideCure(Entity target){
        target.getCapability(InfectionManagerCapability.INSTANCE).ifPresent(h->{
            if(h.getInfectionProgress() >= 75){// 75 is internal damage threshold.
                int tempinfection = h.getInfectionProgress();;
                tempinfection -= 70; // reduce 75 to 1, 100 to 25, and all the numbers in between
                target.attackEntityFrom(InternalOrganDamage.causeInternalDamage(target), ((tempinfection/5)*4));  //divide the reduced infection rate by 5, multiply by 4. 100 infection rate -> 25/5 = 5, * 4 = 20. Vanilla instant kill.
            }

            h.addInfectionProgress(-40);
            if(h.getInfectionProgress() < 0) h.setInfectionProgress(0);
        });
    }


}
