package cech12.ceramicbucket.item;

import cech12.ceramicbucket.api.item.CeramicBucketItems;

import cech12.ceramicbucket.config.ServerConfig;
import cech12.ceramicbucket.init.ModTags;
import cech12.ceramicbucket.util.CeramicBucketUtils;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.NonNullList;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;

public class FilledCeramicBucketItem extends AbstractCeramicBucketItem {

    public FilledCeramicBucketItem(Properties builder) {
        super(Fluids.EMPTY.delegate, builder);
    }

    @Nonnull
    @Override
    public ICapabilityProvider initCapabilities(@Nonnull ItemStack stack, @Nullable CompoundTag nbt) {
        return new FilledCeramicBucketFluidHandler(stack);
    }

    public ItemStack getFilledInstance(@Nonnull Fluid fluid, @Nullable ItemStack oldStack) {
        ItemStack stack = new ItemStack(this);
        if (oldStack != null) {
            copyNBTWithoutBucketContent(oldStack, stack);
        }
        return fill(stack, new FluidStack(fluid, FluidAttributes.BUCKET_VOLUME));
    }

    @Nonnull
    @Override
    @OnlyIn(Dist.CLIENT)
    public ItemStack getDefaultInstance() {
        return this.getFilledInstance(Fluids.WATER, null);
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    @Override
    public void fillItemCategory(@Nonnull CreativeModeTab group, @Nonnull NonNullList<ItemStack> items) {
        if (this.allowdedIn(group)) {
            ArrayList<Fluid> addedFluids = new ArrayList<>();
            for (Fluid fluid : ForgeRegistries.FLUIDS) {
                //only add non milk source fluids with a bucket item
                Item bucket = fluid.getBucket();
                if (bucket instanceof BucketItem && !CeramicBucketUtils.isMilkFluid(fluid, false)) {
                    Fluid bucketFluid = ((BucketItem) bucket).getFluid();
                    if (!addedFluids.contains(bucketFluid)) {
                        items.add(getFilledInstance(bucketFluid, null));
                        addedFluids.add(bucketFluid);
                    }
                }
            }
        }
    }

    @Override
    @Nonnull
    public String getDescriptionId() {
        return Util.makeDescriptionId("item", CeramicBucketItems.CERAMIC_BUCKET.getRegistryName());
    }

    @Override
    @Nonnull
    public Component getName(@Nonnull ItemStack stack) {
        if (getFluid(stack) == Fluids.EMPTY) {
            return new TranslatableComponent("item.ceramicbucket.ceramic_bucket");
        } else {
            Component fluidText = new TranslatableComponent(getFluid(stack).getAttributes().getTranslationKey());
            return new TranslatableComponent("item.ceramicbucket.filled_ceramic_bucket", fluidText);
        }
    }

    @Override
    public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
        //get burn time of normal bucket
        int burnTime = CeramicBucketUtils.getBurnTimeOfFluid(this.getFluid(itemStack), recipeType);
        if (burnTime >= 0) {
            return burnTime;
        }
        return super.getBurnTime(itemStack, recipeType);
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        //for using a filled bucket as fuel or in crafting recipes, an empty bucket should remain
        return !this.isCrackedBucket(stack);
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        //for using a filled bucket as fuel or in crafting recipes, an empty bucket should remain
        if (this.hasContainerItem(itemStack)) {
            if (CeramicBucketUtils.isAffectedByInfinityEnchantment(itemStack)) {
                //with infinity enchantment the filled bucket remains
                return itemStack.copy();
            }
            return copyNBTWithoutBucketContent(itemStack, new ItemStack(CeramicBucketItems.CERAMIC_BUCKET));
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if (enchantment == Enchantments.INFINITY_ARROWS
                && ServerConfig.INFINITY_ENCHANTMENT_ENABLED.get()
                && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, stack) <= 0
                && this.getFluid(stack).is(ModTags.Fluids.INFINITY_ENCHANTABLE)) {
            return true;
        }
        return super.canApplyAtEnchantingTable(stack, enchantment);
    }

}
