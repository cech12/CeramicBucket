package cech12.ceramicbucket.compat;

import cech12.ceramicbucket.api.data.ObtainableEntityType;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.tags.FluidTags;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class AquacultureCompat extends ModCompat.Mod implements ModCompat.EntityTypeObtainingMod {

    private final List<ObtainableEntityType> obtainableEntityTypes = new ArrayList<>();

    public AquacultureCompat() {
        super("aquaculture");
        this.addFish("arapaima");
        this.addFish("atlantic_cod");
        this.addFish("atlantic_halibut");
        this.addFish("atlantic_herring");
        this.addFish("bayad");
        this.addFish("blackfish");
        this.addFish("bluegill");
        this.addFish("boulti");
        this.addFish("brown_shrooma");
        this.addFish("brown_trout");
        this.addFish("capitaine");
        this.addFish("carp");
        this.addFish("catfish");
        this.addFish("gar");
        this.addFish("jellyfish");
        this.addFish("minnow");
        this.addFish("muskellunge");
        this.addFish("pacific_halibut");
        this.addFish("perch");
        this.addFish("pink_salmon");
        this.addFish("piranha");
        this.addFish("pollock");
        this.addFish("rainbow_trout");
        this.addFish("red_grouper");
        this.addFish("red_shrooma");
        this.addFish("smallmouth_bass");
        this.addFish("synodontis");
        this.addFish("tambaqui");
        this.addFish("tuna");
    }

    private void addFish(String fish) {
        this.obtainableEntityTypes.add(new ObtainableEntityType.Builder(new ResourceLocation(this.name, fish), Fluids.WATER).addFluidTag(FluidTags.WATER).build());
    }

    @Override
    public List<ObtainableEntityType> getObtainableEntityTypes() {
        return this.obtainableEntityTypes;
    }

}
