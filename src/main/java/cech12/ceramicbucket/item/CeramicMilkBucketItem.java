package cech12.ceramicbucket.item;

import cech12.ceramicbucket.api.item.CeramicBucketItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MilkBucketItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CeramicMilkBucketItem extends MilkBucketItem {

    public CeramicMilkBucketItem(Properties builder) {
        super(builder);
    }

    /**
     * Replace vanilla bucket with ceramic bucket after drinking.
     */
    @Override
    public @Nonnull ItemStack onItemUseFinish(ItemStack stack, World worldIn, @Nonnull LivingEntity entityLiving) {
        ItemStack bucket = super.onItemUseFinish(stack, worldIn, entityLiving);
        return bucket.getItem().equals(Items.BUCKET) ? new ItemStack(CeramicBucketItems.CERAMIC_BUCKET) : bucket;
    }

    @Override
    public ICapabilityProvider initCapabilities(@Nonnull ItemStack stack, @Nullable CompoundNBT nbt) {
        return null; //new FilledCeramicBucketFluidHandler(stack);
    }
}
