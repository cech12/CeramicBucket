package cech12.ceramicbucket.item;

import cech12.ceramicbucket.api.item.CeramicBucketItems;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockState;
import net.minecraft.block.IBucketPickupHandler;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Supplier;

public class CeramicBucketItem extends BucketItem {

    public CeramicBucketItem(Supplier<? extends Fluid> supplier, Properties builder) {
        super(supplier, builder);
    }

    @Override
    protected @Nonnull ItemStack emptyBucket(@Nonnull ItemStack stack, PlayerEntity player) {
        //in creative mode bucket is always full
        if (player.abilities.isCreativeMode) {
            return stack;
        }
        //contains lava? no empty bucket remains.
        if (FluidUtil.getFluidContained(stack).orElse(new FluidStack(Fluids.EMPTY, 0)).getFluid() == Fluids.LAVA) {
            return ItemStack.EMPTY;
        }
        //else empty bucket
        return new ItemStack(CeramicBucketItems.CERAMIC_BUCKET);
    }

    @Override
    public ICapabilityProvider initCapabilities(@Nonnull ItemStack stack, @Nullable CompoundNBT nbt) {
        return new FluidCeramicBucketWrapper(stack);
    }

    protected Item getCeramicVariant(Item bucket) {
        if (bucket.equals(Items.LAVA_BUCKET)) {
            return CeramicBucketItems.CERAMIC_LAVA_BUCKET;
        } else if (bucket.equals(Items.WATER_BUCKET)){
            return CeramicBucketItems.CERAMIC_WATER_BUCKET;
        }
        //TODO other fluids?
        return bucket;
    }

    /*
    * Copied following code from BucketItem class because of private fillBucket method.
    * this.containedBlock should be null (constructor)
    * So, change all positions with this.containedBlock to getFluid()
     */

    /**
     * Called to trigger the item's "innate" right click behavior. To handle when this item is used on a Block, see
     * {@link #onItemUse}.
     */
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        RayTraceResult raytraceresult = rayTrace(worldIn, playerIn, this.getFluid() == Fluids.EMPTY ? RayTraceContext.FluidMode.SOURCE_ONLY : RayTraceContext.FluidMode.NONE);
        ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onBucketUse(playerIn, worldIn, itemstack, raytraceresult);
        if (ret != null) return ret;
        if (raytraceresult.getType() == RayTraceResult.Type.MISS) {
            return new ActionResult<>(ActionResultType.PASS, itemstack);
        } else if (raytraceresult.getType() != RayTraceResult.Type.BLOCK) {
            return new ActionResult<>(ActionResultType.PASS, itemstack);
        } else {
            BlockRayTraceResult blockraytraceresult = (BlockRayTraceResult)raytraceresult;
            BlockPos blockpos = blockraytraceresult.getPos();
            if (worldIn.isBlockModifiable(playerIn, blockpos) && playerIn.canPlayerEdit(blockpos, blockraytraceresult.getFace(), itemstack)) {
                if (this.getFluid() == Fluids.EMPTY) {
                    BlockState blockstate1 = worldIn.getBlockState(blockpos);
                    if (blockstate1.getBlock() instanceof IBucketPickupHandler) {
                        Fluid fluid = ((IBucketPickupHandler)blockstate1.getBlock()).pickupFluid(worldIn, blockpos, blockstate1);
                        if (fluid != Fluids.EMPTY) {
                            playerIn.addStat(Stats.ITEM_USED.get(this));

                            SoundEvent soundevent = this.getFluid().getAttributes().getEmptySound();
                            if(soundevent == null) soundevent = fluid.isIn(FluidTags.LAVA) ? SoundEvents.ITEM_BUCKET_FILL_LAVA : SoundEvents.ITEM_BUCKET_FILL;
                            playerIn.playSound(soundevent, 1.0F, 1.0F);
                            ItemStack itemstack1 = this.fillBucket(itemstack, playerIn, this.getCeramicVariant(fluid.getFilledBucket())); //Ceramic variant
                            if (!worldIn.isRemote) {
                                CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayerEntity)playerIn, new ItemStack(this.getCeramicVariant(fluid.getFilledBucket()))); //Ceramic variant
                            }

                            return new ActionResult<>(ActionResultType.SUCCESS, itemstack1);
                        }
                    }

                    return new ActionResult<>(ActionResultType.FAIL, itemstack);
                } else {
                    BlockState blockstate = worldIn.getBlockState(blockpos);
                    BlockPos blockpos1 = blockstate.getBlock() instanceof ILiquidContainer && this.getFluid() == Fluids.WATER ? blockpos : blockraytraceresult.getPos().offset(blockraytraceresult.getFace());
                    if (this.tryPlaceContainedLiquid(playerIn, worldIn, blockpos1, blockraytraceresult)) {
                        this.onLiquidPlaced(worldIn, itemstack, blockpos1);
                        if (playerIn instanceof ServerPlayerEntity) {
                            CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity)playerIn, blockpos1, itemstack);
                        }

                        playerIn.addStat(Stats.ITEM_USED.get(this));
                        return new ActionResult<>(ActionResultType.SUCCESS, this.emptyBucket(itemstack, playerIn));
                    } else {
                        return new ActionResult<>(ActionResultType.FAIL, itemstack);
                    }
                }
            } else {
                return new ActionResult<>(ActionResultType.FAIL, itemstack);
            }
        }
    }

    private ItemStack fillBucket(ItemStack emptyBuckets, PlayerEntity player, Item fullBucket) {
        if (player.abilities.isCreativeMode) {
            return emptyBuckets;
        } else {
            emptyBuckets.shrink(1);
            if (emptyBuckets.isEmpty()) {
                return new ItemStack(fullBucket);
            } else {
                if (!player.inventory.addItemStackToInventory(new ItemStack(fullBucket))) {
                    player.dropItem(new ItemStack(fullBucket), false);
                }

                return emptyBuckets;
            }
        }
    }


}
