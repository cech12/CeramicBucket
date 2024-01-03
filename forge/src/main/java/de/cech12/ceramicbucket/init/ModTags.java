package de.cech12.ceramicbucket.init;

import de.cech12.ceramicbucket.CeramicBucketMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;

public class ModTags {

    public static class Fluids {

        public static final TagKey<Fluid> CERAMIC_CRACKING = tag("ceramic_cracking");

        private static TagKey<Fluid> tag(@Nonnull String name) {
            return TagKey.create(ForgeRegistries.FLUIDS.getRegistryKey(), new ResourceLocation(CeramicBucketMod.MOD_ID, name));
        }

    }

}
