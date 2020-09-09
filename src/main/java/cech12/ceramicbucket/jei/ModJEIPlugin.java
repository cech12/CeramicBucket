package cech12.ceramicbucket.jei;

import cech12.ceramicbucket.CeramicBucketMod;
import cech12.ceramicbucket.api.item.CeramicBucketItems;
import cech12.ceramicbucket.util.CeramicBucketUtils;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@JeiPlugin
public class ModJEIPlugin implements IModPlugin {

    private static ResourceLocation ID = new ResourceLocation(CeramicBucketMod.MOD_ID, "jei_plugin");

    @Nonnull
    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registration.useNbtForSubtypes(CeramicBucketItems.FILLED_CERAMIC_BUCKET);
    }

    @Override
    public void registerRecipes(@Nonnull IRecipeRegistration registration) {
        //add fuel recipes for all ceramic buckets that can be used as fuel
        ArrayList<Fluid> addedFluids = new ArrayList<>();
        List<ItemStack> fuelBuckets = new LinkedList<>();
        for (Fluid fluid : ForgeRegistries.FLUIDS) {
            Item bucket = fluid.getFilledBucket();
            if (bucket instanceof BucketItem && CeramicBucketUtils.getBurnTimeOfFluid(fluid) > 0) {
                Fluid bucketFluid = ((BucketItem) bucket).getFluid();
                if (!addedFluids.contains(bucketFluid)) {
                    fuelBuckets.add(CeramicBucketUtils.getFilledCeramicBucket(bucketFluid));
                    addedFluids.add(bucketFluid);
                }
            }
        }
        registration.addRecipes(fuelBuckets, VanillaRecipeCategoryUid.FUEL);
    }
}
