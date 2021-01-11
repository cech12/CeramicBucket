package cech12.ceramicbucket.compat;

import cech12.ceramicbucket.api.data.ObtainableEntityType;
import net.minecraft.fluid.Fluids;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class EnvironmentalCompat extends ModCompat.Mod implements ModCompat.EntityTypeObtainingMod {

    private final List<ObtainableEntityType> obtainableEntityTypes = new ArrayList<>();

    public EnvironmentalCompat() {
        super("environmental");
        this.addFish("slabfish");
        this.addFish("koi");
    }

    private void addFish(String fish) {
        this.obtainableEntityTypes.add(new ObtainableEntityType.Builder(new ResourceLocation(this.name, fish), Fluids.WATER).addFluidTag(FluidTags.WATER).build());
    }

    @Override
    public List<ObtainableEntityType> getObtainableEntityTypes() {
        return this.obtainableEntityTypes;
    }

}
