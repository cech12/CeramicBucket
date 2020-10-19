package cech12.ceramicbucket.compat;

import cech12.ceramicbucket.api.data.ObtainableEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.Fluid;
import net.minecraftforge.fml.ModList;

import java.util.ArrayList;
import java.util.List;

public class ModCompat {

    public static final Mod[] MODS = {
            new MinecraftCompat(),
            new MilkAllTheMobs()
    };

    public static boolean canEntityBeMilked(Entity entity) {
        for (Mod mod : MODS) {
            if (mod.isLoaded() && mod instanceof MobMilkingMod && ((MobMilkingMod)mod).canEntityBeMilked(entity)
                    && (!(entity instanceof LivingEntity) || !((LivingEntity)entity).isChild())) {
                return true;
            }
        }
        return false;
    }

    public static List<ObtainableEntityType> getObtainableEntityTypes() {
        ArrayList<ObtainableEntityType> types = new ArrayList<>();
        for (Mod mod : MODS) {
            if (mod.isLoaded() && mod instanceof EntityTypeObtainingMod) {
                types.addAll(((EntityTypeObtainingMod)mod).getObtainableEntityTypes());
            }
        }
        return types;
    }

    public static boolean canEntityTypeBeObtained(Fluid fluid, EntityType<?> entity) {
        for (Mod mod : MODS) {
            if (mod.isLoaded() && mod instanceof EntityTypeObtainingMod && ((EntityTypeObtainingMod)mod).canEntityTypeBeObtained(fluid, entity)) {
                return true;
            }
        }
        return false;
    }

    public static ObtainableEntityType getObtainableEntityType(EntityType<?> entityType) {
        for (ObtainableEntityType type : getObtainableEntityTypes()) {
            if (type.getEntityType() == entityType) {
                return type;
            }
        }
        return null;
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

        boolean canEntityBeMilked(Entity entity);

    }

    public interface EntityTypeObtainingMod {

        List<ObtainableEntityType> getObtainableEntityTypes();

        default boolean canEntityTypeBeObtained(Fluid fluid, EntityType<?> entityType) {
            for (ObtainableEntityType type : this.getObtainableEntityTypes()) {
                if (type.getEntityType() == entityType && type.isCorrectFluid(fluid)){
                    return true;
                }
            }
            return false;
        }

    }

}
