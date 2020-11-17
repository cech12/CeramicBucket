package cech12.ceramicbucket.compat;

import cech12.ceramicbucket.api.data.ObtainableEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.Fluid;
import net.minecraftforge.fml.ModList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ModCompat {

    public static final Mod[] MODS = {
            new MinecraftCompat(),
            new AquacultureCompat(),
            new AxolotlCompat(),
            new CombustiveFishingCompat(),
            new MilkAllTheMobs(),
            new QuarkCompat(),
            new UpgradeAquaticCompat()
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

    public static boolean canEntityBeObtained(@Nonnull Fluid fluid, @Nullable Entity entity) {
        if (entity != null) {
            for (Mod mod : MODS) {
                if (mod.isLoaded() && mod instanceof EntityTypeObtainingMod && ((EntityTypeObtainingMod)mod).canEntityBeObtained(fluid, entity)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static ObtainableEntityType getObtainableEntityType(@Nullable EntityType<?> entityType) {
        if (entityType != null) {
            for (ObtainableEntityType type : getObtainableEntityTypes()) {
                if (type.getEntityType() == entityType) {
                    return type;
                }
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

        default boolean canEntityBeObtained(@Nonnull Fluid fluid, @Nonnull Entity entity) {
            EntityType<?> entityType = entity.getType();
            for (ObtainableEntityType type : this.getObtainableEntityTypes()) {
                if (type.getEntityType() == entityType && type.isCorrectFluid(fluid)){
                    return true;
                }
            }
            return false;
        }

    }

}
