package cech12.ceramicbucket.init;

import cech12.ceramicbucket.CeramicBucketMod;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.Tag;

import javax.annotation.Nonnull;

public class ModTags {

    public static class Fluids {

        public static final Tag.Named<Fluid> CERAMIC_CRACKING = tag("ceramic_cracking");
        public static final Tag.Named<Fluid> INFINITY_ENCHANTABLE = tag("infinity_enchantable");

        private static Tag.Named<Fluid> tag(@Nonnull String name) {
            return FluidTags.bind(CeramicBucketMod.MOD_ID + ":" + name);
        }

    }

}
