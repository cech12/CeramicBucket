package cech12.ceramicbucket.api.data;

import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class ObtainableEntityType {

    private final EntityType<?> entityType;
    private final List<Fluid> fluids;
    private final List<ITag<Fluid>> fluidTags;
    private final Boolean cracksBucket;
    private final ResourceLocation emptySound;

    public ObtainableEntityType(@Nonnull EntityType<?> entityType, List<Fluid> fluids, List<ITag<Fluid>> fluidTags, Boolean cracksBucket, ResourceLocation emptySound) {
        this.entityType = entityType;
        this.fluids = fluids;
        this.fluidTags = fluidTags;
        this.cracksBucket = cracksBucket;
        this.emptySound = emptySound;
    }

    public EntityType<?> getEntityType() {
        return this.entityType;
    }

    public Fluid getOneFluid() {
        //fluid tags cannot be accessed in this phase!
        //at least one fluid must be set
        if (!this.fluids.isEmpty()) {
            return this.fluids.get(0);
        }
        return Fluids.EMPTY;
    }

    public boolean isCorrectFluid(Fluid fluid) {
        for (Fluid f : this.fluids) {
            if (f == fluid) {
                return true;
            }
        }
        for (ITag<Fluid> tag : this.fluidTags) {
            if (fluid.isIn(tag)) {
                return true;
            }
        }
        return false;
    }

    public Boolean cracksBucket() {
        return this.cracksBucket;
    }

    public SoundEvent getEmptySound() {
        return ForgeRegistries.SOUND_EVENTS.getValue(this.emptySound);
    }

    public static class Builder {

        private final EntityType<?> entityType;
        private final List<Fluid> fluids = new ArrayList<>();
        private final List<ITag<Fluid>> fluidTags = new ArrayList<>();
        private Boolean cracksBucket = null;
        private ResourceLocation emptySound = SoundEvents.ITEM_BUCKET_EMPTY_FISH.getName();

        public Builder(EntityType<?> entityType, Fluid fluid) {
            this.entityType = entityType;
            this.fluids.add(fluid);
        }

        public Builder addFluid(@Nonnull Fluid fluid) {
            this.fluids.add(fluid);
            return this;
        }

        public Builder addFluidTag(@Nonnull ITag<Fluid> fluidTag) {
            this.fluidTags.add(fluidTag);
            return this;
        }

        public Builder setCracksBucket(Boolean cracksBucket) {
            this.cracksBucket = cracksBucket;
            return this;
        }

        public Builder setEmptySound(@Nonnull ResourceLocation sound) {
            this.emptySound = sound;
            return this;
        }

        public ObtainableEntityType build() {
            return new ObtainableEntityType(this.entityType, this.fluids, this.fluidTags, cracksBucket, this.emptySound);
        }

    }

}
