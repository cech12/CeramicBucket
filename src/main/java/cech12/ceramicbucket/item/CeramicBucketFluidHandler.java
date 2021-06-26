package cech12.ceramicbucket.item;

import cech12.ceramicbucket.util.CeramicBucketUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class CeramicBucketFluidHandler extends FluidHandlerItemStack {

    private final LazyOptional<IFluidHandlerItem> holder = LazyOptional.of(() -> this);

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

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY) {
            return (LazyOptional<T>) holder;
        }
        return LazyOptional.empty();
    }
}
