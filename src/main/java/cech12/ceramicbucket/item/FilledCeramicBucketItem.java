package cech12.ceramicbucket.item;

import cech12.ceramicbucket.api.item.CeramicBucketItems;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;

public class FilledCeramicBucketItem extends AbstractCeramicBucketItem implements IItemColor {

    public FilledCeramicBucketItem(Properties builder) {
        super(Fluids.EMPTY.delegate, builder);
    }

    @Nonnull
    @Override
    FluidHandlerItemStack getNewFluidHandlerInstance(@Nonnull ItemStack stack) {
        return new FilledCeramicBucketFluidHandler(stack, new ItemStack(CeramicBucketItems.CERAMIC_BUCKET));
    }

    public ItemStack getFilledInstance(@Nonnull Fluid fluid) {
        return fill(new ItemStack(this), new FluidStack(fluid, FluidAttributes.BUCKET_VOLUME));
    }

    @Nonnull
    @OnlyIn(Dist.CLIENT)
    public ItemStack getDefaultInstance() {
        return this.getFilledInstance(Fluids.WATER);
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    @Override
    public void fillItemGroup(@Nonnull ItemGroup group, @Nonnull NonNullList<ItemStack> items) {
        if (this.isInGroup(group)) {
            for(Fluid fluid : ForgeRegistries.FLUIDS) {
                if (fluid.getDefaultState().isSource()) {
                    items.add(getFilledInstance(fluid));
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
        if (getFluid(stack) == Fluids.EMPTY)
            return super.getDisplayName(stack);
        return super.getDisplayName(stack).appendSibling(new StringTextComponent(" (").appendSibling(getFluid(stack).getDefaultState().getBlockState().getBlock().getNameTextComponent()).appendSibling(new StringTextComponent(")")));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public int getColor(@Nonnull ItemStack stack, int layer) {
        //color layer1
        if (layer > 0 && stack.getItem() instanceof FilledCeramicBucketItem) {
            Fluid fluid = ((FilledCeramicBucketItem) stack.getItem()).getFluid(stack).getFluid();
            if (fluid == Fluids.LAVA) { //lava has multiple colors
                switch (layer) {
                    case 1: return 14996060; //yellow
                    case 2: return 16751943; //orange
                    case 3: return 16733234; //red
                }
            }
            return fluid.getAttributes().getColor();
        }
        return -1;
    }
}
