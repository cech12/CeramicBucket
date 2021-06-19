package cech12.ceramicbucket.compat;

import cech12.ceramicbucket.api.data.ObtainableEntityType;
import net.minecraft.fluid.Fluids;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class FinsAndTrailsCompat extends ModCompat.Mod implements ModCompat.EntityTypeObtainingMod {

    private final List<ObtainableEntityType> obtainableEntityTypes = new ArrayList<>();

    public FinsAndTrailsCompat() {
        super("fins");
        this.addFish("banded_redback_shrimp");
        this.addFish("blu_wee");
        this.addFish("flatback_sucker");
        this.addFish("golden_river_ray");
        this.addFish("gopjet");
        this.addFish("high_finned_blue");
        this.addFish("night_light_squid");
        this.addFish("ornate_bugfish");
        this.addFish("papa_wee");
        this.addFish("pea_wee");

        ObtainableEntityType.Builder builder = new ObtainableEntityType.Builder(new ResourceLocation(this.name, "penglil"), Fluids.EMPTY);
        ResourceLocation fillSound = ForgeRegistries.SOUND_EVENTS.getKey(SoundEvents.BUCKET_FILL);
        if (fillSound != null) builder.setFillSound(fillSound);
        ResourceLocation emptySound = new ResourceLocation(this.name, "fins.penglil.ambient");
        SoundEvent sound = ForgeRegistries.SOUND_EVENTS.getValue(emptySound);
        if (sound != null) builder.setEmptySound(emptySound);
        this.obtainableEntityTypes.add(builder.build());

        this.addFish("phantom_nudibranch");
        this.addFish("red_bull_crab");
        this.addFish("spindly_gem_crab");
        this.addFish("swamp_mucker");
        this.addFish("teal_arrowfish");
        this.addFish("vibra_wee");
        this.addFish("wee_wee");
        this.addFish("white_bull_crab");
    }

    private void addFish(String fish) {
        this.obtainableEntityTypes.add(new ObtainableEntityType.Builder(new ResourceLocation(this.name, fish), Fluids.WATER).addFluidTag(FluidTags.WATER).build());
    }

    @Override
    public List<ObtainableEntityType> getObtainableEntityTypes() {
        return this.obtainableEntityTypes;
    }

}
