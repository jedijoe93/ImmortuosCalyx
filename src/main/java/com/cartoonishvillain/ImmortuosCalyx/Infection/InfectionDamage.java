package com.cartoonishvillain.ImmortuosCalyx.Infection;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;

public class InfectionDamage extends DamageSource {
    public InfectionDamage(String p_i1566_1_) {
        super(p_i1566_1_);
    }

    public static DamageSource causeInfectionDamage(Entity entity){
        return (new EntityDamageSource("infection", entity)).bypassArmor().bypassMagic();
    }


}
