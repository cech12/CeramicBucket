package cech12.ceramicbucket.item;

import cech12.ceramicbucket.config.ServerConfig;
import cech12.ceramicbucket.util.CeramicBucketUtils;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
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
        //consider infinity enchantment
        if (action == FluidAction.EXECUTE && ServerConfig.INFINITY_ENCHANTMENT_ENABLED.get()) {
            Fluid fluid = this.getFluid().getFluid();
            if (EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, this.getContainer()) > 0
                    && fluid instanceof FlowingFluid
                    && CeramicBucketUtils.canFluidSourcesMultiply((FlowingFluid) fluid)) {
                //simulate drain to simulate infinity effect
                return super.drain(maxDrain, FluidAction.SIMULATE);
            }
        }
        return super.drain(maxDrain, action);
    }
}
