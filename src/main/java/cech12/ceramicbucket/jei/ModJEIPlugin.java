package cech12.ceramicbucket.jei;

import cech12.ceramicbucket.CeramicBucketMod;
import cech12.ceramicbucket.api.item.CeramicBucketItems;
import cech12.ceramicbucket.config.ServerConfig;
import cech12.ceramicbucket.init.ModTags;
import cech12.ceramicbucket.item.FilledCeramicBucketItem;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@JeiPlugin
public class ModJEIPlugin implements IModPlugin {

    private static final ResourceLocation ID = new ResourceLocation(CeramicBucketMod.MOD_ID, "jei_plugin");

    @Nonnull
    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registration.useNbtForSubtypes(CeramicBucketItems.FILLED_CERAMIC_BUCKET);
        registration.useNbtForSubtypes(CeramicBucketItems.CERAMIC_ENTITY_BUCKET);
    }

    @Override
    public void registerRecipes(@Nonnull IRecipeRegistration registration) {
        if (ServerConfig.INFINITY_ENCHANTMENT_ENABLED.get()) {
            IVanillaRecipeFactory factory = registration.getVanillaRecipeFactory();
            EnchantmentData data = new EnchantmentData(Enchantments.INFINITY, Enchantments.INFINITY.getMaxLevel());
            List<Object> recipes = new ArrayList<>();
            List<Fluid> addedFluids = new ArrayList<>();
            for (Fluid fluid : ForgeRegistries.FLUIDS) {
                FluidUtil.getFluidContained(new ItemStack(fluid.getFilledBucket())).ifPresent(bucketFluidStack -> {
                    Fluid bucketFluid = bucketFluidStack.getFluid();
                    if (!addedFluids.contains(bucketFluid) && bucketFluid.isIn(ModTags.Fluids.INFINITY_ENCHANTABLE)) {
                        addedFluids.add(bucketFluid);
                        ItemStack bucket = ((FilledCeramicBucketItem) CeramicBucketItems.FILLED_CERAMIC_BUCKET).getFilledInstance(bucketFluid.getFluid());
                        ItemStack enchantedBucket = bucket.copy();
                        enchantedBucket.addEnchantment(data.enchantment, data.enchantmentLevel);
                        recipes.add(factory.createAnvilRecipe(bucket,
                                Collections.singletonList(EnchantedBookItem.getEnchantedItemStack(data)),
                                Collections.singletonList(enchantedBucket)));
                    }
                });
            }
            if (!recipes.isEmpty()) {
                registration.addRecipes(recipes, VanillaRecipeCategoryUid.ANVIL);
            }
        }
    }

}
