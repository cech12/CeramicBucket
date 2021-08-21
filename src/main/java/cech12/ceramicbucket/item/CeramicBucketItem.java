package cech12.ceramicbucket.item;

import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CeramicBucketItem extends AbstractCeramicBucketItem {

    public CeramicBucketItem(Item.Properties builder) {
        super(Fluids.EMPTY.delegate, builder);
    }

    @Nonnull
    @Override
    public ICapabilityProvider initCapabilities(@Nonnull ItemStack stack, @Nullable CompoundTag nbt) {
        return new CeramicBucketFluidHandler(stack);
    }

}
