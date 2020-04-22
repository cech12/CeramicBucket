package cech12.ceramicbucket.compat;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.passive.horse.DonkeyEntity;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.entity.passive.horse.LlamaEntity;
import net.minecraft.entity.passive.horse.MuleEntity;

public class MilkAllTheMobs_1_15 extends ModCompat.Mod implements ModCompat.MobMilkingMod {

    public MilkAllTheMobs_1_15() {
        super("milkatmobs");
    }

    @Override
    public boolean canEntityBeMilked(LivingEntity entity) {
        return entity instanceof SheepEntity
                || entity instanceof LlamaEntity
                || entity instanceof PigEntity
                || entity instanceof DonkeyEntity
                || entity instanceof HorseEntity
                || entity instanceof MuleEntity;
    }
}
