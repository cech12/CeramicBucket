package cech12.ceramicbucket.util;

import cech12.ceramicbucket.api.item.CeramicBucketItems;
import cech12.ceramicbucket.config.ServerConfig;
import cech12.ceramicbucket.init.ModTags;
import cech12.ceramicbucket.item.AbstractCeramicBucketItem;
import cech12.ceramicbucket.item.CeramicMilkBucketItem;
import cech12.ceramicbucket.item.FilledCeramicBucketItem;
import net.minecraft.advancements.Advancement;
import net.minecraft.server.ServerAdvancementManager;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CeramicBucketUtils {

    /**
     * Checks if a given fluid is a milk fluid.
     * You can decide to check the forge:milk tag or not.
     */
    public static boolean isMilkFluid(@Nonnull Fluid fluid, boolean checkTag) {
        if (checkTag && fluid.is(Tags.Fluids.MILK)) {
            return true;
        }
        ResourceLocation location = fluid.getBucket().getRegistryName();
        return location != null && location.equals(Items.MILK_BUCKET.getRegistryName());
    }

    /**
     * Checks if a given fluid is a milk fluid.
     * It also checks the forge:milk tag.
     */
    public static boolean isMilkFluid(@Nonnull Fluid fluid) {
        return isMilkFluid(fluid, true);
    }

    /**
     * Checks if a given fluid is too hot for a ceramic bucket.
     * "too hot" temperature is configurable (std. 1000) (lava: 1300)
     * @param fluid fluid that should be checked
     * @return true if the temperature of the fluid is more than the break temperature, else false
     */
    public static boolean isFluidTooHotForCeramicBucket(@Nonnull Fluid fluid) {
        if (fluid == Fluids.EMPTY) {
            return false;
        }
        int minBreakTemperature = ServerConfig.CERAMIC_BUCKET_BREAK_TEMPERATURE.get();
        return (minBreakTemperature >= 0 && fluid.getAttributes().getTemperature() >= minBreakTemperature)
                || (fluid.is(ModTags.Fluids.CERAMIC_CRACKING));
    }

    public static ItemStack getFilledCeramicBucket(Fluid fluid, ItemStack emptyBucket) {
        if (CeramicBucketUtils.isMilkFluid(fluid)) {
            return ((CeramicMilkBucketItem) CeramicBucketItems.CERAMIC_MILK_BUCKET).getFilledInstance(fluid, emptyBucket);
        } else {
            return ((FilledCeramicBucketItem) CeramicBucketItems.FILLED_CERAMIC_BUCKET).getFilledInstance(fluid, emptyBucket);
        }
    }

    /**
     * Fills the given emptyBucket with the content of the given filledItemStack.
     * If the content cannot be filled into the emptyBucket, null is returned.
     *
     * @param filledItemStack filled item stack
     * @param emptyBucket empty ceramic bucket that should be filled with the content of filledItemStack.
     * @return filled ceramic bucket or null if it cannot be filled.
     */
    public static ItemStack getFilledCeramicBucket(ItemStack filledItemStack, ItemStack emptyBucket) {
        if (filledItemStack.getItem() == Items.POWDER_SNOW_BUCKET) {
            //TODO
        } else {
            FluidStack fluidStack = FluidUtil.getFluidContained(filledItemStack).orElse(null);
            if (fluidStack != null && fluidStack.getFluid() != Fluids.EMPTY) {
                if (CeramicBucketUtils.isMilkFluid(fluidStack.getFluid())) {
                    return ((CeramicMilkBucketItem) CeramicBucketItems.CERAMIC_MILK_BUCKET).getFilledInstance(fluidStack.getFluid(), emptyBucket);
                } else {
                    return ((FilledCeramicBucketItem) CeramicBucketItems.FILLED_CERAMIC_BUCKET).getFilledInstance(fluidStack.getFluid(), emptyBucket);
                }
            }
        }
        return null;
    }

    /**
     * Get the burn time of the bucket item of the given fluid.
     * @param fluid fluid that should be checked.
     * @param recipeType recipeType
     * @return burn time of the bucket item of the given fluid; -1 for Fluids.EMPTY
     */
    public static int getBurnTimeOfFluid(@Nonnull Fluid fluid, @Nullable RecipeType<?> recipeType) {
        if (fluid != Fluids.EMPTY) {
            //all fluids have their burn time in their bucket item.
            //get the burn time via ForgeHooks.getBurnTime to let other mods change burn times of buckets of vanilla and other fluids.
            return ForgeHooks.getBurnTime(new ItemStack(fluid.getBucket()), recipeType);
        }
        return -1;
    }

    /**
     * Checks if the given bucket is affected by a infinity enchantment.
     * @param bucket checked item stack
     * @return boolean
     */
    public static boolean isAffectedByInfinityEnchantment(@Nonnull ItemStack bucket) {
        return ServerConfig.INFINITY_ENCHANTMENT_ENABLED.get()
                && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, bucket) > 0
                && bucket.getItem() instanceof AbstractCeramicBucketItem
                && ((AbstractCeramicBucketItem) bucket.getItem()).getFluid(bucket).is(ModTags.Fluids.INFINITY_ENCHANTABLE);
    }

    /**
     * Grants the given advancement for the given player.
     * @param player Player
     * @param advancementLocation ResourceLocation of the advancement
     */
    public static void grantAdvancement(ServerPlayer player, ResourceLocation advancementLocation) {
        if (player!= null && advancementLocation != null && player.getServer() != null) {
            ServerAdvancementManager am = player.getServer().getAdvancements();
            Advancement advancement = am.getAdvancement(advancementLocation);
            if (advancement != null) {
                AdvancementProgress advancementprogress = player.getAdvancements().getOrStartProgress(advancement);
                if (!advancementprogress.isDone()) {
                    for (String s : advancementprogress.getRemainingCriteria()) {
                        player.getAdvancements().award(advancement, s);
                    }
                }
            }
        }
    }

}
