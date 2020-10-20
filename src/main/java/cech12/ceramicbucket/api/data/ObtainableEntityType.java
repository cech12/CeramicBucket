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
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ObtainableEntityType {

    private final ResourceLocation entityType;
    private final List<Fluid> fluids;
    private final List<ITag<Fluid>> fluidTags;
    private final Boolean cracksBucket;
    private final ResourceLocation emptySound;
    private final ResourceLocation fillSound;

    public ObtainableEntityType(@Nonnull ResourceLocation entityType, List<Fluid> fluids, List<ITag<Fluid>> fluidTags, Boolean cracksBucket, ResourceLocation emptySound, ResourceLocation fillSound) {
        this.entityType = entityType;
        this.fluids = fluids;
        this.fluidTags = fluidTags;
        this.cracksBucket = cracksBucket;
        this.emptySound = emptySound;
        this.fillSound = fillSound;
    }

    @Nullable
    public EntityType<?> getEntityType() {
        if (ForgeRegistries.ENTITIES.containsKey(this.entityType)) {
            return ForgeRegistries.ENTITIES.getValue(this.entityType);
        }
        return null;
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

    public SoundEvent getFillSound() {
        return ForgeRegistries.SOUND_EVENTS.getValue(this.fillSound);
    }

    public static class Builder {

        private final ResourceLocation entityType;
        private final List<Fluid> fluids = new ArrayList<>();
        private final List<ITag<Fluid>> fluidTags = new ArrayList<>();
        private Boolean cracksBucket = null;
        private ResourceLocation emptySound = SoundEvents.ITEM_BUCKET_EMPTY_FISH.getName();
        private ResourceLocation fillSound = SoundEvents.ITEM_BUCKET_FILL_FISH.getName();

        public Builder(@Nonnull EntityType<?> entityType, Fluid fluid) {
            this.entityType = ForgeRegistries.ENTITIES.getKey(entityType);
            this.fluids.add(fluid);
        }

        public Builder(@Nonnull ResourceLocation entityType, Fluid fluid) {
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

        public Builder setFillSound(@Nonnull ResourceLocation sound) {
            this.fillSound = sound;
            return this;
        }

        public ObtainableEntityType build() {
            return new ObtainableEntityType(this.entityType, this.fluids, this.fluidTags, cracksBucket, this.emptySound, this.fillSound);
        }

    }

}
