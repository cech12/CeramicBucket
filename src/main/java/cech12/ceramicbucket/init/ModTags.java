package cech12.ceramicbucket.init;

import cech12.ceramicbucket.CeramicBucketMod;
import net.minecraft.fluid.Fluid;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ITag;

import javax.annotation.Nonnull;

public class ModTags {

    public static class Fluids {

        public static final ITag.INamedTag<Fluid> CERAMIC_CRACKING = tag("ceramic_cracking");
        public static final ITag.INamedTag<Fluid> INFINITY_ENCHANTABLE = tag("infinity_enchantable");

        private static ITag.INamedTag<Fluid> tag(@Nonnull String name) {
            return FluidTags.bind(CeramicBucketMod.MOD_ID + ":" + name);
        }

    }

}
