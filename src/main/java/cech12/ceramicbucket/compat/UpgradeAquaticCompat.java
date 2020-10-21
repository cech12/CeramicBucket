package cech12.ceramicbucket.compat;

import cech12.ceramicbucket.api.data.ObtainableEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluids;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class UpgradeAquaticCompat extends ModCompat.Mod implements ModCompat.EntityTypeObtainingMod {

    List<ObtainableEntityType> obtainableEntityTypes = new ArrayList<>();

    public UpgradeAquaticCompat() {
        super("upgrade_aquatic");
        //this.addFish("box_jellyfish"); //added in 1.15
        //this.addFish("cassiopea_jellyfish"); //added in 1.15
        //this.addFish("glow_squid"); //added in 1.16
        //this.addFish("immortal_jellyfish"); //added in 1.15
        this.addFish("lionfish");
        this.addFish("nautilus");
        this.addFish("pike");
        this.addSquid();
    }

    private ObtainableEntityType.Builder getWaterFishBuilder(String fish) {
        return new ObtainableEntityType.Builder(new ResourceLocation(this.name, fish), Fluids.WATER).addFluidTag(FluidTags.WATER);
    }

    private void addFish(String fish) {
        this.obtainableEntityTypes.add(this.getWaterFishBuilder(fish).build());
    }

    private void addSquid() {
        this.obtainableEntityTypes.add(new ObtainableEntityType.Builder(EntityType.SQUID, Fluids.WATER).addFluidTag(FluidTags.WATER).build());
    }

    @Override
    public List<ObtainableEntityType> getObtainableEntityTypes() {
        return this.obtainableEntityTypes;
    }

}
