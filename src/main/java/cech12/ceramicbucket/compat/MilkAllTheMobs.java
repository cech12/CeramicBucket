package cech12.ceramicbucket.compat;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.animal.horse.Donkey;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.animal.horse.Mule;

public class MilkAllTheMobs extends ModCompat.Mod implements ModCompat.MobMilkingMod {

    public MilkAllTheMobs() {
        super("milkatmobs");
    }

    @Override
    public boolean canEntityBeMilked(Entity entity) {
        return entity instanceof Sheep
                || entity instanceof Llama
                || entity instanceof Pig
                || entity instanceof Donkey
                || entity instanceof Horse
                || entity instanceof Mule;
    }
}
