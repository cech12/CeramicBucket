package cech12.ceramicbucket.compat;

import cech12.ceramicbucket.api.data.ObtainableEntityType;
import net.minecraft.fluid.Fluids;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class AlexsMobsCompat extends ModCompat.Mod implements ModCompat.EntityTypeObtainingMod {

    private final List<ObtainableEntityType> obtainableEntityTypes = new ArrayList<>();

    public AlexsMobsCompat() {
        super("alexsmobs");
        this.addFish("lobster");
        this.addFish("blobfish");
        this.addFish("platypus");
        this.addFish("frilled_shark");
        this.addFish("mimic_octopus");
        //stradpole is a lava fish
        this.obtainableEntityTypes.add(new ObtainableEntityType.Builder(new ResourceLocation(this.name, "stradpole"), Fluids.LAVA).addFluidTag(FluidTags.LAVA).build());
    }

    private void addFish(String fish) {
        this.obtainableEntityTypes.add(new ObtainableEntityType.Builder(new ResourceLocation(this.name, fish), Fluids.WATER).addFluidTag(FluidTags.WATER).build());
    }

    @Override
    public List<ObtainableEntityType> getObtainableEntityTypes() {
        return this.obtainableEntityTypes;
    }

}
