package cech12.ceramicbucket.item;

import cech12.ceramicbucket.api.item.CeramicBucketItems;
import cech12.ceramicbucket.util.CeramicBucketUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

import javax.annotation.Nonnull;

public class FilledCeramicBucketFluidHandler extends FluidHandlerItemStack {

    protected final ItemStack emptyContainer = new ItemStack(CeramicBucketItems.CERAMIC_BUCKET);

    public FilledCeramicBucketFluidHandler(@Nonnull ItemStack container) {
        super(container, FluidAttributes.BUCKET_VOLUME);
    }

    @Override
    protected void setContainerToEmpty() {
        if (CeramicBucketUtils.isFluidTooHotForCeramicBucket(FluidUtil.getFluidContained(this.container).orElse(FluidStack.EMPTY).getFluid())) {
            //contains hot fluid (configurable temperature) bucket is removed
            this.container = ItemStack.EMPTY;
        } else {
            //else empty bucket
            this.container = emptyContainer;
        }
    }

}
