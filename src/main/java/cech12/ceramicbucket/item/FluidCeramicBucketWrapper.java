package cech12.ceramicbucket.item;

import cech12.ceramicbucket.api.item.CeramicBucketItems;
import cech12.ceramicbucket.config.Config;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;

import javax.annotation.Nonnull;

public class FluidCeramicBucketWrapper extends FluidBucketWrapper {

    public FluidCeramicBucketWrapper(@Nonnull ItemStack container) {
        super(container);
    }

    @Override
    protected void setFluid(@Nonnull FluidStack fluidStack) {
        if (fluidStack.isEmpty()) {
            int minBreakTemperature = Config.CERAMIC_BUCKET_BREAK_TEMPERATURE.getValue();
            if (minBreakTemperature >= 0 && FluidUtil.getFluidContained(this.container).orElse(FluidStack.EMPTY).getFluid().getAttributes().getTemperature() >= minBreakTemperature) {
                //contains hot fluid (configurable temperature, std. 1000) like lava (1300)? no empty bucket remains.
                this.container = ItemStack.EMPTY;
            } else {
                //else empty bucket
                this.container = new ItemStack(CeramicBucketItems.CERAMIC_BUCKET);
            }
        } else {
            Fluid fluid = fluidStack.getFluid();
            if (!fluidStack.hasTag() || fluidStack.getTag().isEmpty()) {
                if (fluid == Fluids.WATER) {
                    this.container =  new ItemStack(CeramicBucketItems.CERAMIC_WATER_BUCKET);
                    return;
                } else if (fluid == Fluids.LAVA) {
                    this.container =  new ItemStack(CeramicBucketItems.CERAMIC_LAVA_BUCKET);
                    return;
                } else if (fluid.getRegistryName() != null && fluid.getRegistryName().equals(new ResourceLocation("milk"))) {
                    this.container =  new ItemStack(CeramicBucketItems.CERAMIC_MILK_BUCKET);
                    return;
                }
            }
            //TODO other fluids?
            this.container = fluid.getAttributes().getBucket(fluidStack);
        }
    }
}
