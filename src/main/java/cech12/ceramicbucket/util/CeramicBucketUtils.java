package cech12.ceramicbucket.util;

import cech12.ceramicbucket.api.item.CeramicBucketItems;
import cech12.ceramicbucket.config.Config;
import cech12.ceramicbucket.item.CeramicMilkBucketItem;
import cech12.ceramicbucket.item.FilledCeramicBucketItem;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class CeramicBucketUtils {

    private static final Tag<Fluid> MILK_TAG = new FluidTags.Wrapper(new ResourceLocation("forge", "milk"));
    private static final List<ResourceLocation> MILK_FLUIDS = new ArrayList<>();
    static {
        MILK_FLUIDS.add(new ResourceLocation("milk")); //like in FluidUtil.getFilledBucket(...)
        MILK_FLUIDS.add(new ResourceLocation("industrialforegoing:milk")); //milk of IndustrialForegoing has not "forge:milk" tag
    }

    /**
     * Checks if a given fluid is a milk fluid.
     */
    public static boolean isMilkFluid(@Nonnull Fluid fluid) {
        if (fluid.isIn(MILK_TAG)) {
            return true;
        }
        ResourceLocation location = fluid.getFilledBucket().getRegistryName();
        if (location != null && location.equals(Items.MILK_BUCKET.getRegistryName())) {
            return true;
        }
        for (ResourceLocation name : MILK_FLUIDS) {
            if (name.equals(fluid.getRegistryName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a given fluid is too hot for a ceramic bucket.
     * "too hot" temperature is configurable (std. 1000) (lava: 1300)
     * @param fluid fluid that should be checked
     * @return true if the temperature of the fluid is more than the break temperature, else false
     */
    public static boolean isFluidTooHotForCeramicBucket(@Nonnull Fluid fluid) {
        int minBreakTemperature = Config.CERAMIC_BUCKET_BREAK_TEMPERATURE.getValue();
        return minBreakTemperature >= 0 && fluid.getAttributes().getTemperature() >= minBreakTemperature;
    }


    public static ItemStack getFilledCeramicBucket(Fluid fluid) {
        if (CeramicBucketUtils.isMilkFluid(fluid)) {
            return ((CeramicMilkBucketItem) CeramicBucketItems.CERAMIC_MILK_BUCKET).getFilledInstance(fluid);
        }
        return ((FilledCeramicBucketItem) CeramicBucketItems.FILLED_CERAMIC_BUCKET).getFilledInstance(fluid);
    }

}
