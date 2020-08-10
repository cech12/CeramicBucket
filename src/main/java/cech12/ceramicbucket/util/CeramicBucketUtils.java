package cech12.ceramicbucket.util;

import cech12.ceramicbucket.api.item.CeramicBucketItems;
import cech12.ceramicbucket.item.CeramicMilkBucketItem;
import cech12.ceramicbucket.item.FilledCeramicBucketItem;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class CeramicBucketUtils {

    //TODO milk tag
    //private static final ITag.INamedTag<Fluid> MILK_TAG = FluidTags.makeWrapperTag("forge:milk");
    private static final List<ResourceLocation> MILK_FLUIDS = new ArrayList<>();
    static {
        MILK_FLUIDS.add(new ResourceLocation("milk")); //like in FluidUtil.getFilledBucket(...)
        MILK_FLUIDS.add(new ResourceLocation("industrialforegoing:milk")); //milk of IndustrialForegoing has not "forge:milk" tag
    }

    /**
     * Checks if a given fluid is a milk fluid.
     */
    public static boolean isMilkFluid(@Nonnull Fluid fluid) {
        //TODO milk tag
        //if (fluid.isIn(MILK_TAG)) {
        //    return true;
        //}
        for (ResourceLocation name : MILK_FLUIDS) {
            if (name.equals(fluid.getRegistryName())) {
                return true;
            }
        }
        return false;
    }

    public static ItemStack getFilledCeramicBucket(Fluid fluid) {
        if (CeramicBucketUtils.isMilkFluid(fluid)) {
            return ((CeramicMilkBucketItem) CeramicBucketItems.CERAMIC_MILK_BUCKET).getFilledInstance(fluid);
        }
        return ((FilledCeramicBucketItem) CeramicBucketItems.FILLED_CERAMIC_BUCKET).getFilledInstance(fluid);
    }

}
