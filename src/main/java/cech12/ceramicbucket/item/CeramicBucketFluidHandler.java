package cech12.ceramicbucket.item;

import cech12.ceramicbucket.api.item.CeramicBucketItems;
import cech12.ceramicbucket.util.CeramicBucketUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

import javax.annotation.Nonnull;

public class CeramicBucketFluidHandler extends FluidHandlerItemStack {

    protected final ItemStack filledContainer = new ItemStack(CeramicBucketItems.FILLED_CERAMIC_BUCKET);
    protected final ItemStack filledMilkContainer = new ItemStack(CeramicBucketItems.CERAMIC_MILK_BUCKET);

    public CeramicBucketFluidHandler(@Nonnull ItemStack container) {
        super(container, FluidAttributes.BUCKET_VOLUME);
    }

    @Override
    protected void setFluid(FluidStack fluid) {
        if (CeramicBucketUtils.isMilkFluid(fluid.getFluid())) {
            this.container = this.filledMilkContainer;
        } else {
            this.container = this.filledContainer;
        }
        super.setFluid(fluid);
    }

}
