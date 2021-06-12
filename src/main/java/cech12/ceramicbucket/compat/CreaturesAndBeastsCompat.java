package cech12.ceramicbucket.compat;

import cech12.ceramicbucket.api.data.ObtainableEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class CreaturesAndBeastsCompat extends ModCompat.Mod implements ModCompat.EntityTypeObtainingMod {

    private final List<ObtainableEntityType> obtainableEntityTypes = new ArrayList<>();

    public CreaturesAndBeastsCompat() {
        super("cnb");
        this.obtainableEntityTypes.add(new ObtainableEntityType.Builder(new ResourceLocation(this.name, "cindershell"), Fluids.LAVA).addFluidTag(FluidTags.LAVA).build());
    }

    @Override
    public List<ObtainableEntityType> getObtainableEntityTypes() {
        return this.obtainableEntityTypes;
    }

    //TODO let only children be spawned out of a "creative" bucket

    @Override
    public boolean canEntityBeObtained(@Nonnull Fluid fluid, @Nonnull Entity entity) {
        //only support children to be obtainable
        return ModCompat.EntityTypeObtainingMod.super.canEntityBeObtained(fluid, entity)
                && entity instanceof LivingEntity && ((LivingEntity) entity).isChild();
    }

}
