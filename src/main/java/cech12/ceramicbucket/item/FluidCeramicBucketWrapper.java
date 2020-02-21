package cech12.ceramicbucket.item;

import cech12.ceramicbucket.api.item.CeramicBucketItems;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;

import javax.annotation.Nonnull;

public class FluidCeramicBucketWrapper extends FluidBucketWrapper {

    public FluidCeramicBucketWrapper(@Nonnull ItemStack container) {
        super(container);
    }

    @Override
    protected void setFluid(@Nonnull FluidStack fluidStack) {
        if (fluidStack.isEmpty()) {
            if (this.container.getItem() == CeramicBucketItems.CERAMIC_LAVA_BUCKET) {
                //lava in bucket and it is set to empty? no bucket remains
                //TODO other hot fluids?
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
