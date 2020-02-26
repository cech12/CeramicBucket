package cech12.ceramicbucket.item;

import cech12.ceramicbucket.config.Config;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

import javax.annotation.Nonnull;

public class FilledCeramicBucketFluidHandler extends FluidHandlerItemStack {

    protected final ItemStack emptyContainer;

    public FilledCeramicBucketFluidHandler(@Nonnull ItemStack container, @Nonnull ItemStack emptyContainer) {
        super(container, FluidAttributes.BUCKET_VOLUME);
        this.emptyContainer = emptyContainer;
    }

    @Override
    protected void setContainerToEmpty() {
        int minBreakTemperature = Config.CERAMIC_BUCKET_BREAK_TEMPERATURE.getValue();
        if (minBreakTemperature >= 0 && FluidUtil.getFluidContained(this.container).orElse(FluidStack.EMPTY).getFluid().getAttributes().getTemperature() >= minBreakTemperature) {
            //contains hot fluid (configurable temperature, std. 1000) like lava (1300)? no empty bucket remains.
            this.container = ItemStack.EMPTY;
        } else {
            //else empty bucket
            this.container = emptyContainer;
        }
    }

}
