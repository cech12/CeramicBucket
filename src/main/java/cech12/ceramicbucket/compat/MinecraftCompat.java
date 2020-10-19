package cech12.ceramicbucket.compat;

import cech12.ceramicbucket.api.data.ObtainableEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.tags.FluidTags;

import java.util.ArrayList;
import java.util.List;

public class MinecraftCompat extends ModCompat.Mod implements ModCompat.MobMilkingMod, ModCompat.EntityTypeObtainingMod {

    List<ObtainableEntityType> obtainableEntityTypes;

    public MinecraftCompat() {
        super("minecraft");
        this.obtainableEntityTypes = new ArrayList<>();
        this.obtainableEntityTypes.add(new ObtainableEntityType.Builder(EntityType.PUFFERFISH, Fluids.WATER).build());
        this.obtainableEntityTypes.add(new ObtainableEntityType.Builder(EntityType.SALMON, Fluids.WATER).addFluidTag(FluidTags.WATER).build());
        this.obtainableEntityTypes.add(new ObtainableEntityType.Builder(EntityType.COD, Fluids.WATER).addFluidTag(FluidTags.WATER).build());
        this.obtainableEntityTypes.add(new ObtainableEntityType.Builder(EntityType.TROPICAL_FISH, Fluids.WATER).addFluidTag(FluidTags.WATER).build());
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
    public List<ObtainableEntityType> getObtainableEntityTypes() {
        return this.obtainableEntityTypes;
    }

}
