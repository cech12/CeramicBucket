package de.cech12.ceramicbucket.init;

import de.cech12.ceramicbucket.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;

public class ModTags {

    public static class Fluids {

        public static final TagKey<Fluid> CERAMIC_CRACKING = tag("ceramic_cracking");

        private static TagKey<Fluid> tag(@NotNull String name) {
            return TagKey.create(Registries.FLUID, Constants.id(name));
        }

    }

}
