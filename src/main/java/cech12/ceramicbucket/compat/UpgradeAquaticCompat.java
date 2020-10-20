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
        this.addJellyfish("box_jellyfish");
        this.addJellyfish("cassiopea_jellyfish");
        this.addFish("glow_squid");
        this.addJellyfish("immortal_jellyfish");
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

    private void addJellyfish(String fish) {
        this.obtainableEntityTypes.add(this.getWaterFishBuilder(fish)
                .setEmptySound(new ResourceLocation(this.name, "item.bucket.empty_jellyfish"))
                .setFillSound(new ResourceLocation(this.name, "item.bucket.fill_jellyfish"))
                .build());
    }

    private void addSquid() {
        this.obtainableEntityTypes.add(new ObtainableEntityType.Builder(EntityType.SQUID, Fluids.WATER).addFluidTag(FluidTags.WATER).build());
    }

    @Override
    public List<ObtainableEntityType> getObtainableEntityTypes() {
        return this.obtainableEntityTypes;
    }

}
