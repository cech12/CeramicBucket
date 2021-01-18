package cech12.ceramicbucket.util;

import cech12.ceramicbucket.api.item.CeramicBucketItems;
import cech12.ceramicbucket.config.ServerConfig;
import cech12.ceramicbucket.init.ModTags;
import cech12.ceramicbucket.item.AbstractCeramicBucketItem;
import cech12.ceramicbucket.item.CeramicMilkBucketItem;
import cech12.ceramicbucket.item.FilledCeramicBucketItem;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeHooks;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class CeramicBucketUtils {

    private static final ResourceLocation FORGE_MILK_LOCATION = new ResourceLocation("forge", "milk");
    private static final ITag.INamedTag<Fluid> MILK_TAG;
    private static final List<ResourceLocation> MILK_FLUIDS = new ArrayList<>();
    static {
        ITag.INamedTag<Fluid> milkTag = null;
        for (ITag.INamedTag<Fluid> tag : FluidTags.getAllTags()) {
            if (tag.getName().equals(FORGE_MILK_LOCATION)) {
                milkTag = tag;
                break;
            }
        }
        MILK_TAG = (milkTag != null) ? milkTag : FluidTags.makeWrapperTag(FORGE_MILK_LOCATION.toString());

        MILK_FLUIDS.add(new ResourceLocation("milk")); //like in FluidUtil.getFilledBucket(...)
        //TODO remove this tag reference and the tag file in 1.17 update! Forge contains its own milk fluid since 36.0.1 (Industrial Forgoing is using this)
        MILK_FLUIDS.add(new ResourceLocation("industrialforegoing:milk")); //milk of IndustrialForegoing has not "forge:milk" tag
    }

    /**
     * Checks if a given fluid is a milk fluid.
     * You can decide to check the forge:milk tag or not.
     */
    public static boolean isMilkFluid(@Nonnull Fluid fluid, boolean checkTag) {
        if (checkTag && fluid.isIn(MILK_TAG)) {
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
                || (fluid.isIn(ModTags.Fluids.CERAMIC_CRACKING));
    }

    public static ItemStack getFilledCeramicBucket(Fluid fluid, ItemStack emptyBucket) {
        if (CeramicBucketUtils.isMilkFluid(fluid)) {
            return ((CeramicMilkBucketItem) CeramicBucketItems.CERAMIC_MILK_BUCKET).getFilledInstance(fluid, emptyBucket);
        } else {
            return ((FilledCeramicBucketItem) CeramicBucketItems.FILLED_CERAMIC_BUCKET).getFilledInstance(fluid, emptyBucket);
        }
    }

    /**
     * Get the burn time of the bucket item of the given fluid.
     * @param fluid fluid that should be checked.
     * @return burn time of the bucket item of the given fluid; -1 for Fluids.EMPTY
     */
    public static int getBurnTimeOfFluid(@Nonnull Fluid fluid) {
        if (fluid != Fluids.EMPTY) {
            //all fluids have their burn time in their bucket item.
            //get the burn time via ForgeHooks.getBurnTime to let other mods change burn times of buckets of vanilla and other fluids.
            return ForgeHooks.getBurnTime(new ItemStack(fluid.getFilledBucket()));
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
                && EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, bucket) > 0
                && bucket.getItem() instanceof AbstractCeramicBucketItem
                && ((AbstractCeramicBucketItem) bucket.getItem()).getFluid(bucket).isIn(ModTags.Fluids.INFINITY_ENCHANTABLE);
    }

    /**
     * Grants the given advancement for the given player.
     * @param player Player
     * @param advancementLocation ResourceLocation of the advancement
     */
    public static void grantAdvancement(ServerPlayerEntity player, ResourceLocation advancementLocation) {
        if (player!= null && advancementLocation != null && player.getServer() != null) {
            AdvancementManager am = player.getServer().getAdvancementManager();
            Advancement advancement = am.getAdvancement(advancementLocation);
            if (advancement != null) {
                AdvancementProgress advancementprogress = player.getAdvancements().getProgress(advancement);
                if (!advancementprogress.isDone()) {
                    for (String s : advancementprogress.getRemaningCriteria()) {
                        player.getAdvancements().grantCriterion(advancement, s);
                    }
                }
            }
        }
    }

}
