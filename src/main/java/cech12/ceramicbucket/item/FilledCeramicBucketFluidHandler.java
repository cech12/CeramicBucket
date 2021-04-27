package cech12.ceramicbucket.item;

import cech12.ceramicbucket.util.CeramicBucketUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

import javax.annotation.Nonnull;

public class FilledCeramicBucketFluidHandler extends FluidHandlerItemStack {

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
}
