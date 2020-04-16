package cech12.ceramicbucket.item;

import cech12.ceramicbucket.api.item.CeramicBucketItems;
import cech12.ceramicbucket.config.Config;
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
