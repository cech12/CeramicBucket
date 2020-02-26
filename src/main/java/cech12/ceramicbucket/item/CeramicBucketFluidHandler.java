package cech12.ceramicbucket.item;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

import javax.annotation.Nonnull;

public class CeramicBucketFluidHandler extends FluidHandlerItemStack {

    protected final ItemStack filledContainer;

    public CeramicBucketFluidHandler(@Nonnull ItemStack container, @Nonnull ItemStack filledContainer) {
        super(container, FluidAttributes.BUCKET_VOLUME);
        this.filledContainer = filledContainer;
    }

    @Override
    protected void setFluid(FluidStack fluid) {
        this.container = this.filledContainer;
        super.setFluid(fluid);
    }

}
