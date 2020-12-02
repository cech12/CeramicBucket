package cech12.ceramicbucket.init;

import cech12.ceramicbucket.CeramicBucketMod;
import net.minecraft.fluid.Fluid;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class ModTags {

    public static class Fluids {

        public static final Tag<Fluid> CERAMIC_CRACKING = tag("ceramic_cracking");
        public static final Tag<Fluid> INFINITY_ENCHANTABLE = tag("infinity_enchantable");

        private static Tag<Fluid> tag(@Nonnull String name) {
            return new FluidTags.Wrapper(new ResourceLocation(CeramicBucketMod.MOD_ID, name));
        }

    }

}
