package cech12.ceramicbucket.compat;

import cech12.ceramicbucket.api.data.ObtainableEntityType;
import net.minecraft.fluid.Fluids;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class AxolotlCompat extends ModCompat.Mod implements ModCompat.EntityTypeObtainingMod {

    private final List<ObtainableEntityType> obtainableEntityTypes = new ArrayList<>();

    public AxolotlCompat() {
        super("axolotl");
        this.obtainableEntityTypes.add(new ObtainableEntityType.Builder(new ResourceLocation(this.name, "axolotl"), Fluids.WATER).addFluidTag(FluidTags.WATER).build());
        this.obtainableEntityTypes.add(new ObtainableEntityType.Builder(new ResourceLocation(this.name, "axolot_2"), Fluids.WATER).addFluidTag(FluidTags.WATER).build());
    }

    @Override
    public List<ObtainableEntityType> getObtainableEntityTypes() {
        return this.obtainableEntityTypes;
    }

}
