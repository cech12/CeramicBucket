package cech12.ceramicbucket.item;

import cech12.ceramicbucket.util.CeramicBucketUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class FilledCeramicBucketFluidHandler extends FluidHandlerItemStack {

    private final LazyOptional<IFluidHandlerItem> holder = LazyOptional.of(() -> this);

    public FilledCeramicBucketFluidHandler(@Nonnull ItemStack container) {
        super(container, FluidAttributes.BUCKET_VOLUME);
    }

    @Override
    protected void setContainerToEmpty() {
        this.container = this.container.getContainerItem();
    }

    @Nonnull
    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        //only drain the bucket, if there is enough space to drain the bucket completely
        if (maxDrain < FluidAttributes.BUCKET_VOLUME) {
            return FluidStack.EMPTY;
        }
        //consider infinity enchantment
        if (action == FluidAction.EXECUTE
                && CeramicBucketUtils.isAffectedByInfinityEnchantment(this.container)) {
            //simulate drain to simulate infinity effect
            return super.drain(maxDrain, FluidAction.SIMULATE);
        }
        return super.drain(maxDrain, action);
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
