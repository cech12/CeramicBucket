package cech12.ceramicbucket.item;

import cech12.ceramicbucket.api.item.CeramicBucketItems;
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
        ItemStack newContainer;
        if (CeramicBucketUtils.isMilkFluid(fluid.getFluid())) {
            newContainer = new ItemStack(CeramicBucketItems.CERAMIC_MILK_BUCKET);
        } else {
            newContainer = new ItemStack(CeramicBucketItems.FILLED_CERAMIC_BUCKET);
        }
        this.container = CeramicBucketUtils.copyBucketColor(this.container, newContainer);
        super.setFluid(fluid);
    }

}
