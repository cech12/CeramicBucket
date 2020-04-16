package cech12.ceramicbucket.item;

import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

import javax.annotation.Nonnull;

public class CeramicBucketItem extends AbstractCeramicBucketItem {

    public CeramicBucketItem(Item.Properties builder) {
        super(Fluids.EMPTY.delegate, builder);
    }

    @Nonnull
    @Override
    FluidHandlerItemStack getNewFluidHandlerInstance(@Nonnull ItemStack stack) {
        return new CeramicBucketFluidHandler(stack);
    }

}
