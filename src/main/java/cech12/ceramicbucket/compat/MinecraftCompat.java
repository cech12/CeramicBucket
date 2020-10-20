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

    List<ObtainableEntityType> obtainableEntityTypes = new ArrayList<>();

    public MinecraftCompat() {
        super("minecraft");
        this.addFish(EntityType.PUFFERFISH);
        this.addFish(EntityType.SALMON);
        this.addFish(EntityType.COD);
        this.addFish(EntityType.TROPICAL_FISH);
    }

    private void addFish(EntityType<?> fish) {
        this.obtainableEntityTypes.add(new ObtainableEntityType.Builder(fish, Fluids.WATER).addFluidTag(FluidTags.WATER).build());
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
