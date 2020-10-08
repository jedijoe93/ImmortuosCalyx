package com.jedijoe.ImmortuosCalyx;

import com.jedijoe.ImmortuosCalyx.Entity.InfectedDiverEntity;
import com.jedijoe.ImmortuosCalyx.Entity.InfectedHumanEntity;
import com.jedijoe.ImmortuosCalyx.Infection.InfectionDamage;
import com.jedijoe.ImmortuosCalyx.Infection.InfectionManagerCapability;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = ImmortuosCalyx.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class NonInfectionEvents {
    static ResourceLocation injectSounds = new ResourceLocation("immortuoscalyx", "inject");
    static ResourceLocation extractSounds = new ResourceLocation("immortuoscalyx", "extract");
    static SoundEvent soundEvent = new SoundEvent(injectSounds);
    static SoundEvent soundEvent1 = new SoundEvent(extractSounds);
    @SubscribeEvent
    public static void selfUseCalyxide(PlayerInteractEvent.RightClickItem event){
        if(event.getEntity() instanceof PlayerEntity && event.getItemStack().getItem().equals(Register.CALYXANIDE.get())){
            PlayerEntity player = (PlayerEntity) event.getEntity();
            if(player.isCrouching()) {CalyxideCure(player); event.getItemStack().shrink(1);}
            if(event.getSide() == LogicalSide.SERVER){event.getWorld().playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), Register.INJECT.get(), SoundCategory.PLAYERS, 1f, 1f);}


        }
    }

    @SubscribeEvent
    public static void selfUseAntiParasite(PlayerInteractEvent.RightClickItem event){
        if(event.getEntity() instanceof PlayerEntity && event.getItemStack().getItem().equals(Register.GENERALANTIPARASITIC.get())){
            PlayerEntity player = (PlayerEntity) event.getEntity();
            if(player.isCrouching()) {AntiParasiticCure(player); event.getItemStack().shrink(1);}
            if(event.getSide() == LogicalSide.SERVER){event.getWorld().playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), Register.INJECT.get(), SoundCategory.PLAYERS, 1f, 1f);}
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
            if(event.getSide() == LogicalSide.SERVER){event.getWorld().playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), Register.INJECT.get(), SoundCategory.PLAYERS, 1f, 1f);}

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
            if(!event.getTarget().world.isRemote()){target.getEntityWorld().playSound(null, target.getPosX(), target.getPosY(), target.getPosZ(), Register.INJECT.get(), SoundCategory.PLAYERS, 1f, 1f);}

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
            if(!event.getTarget().world.isRemote()){target.getEntityWorld().playSound(null, target.getPosX(), target.getPosY(), target.getPosZ(), Register.INJECT.get(), SoundCategory.PLAYERS, 1f, 1f);}
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
            if(!event.getTarget().world.isRemote()){target.getEntityWorld().playSound(null, target.getPosX(), target.getPosY(), target.getPosZ(), Register.INJECT.get(), SoundCategory.PLAYERS, 1f, 1f);}
            if(target instanceof InfectedDiverEntity || target instanceof InfectedHumanEntity){((MonsterEntity) target).addPotionEffect(new EffectInstance(Effects.WITHER, 500, 25, true, false));}
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

    @SubscribeEvent
    public static void playerExtract(AttackEntityEvent event){
        Entity target = event.getTarget();
        if(event.getTarget() instanceof SlimeEntity && event.getEntity() instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) event.getEntity();
            if(player.getHeldItemMainhand().getItem().equals(Register.SYRINGE.get().getItem())){
                ItemStack olditemstack = player.getHeldItemMainhand();
                olditemstack.shrink(1);
                ItemStack itemStack = new ItemStack(Register.GENERALANTIPARASITIC.get());
                player.inventory.addItemStackToInventory(itemStack);
                if(!event.getTarget().world.isRemote()){target.getEntityWorld().playSound(null, target.getPosX(), target.getPosY(), target.getPosZ(), Register.EXTRACT.get(), SoundCategory.PLAYERS, 1f, 1f);}
            }
        }else if((event.getTarget() instanceof InfectedHumanEntity) || (event.getTarget() instanceof InfectedDiverEntity) && event.getEntity() instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) event.getEntity();
            if(player.getHeldItemMainhand().getItem().equals(Register.SYRINGE.get().getItem())){
                ItemStack olditemstack = player.getHeldItemMainhand();
                olditemstack.shrink(1);
                ItemStack itemStack = new ItemStack(Register.IMMORTUOSCALYXEGGS.get());
                player.inventory.addItemStackToInventory(itemStack);
                if(!target.getEntityWorld().isRemote()) {target.getEntityWorld().playSound(null, target.getPosX(), target.getPosY(), target.getPosZ(), soundEvent1, SoundCategory.PLAYERS, 1f, 1f);}
            }
        }
    }

    @SubscribeEvent
    public static void selfScan(PlayerInteractEvent.RightClickItem event){
        if(event.getSide() == LogicalSide.SERVER) {
            if (event.getEntity() instanceof PlayerEntity && event.getItemStack().getItem().equals(Register.SCANNER.get()) && event.getEntity().isCrouching()) {
                PlayerEntity p = (PlayerEntity) event.getEntity();
                event.getEntity().getCapability(InfectionManagerCapability.INSTANCE).ifPresent(h -> {
                    p.sendMessage(new StringTextComponent(p.getScoreboardName() + "'s stats"), p.getUniqueID());
                    p.sendMessage(new StringTextComponent("Saturation level: " + p.getFoodStats().getSaturationLevel()), p.getUniqueID());
                    p.sendMessage(new StringTextComponent("Infection Level: " + h.getInfectionProgress() + "%"), p.getUniqueID());
                    p.sendMessage(new StringTextComponent("Resistance Multiplier: " + h.getResistance()), p.getUniqueID());
                });
            }
        }
    }

    @SubscribeEvent
    public static void Scan(AttackEntityEvent event){
        if(!event.getEntity().getEntityWorld().isRemote()) {
            if (event.getEntity() instanceof PlayerEntity && ((PlayerEntity) event.getEntity()).getHeldItemMainhand().getItem().equals(Register.SCANNER.get())) {
                event.setCanceled(true);
                if (event.getTarget() instanceof PlayerEntity) {
                    PlayerEntity t = (PlayerEntity) event.getTarget();
                    PlayerEntity a = (PlayerEntity) event.getEntity();

                    t.getCapability(InfectionManagerCapability.INSTANCE).ifPresent(h -> {
                        a.sendMessage(new StringTextComponent(a.getScoreboardName() + "'s stats"), a.getUniqueID());
                        a.sendMessage(new StringTextComponent("Health: " + a.getHealth()), a.getUniqueID());
                        a.sendMessage(new StringTextComponent("Food: " + a.getFoodStats().getFoodLevel()), a.getUniqueID());
                        a.sendMessage(new StringTextComponent("Infection Level: " + h.getInfectionProgress() + "%"), a.getUniqueID());
                        a.sendMessage(new StringTextComponent("Resistance Multiplier: " + h.getResistance()), a.getUniqueID());
                    });
                } else if (event.getTarget() instanceof InfectedHumanEntity || event.getTarget() instanceof InfectedDiverEntity) {
                    event.getEntity().sendMessage(new StringTextComponent("Target completely infected."), event.getEntity().getUniqueID());
                } else {
                    event.getEntity().sendMessage(new StringTextComponent("Invalid Target."), event.getEntity().getUniqueID());
                }
            }
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
