package cech12.ceramicbucket.compat;

import net.minecraft.entity.LivingEntity;
import net.minecraftforge.fml.ModList;

public class ModCompat {

    public static final Mod[] MODS = {
            new MilkAllTheMobs_1_14(),
            new MilkAllTheMobs_1_15()
    };

    public static boolean canEntityBeMilked(LivingEntity entity) {
        for (Mod mod : MODS) {
            if (mod.isLoaded() && mod instanceof MobMilkingMod && ((MobMilkingMod)mod).canEntityBeMilked(entity)) {
                return true;
            }
        }
        return false;
    }

    public static class Mod {

        protected String name;

        public Mod(String name) {
            this.name = name;
        }

        public boolean isLoaded() {
            return ModList.get().isLoaded(this.name);
        }

    }

    public interface MobMilkingMod {

        boolean canEntityBeMilked(LivingEntity entity);

    }

}
