package cech12.ceramicbucket.item;

import cech12.ceramicbucket.api.item.CeramicBucketItems;

import cech12.ceramicbucket.config.ServerConfig;
import cech12.ceramicbucket.init.ModTags;
import cech12.ceramicbucket.util.CeramicBucketUtils;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
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
    FluidHandlerItemStack getNewFluidHandlerInstance(@Nonnull ItemStack stack) {
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
    public void fillItemGroup(@Nonnull ItemGroup group, @Nonnull NonNullList<ItemStack> items) {
        if (this.isInGroup(group)) {
            ArrayList<Fluid> addedFluids = new ArrayList<>();
            for (Fluid fluid : ForgeRegistries.FLUIDS) {
                //only add non milk source fluids with a bucket item
                Item bucket = fluid.getFilledBucket();
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
    public String getTranslationKey() {
        return Util.makeTranslationKey("item", CeramicBucketItems.CERAMIC_BUCKET.getRegistryName());
    }

    @Override
    @Nonnull
    public ITextComponent getDisplayName(@Nonnull ItemStack stack) {
        if (getFluid(stack) == Fluids.EMPTY) {
            return new TranslationTextComponent("item.ceramicbucket.ceramic_bucket");
        } else {
            ITextComponent fluidText = new TranslationTextComponent(getFluid(stack).getAttributes().getTranslationKey());
            return new TranslationTextComponent("item.ceramicbucket.filled_ceramic_bucket", fluidText);
        }
    }

    @Override
    public int getBurnTime(ItemStack itemStack) {
        //get burn time of normal bucket
        int burnTime = CeramicBucketUtils.getBurnTimeOfFluid(this.getFluid(itemStack));
        if (burnTime >= 0) {
            return burnTime;
        }
        return super.getBurnTime(itemStack);
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
        if (enchantment == Enchantments.INFINITY
                && ServerConfig.INFINITY_ENCHANTMENT_ENABLED.get()
                && EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) <= 0
                && this.getFluid(stack).isIn(ModTags.Fluids.INFINITY_ENCHANTABLE)) {
            return true;
        }
        return super.canApplyAtEnchantingTable(stack, enchantment);
    }

}
