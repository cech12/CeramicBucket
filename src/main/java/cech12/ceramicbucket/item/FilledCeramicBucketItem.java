package cech12.ceramicbucket.item;

import cech12.ceramicbucket.api.item.CeramicBucketItems;

import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class FilledCeramicBucketItem extends AbstractCeramicBucketItem {

    public FilledCeramicBucketItem(Properties builder) {
        super(Fluids.EMPTY.delegate, builder);
    }

    @NotNull
    @Override
    FluidHandlerItemStack getNewFluidHandlerInstance(@Nonnull ItemStack stack) {
        return new FilledCeramicBucketFluidHandler(stack, new ItemStack(CeramicBucketItems.CERAMIC_BUCKET));
    }
}
