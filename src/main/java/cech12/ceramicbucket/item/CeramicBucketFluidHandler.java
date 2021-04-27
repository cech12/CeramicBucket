package cech12.ceramicbucket.item;

import cech12.ceramicbucket.util.CeramicBucketUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

import javax.annotation.Nonnull;

public class CeramicBucketFluidHandler extends FluidHandlerItemStack {

    public CeramicBucketFluidHandler(@Nonnull ItemStack container) {
        super(container, FluidAttributes.BUCKET_VOLUME);
    }

    @Override
    protected void setFluid(FluidStack fluid) {
        this.container = CeramicBucketUtils.getFilledCeramicBucket(fluid.getFluid(), this.container);
    }

    @Override
    public int fill(FluidStack resource, FluidAction doFill) {
        //only fill the bucket, if there is enough fluid to fill the bucket completely
        if (resource.getAmount() < FluidAttributes.BUCKET_VOLUME) {
            return 0;
        }
        return super.fill(resource, doFill);
    }
}
