package com.jedijoe.ImmortuosCalyx.Infection;

import com.google.common.util.concurrent.AtomicDouble;
import com.jedijoe.ImmortuosCalyx.Entity.InfectedDiverEntity;
import com.jedijoe.ImmortuosCalyx.Entity.InfectedHumanEntity;
import com.jedijoe.ImmortuosCalyx.ImmortuosCalyx;
import com.jedijoe.ImmortuosCalyx.Register;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.Item;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.world.NoteBlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Mod.EventBusSubscriber(modid = ImmortuosCalyx.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class InfectionEventManager {

    @SubscribeEvent
    public static void onAttachCapabilitiesEvent(AttachCapabilitiesEvent<Entity> event){
        if(event.getObject() instanceof LivingEntity){
            InfectionManager provider = new InfectionManager();
            event.addCapability(new ResourceLocation(ImmortuosCalyx.MOD_ID, "infection"), provider);
        }
    }

    @SubscribeEvent
    public static void InfectionTicker(TickEvent.PlayerTickEvent event){
        if(event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.START){
        PlayerEntity player = event.player;
        player.getCapability(InfectionManagerCapability.INSTANCE).ifPresent(h ->{
            if(h.getInfectionProgress() >= 1){
                h.addInfectionTimer(1);
                if(h.getInfectionTimer() == 450){
                    h.addInfectionProgress(1);
                    h.addInfectionTimer(-450);
                    EffectController(event.player);
                }
            }
        });}
    }

    @SubscribeEvent public static void InfectionTickEffects(TickEvent.PlayerTickEvent event){
        if(event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.START){
            PlayerEntity player = event.player;
            player.getCapability(InfectionManagerCapability.INSTANCE).ifPresent(h->{
                if(h.getInfectionProgress() >= 60){
                    BlockPos CurrentPosition = new BlockPos(player.getPosX(), player.getPosY(), player.getPosZ());
                    float temperature = player.world.getBiomeManager().getBiome(CurrentPosition).getTemperature(CurrentPosition);
                    if(temperature > 0.9){player.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 5, 0, true, false));}
                    else if(temperature < 0.275){player.addPotionEffect(new EffectInstance(Effects.SPEED, 5, 0, true, false));}
                }
                if(h.getInfectionProgress() >= 85){
                    BlockPos CurrentPosition = new BlockPos(player.getPosX(), player.getPosY(), player.getPosZ());
                    float temperature = player.world.getBiomeManager().getBiome(CurrentPosition).getTemperature(CurrentPosition);
                    if(temperature > 0.275){player.addPotionEffect(new EffectInstance(Effects.WEAKNESS, 5, 0, true, false));}
                    else {player.addPotionEffect(new EffectInstance(Effects.STRENGTH, 5, 0, true, false));}
                }
                if(h.getInfectionProgress() >= 95){
                    player.addPotionEffect(new EffectInstance(Effects.BLINDNESS, 50, 1, true, false));
                }
                if(h.getInfectionProgress() >= 100){
                    Random random = new Random();
                    int randomValue = random.nextInt(100);
                    if(randomValue < 1){
                        player.attackEntityFrom(InfectionDamage.causeInfectionDamage(player), 1.0f);
                    }
                }
            });

        }
    }
    static ResourceLocation Ambient = new ResourceLocation("immortuoscalyx", "infected_idle");
    static ResourceLocation Hurt = new ResourceLocation("immortuoscalyx", "infected_hurt");
    @SubscribeEvent
    public static void InfectionChat(ServerChatEvent event){
        PlayerEntity player = event.getPlayer();
        player.getCapability(InfectionManagerCapability.INSTANCE).ifPresent(h->{
            if(h.getInfectionProgress() >= 40) {event.setCanceled(true);//if the player's infection is @ or above 40%, they can no longer speak in text chat.
            if(!player.getEntityWorld().isRemote())player.world.playSound(null, player.getPosition(), Register.AMBIENT.get(), SoundCategory.PLAYERS, 0.5f, 2f);}
        });
    }

    @SubscribeEvent public static void InfectOtherPlayer(AttackEntityEvent event){
        if(event.getEntity() instanceof PlayerEntity && event.getTarget() instanceof PlayerEntity){
            PlayerEntity target = (PlayerEntity) event.getTarget(); //the player who got hit
            PlayerEntity aggro = (PlayerEntity) event.getEntity(); //the player that hit
            int protection = target.getTotalArmorValue(); //grabs the armor value of the target
            AtomicInteger infectionRateGrabber = new AtomicInteger(); //funky value time
            aggro.getCapability(InfectionManagerCapability.INSTANCE).ifPresent(h->{
                int infectionProgression = h.getInfectionProgress();
                infectionRateGrabber.addAndGet(infectionProgression); // sets the atomic int equal to the infection % of the attacker

            });
            AtomicDouble resistRateGrabber = new AtomicDouble(); //grab resistance from
            target.getCapability(InfectionManagerCapability.INSTANCE).ifPresent(h->{
                Double resist = Double.valueOf(h.getResistance());
                resistRateGrabber.addAndGet(resist);
            });
            AtomicBoolean infectedGrabber = new AtomicBoolean(false); //A surprise bool that will help us later
            int convert = infectionRateGrabber.intValue();// converts the atomic int into a normal int for better math
            float resist = (float) resistRateGrabber.get();
            if(convert > 49){ // if the attacker's infection rate is at or above 50%
                int conversionThreshold = (int) ((convert - (protection*2))/resist); // creates mimimum score needed to not get infected
                Random rand = new Random();
                if(conversionThreshold > rand.nextInt(100)){ // rolls for infection. If random value rolls below threshold, target is at risk of infection.
                    target.getCapability(InfectionManagerCapability.INSTANCE).ifPresent(h->{
                        if(h.getInfectionProgress() == 0){h.addInfectionProgress(1); infectedGrabber.set(true);} // if target is not already infected, and the risk for infection exists, they are freshly infected. Surprise bool gets activated
                    });
                }
            }
            if(infectedGrabber.get()){aggro.getCapability(InfectionManagerCapability.INSTANCE).ifPresent(h->{ //if surprise bool is activated, it means the aggressor player successfully infected someone. Removing part of the parasite and having it get on someone else.
                h.addInfectionProgress(-5); });//because of that, symptoms are reduced.
                if(!aggro.getEntityWorld().isRemote)aggro.world.playSound(null, aggro.getPosition(), Register.HURT.get(), SoundCategory.PLAYERS, 1f, 1.2f);}
        }
    }

    @SubscribeEvent public static void InfectFromMobs(net.minecraftforge.event.entity.living.LivingAttackEvent event){
        if((event.getSource().getTrueSource() instanceof InfectedHumanEntity || event.getSource().getTrueSource() instanceof InfectedDiverEntity) && event.getEntityLiving() instanceof PlayerEntity){
            int convert = 95;
            int protection = ((PlayerEntity) event.getEntityLiving()).getTotalArmorValue();
            AtomicDouble resistRateGrabber = new AtomicDouble(); //grab resistance from
            event.getEntityLiving().getCapability(InfectionManagerCapability.INSTANCE).ifPresent(h->{
                Double resist = Double.valueOf(h.getResistance());
                resistRateGrabber.addAndGet(resist);
            });
            float resist = (float) resistRateGrabber.get();
            AtomicBoolean infectedGrabber = new AtomicBoolean(false); //A surprise bool that will help us later
            int conversionThreshold = (int) ((convert - (protection*2))/resist); // creates mimimum score needed to not get infected
            Random rand = new Random();
            if(conversionThreshold > rand.nextInt(100)) { // rolls for infection. If random value rolls below threshold, target is at risk of infection.
                event.getEntityLiving().getCapability(InfectionManagerCapability.INSTANCE).ifPresent(h -> {
                    if (h.getInfectionProgress() == 0) {
                        h.addInfectionProgress(1);
                        infectedGrabber.set(true);
                    } // if target is not already infected, and the risk for infection exists, they are freshly infected. Surprise bool gets activated
                });
            }
        }else if(event.getSource().getTrueSource() instanceof ZombieEntity && event.getEntityLiving() instanceof PlayerEntity){
            int convert = 19;
            int protection = ((PlayerEntity) event.getEntityLiving()).getTotalArmorValue();
            AtomicDouble resistRateGrabber = new AtomicDouble(); //grab resistance from
            event.getEntityLiving().getCapability(InfectionManagerCapability.INSTANCE).ifPresent(h->{
                Double resist = Double.valueOf(h.getResistance());
                resistRateGrabber.addAndGet(resist);
            });
            float resist = (float) resistRateGrabber.get();
            AtomicBoolean infectedGrabber = new AtomicBoolean(false); //A surprise bool that will help us later
            int conversionThreshold = (int) ((convert - (protection*2))/resist); // creates mimimum score needed to not get infected
            Random rand = new Random();
            if(conversionThreshold > rand.nextInt(100)) { // rolls for infection. If random value rolls below threshold, target is at risk of infection.
                event.getEntityLiving().getCapability(InfectionManagerCapability.INSTANCE).ifPresent(h -> {
                    if (h.getInfectionProgress() == 0) {
                        h.addInfectionProgress(1);
                        infectedGrabber.set(true);
                    } // if target is not already infected, and the risk for infection exists, they are freshly infected. Surprise bool gets activated
                });
            }

        }
    }

    @SubscribeEvent
    public static void deathReplacement(LivingDeathEvent event){
        if (event.getEntityLiving() instanceof PlayerEntity){
            PlayerEntity deadPlayer = (PlayerEntity) event.getEntityLiving();
            if(event.getSource().damageType == "infection"){
                World world = deadPlayer.getEntityWorld();
                if(!world.isRemote()){
                    ServerWorld serverWorld = (ServerWorld) world;
                    InfectedHumanEntity infectedHumanEntity = new InfectedHumanEntity(Register.INFECTEDHUMAN.get(), world);
                    infectedHumanEntity.setPosition(deadPlayer.getPosX(), deadPlayer.getPosY(), deadPlayer.getPosZ());
                    Register.INFECTEDHUMAN.get().spawn(serverWorld, new ItemStack(Items.AIR), null, deadPlayer.getPosition(), SpawnReason.TRIGGERED, true, false);
                }
            }
        }
    }
    static Item[] rawItem = new Item[]{Items.BEEF, Items.RABBIT, Items.CHICKEN, Items.PORKCHOP, Items.MUTTON, Items.COD, Items.SALMON};
    @SubscribeEvent
    public static void rawFood(LivingEntityUseItemEvent.Finish event){
        if(event.getEntity() instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) event.getEntity();
            boolean raw = false;
            for(Item item : rawItem){if(item.equals(event.getItem().getItem())){raw = true; break;}}
            if(raw){
                player.getCapability(InfectionManagerCapability.INSTANCE).ifPresent(h->{
                    if(h.getInfectionProgress() == 0){
                        Random rand = new Random();
                        if(rand.nextInt(100) < (10/(h.getResistance()))) h.setInfectionProgress(1);
                    }
                });
            }
        }
    }

    private static void EffectController(PlayerEntity inflictedPlayer){
        inflictedPlayer.getCapability(InfectionManagerCapability.INSTANCE).ifPresent(h->{
            int progressionLogic = h.getInfectionProgress();
            switch(progressionLogic){
                default:
                    break;
                case 10:
                    inflictedPlayer.sendMessage(new StringTextComponent(TextFormatting.RED + "Your throat feels sore"), inflictedPlayer.getUniqueID());
                    break;
                case 25:
                    inflictedPlayer.sendMessage(new StringTextComponent(TextFormatting.RED + "Your mind feels foggy"), inflictedPlayer.getUniqueID());
                    break;
                case 40:
                    inflictedPlayer.sendMessage(new StringTextComponent(TextFormatting.RED + "You feel something moving around in your head, you try to yell, but nothing comes out"), inflictedPlayer.getUniqueID());
                    break;
                case 50:
                    inflictedPlayer.sendMessage(new StringTextComponent(TextFormatting.RED + "There is something on your skin and you can't get it off.."), inflictedPlayer.getUniqueID());
                    break;
                case 60:
                    inflictedPlayer.sendMessage(new StringTextComponent(TextFormatting.RED + "You start feeling ill in warm environments, and better in cool ones.."), inflictedPlayer.getUniqueID());
                    break;
                case 85:
                    inflictedPlayer.sendMessage(new StringTextComponent(TextFormatting.RED + "You begin to feel weak in all but the coldest environments.."), inflictedPlayer.getUniqueID());
                    break;
                case 95:
                    inflictedPlayer.sendMessage(new StringTextComponent(TextFormatting.RED + "Your vision gets darker and darker.."), inflictedPlayer.getUniqueID());
                    break;
                case 100:
                    inflictedPlayer.sendMessage(new StringTextComponent(TextFormatting.RED + "You feel an overwhelming pain in your head..."), inflictedPlayer.getUniqueID());
                    break;
            }
        });
    }
}
