package cech12.ceramicbucket.compat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.fluid.Fluids;

import java.util.ArrayList;
import java.util.List;

public class MinecraftCompat extends ModCompat.Mod implements ModCompat.MobMilkingMod, ModCompat.EntityTypeObtainingMod {

    List<ModCompat.ObtainableEntityType> obtainableEntityTypes;

    public MinecraftCompat() {
        super("minecraft");
        this.obtainableEntityTypes = new ArrayList<>();
        this.obtainableEntityTypes.add(new ModCompat.ObtainableEntityType(Fluids.WATER, EntityType.PUFFERFISH));
        this.obtainableEntityTypes.add(new ModCompat.ObtainableEntityType(Fluids.WATER, EntityType.SALMON));
        this.obtainableEntityTypes.add(new ModCompat.ObtainableEntityType(Fluids.WATER, EntityType.COD));
        this.obtainableEntityTypes.add(new ModCompat.ObtainableEntityType(Fluids.WATER, EntityType.TROPICAL_FISH));
    }

    @Override
    public boolean isLoaded() {
        return true;
    }

    @Override
    public boolean canEntityBeMilked(Entity entity) {
        return entity instanceof CowEntity;
    }

    @Override
    public List<ModCompat.ObtainableEntityType> getObtainableEntityTypes() {
        return this.obtainableEntityTypes;
    }

}
