package cech12.ceramicbucket.item;

import cech12.ceramicbucket.util.CeramicBucketUtils;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;

public abstract class AbstractCeramicBucketItem extends BucketItem {

    public AbstractCeramicBucketItem(Supplier<? extends Fluid> supplier, Properties builder) {
        super(supplier, builder);
    }

    @Override
    public abstract ICapabilityProvider initCapabilities(@Nonnull ItemStack stack, @Nullable CompoundTag nbt);

    /**
     * This method copies the nbt data (except the fluid/entity data) of a source bucket to a target bucket.
     * It returns the changed target.
     * @param source bucket with color
     * @param target bucket where the color should be copied to
     * @return changed target bucket
     */
    public static ItemStack copyNBTWithoutBucketContent(ItemStack source, ItemStack target) {
        CompoundTag sourceNbt = source.getTag();
        if (sourceNbt != null && !sourceNbt.isEmpty()) {
            CompoundTag nbt = sourceNbt.copy();
            if (nbt.contains(FluidHandlerItemStack.FLUID_NBT_KEY)) {
                nbt.remove(FluidHandlerItemStack.FLUID_NBT_KEY);
            }
            if (nbt.contains("EntityType")) {
                nbt.remove("EntityType");
            }
            if (nbt.contains("EntityTag")) {
                nbt.remove("EntityTag");
            }
            CompoundTag targetNbt = target.getTag();
            if (targetNbt != null) {
                nbt = targetNbt.merge(nbt);
            }
            target.setTag(nbt);
        }
        return target;
    }

    public void playEmptySound(@Nullable Player player, @Nonnull LevelAccessor worldIn, @Nonnull BlockPos pos, @Nonnull ItemStack stack) {
        SoundEvent soundevent = this.getFluid(stack).getAttributes().getEmptySound();
        if (soundevent == null) soundevent = this.getFluid(stack).is(FluidTags.LAVA) ? SoundEvents.BUCKET_EMPTY_LAVA : SoundEvents.BUCKET_EMPTY;
        worldIn.playSound(player, pos, soundevent, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    public void playFillSound(@Nullable Player player, @Nonnull ItemStack stack) {
        if (player != null) {
            SoundEvent soundevent = this.getFluid(stack).getAttributes().getEmptySound();
            if (soundevent == null) soundevent = this.getFluid(stack).is(FluidTags.LAVA) ? SoundEvents.BUCKET_FILL_LAVA : SoundEvents.BUCKET_FILL;
            player.playSound(soundevent, 1.0F, 1.0F);
        }
    }

    @Override
    @Nonnull
    public InteractionResultHolder<ItemStack> use(@Nonnull Level level, Player playerIn, @Nonnull InteractionHand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        HitResult raytraceresult = getPlayerPOVHitResult(level, playerIn, this.getFluid(itemstack) == Fluids.EMPTY ? ClipContext.Fluid.SOURCE_ONLY : ClipContext.Fluid.NONE);
        InteractionResultHolder<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onBucketUse(playerIn, level, itemstack, raytraceresult);
        if (ret != null) return ret;
        if (raytraceresult.getType() == HitResult.Type.MISS) {
            return new InteractionResultHolder<>(InteractionResult.PASS, itemstack);
        } else if (raytraceresult.getType() != HitResult.Type.BLOCK) {
            return new InteractionResultHolder<>(InteractionResult.PASS, itemstack);
        } else {
            BlockHitResult blockraytraceresult = (BlockHitResult) raytraceresult;
            BlockPos blockpos = blockraytraceresult.getBlockPos();
            if (level.mayInteract(playerIn, blockpos) && playerIn.mayUseItemAt(blockpos, blockraytraceresult.getDirection(), itemstack)) {
                if (this.getFluid(itemstack) == Fluids.EMPTY) {
                    BlockState blockstate1 = level.getBlockState(blockpos);
                    if (blockstate1.getBlock() instanceof BucketPickup) {
                        ItemStack vanillaBucket = ((BucketPickup) blockstate1.getBlock()).pickupBlock(level, blockpos, blockstate1);
                        ItemStack bucket = CeramicBucketUtils.getFilledCeramicBucket(vanillaBucket, itemstack);
                        if (bucket != null) {
                            Fluid fluid = FluidUtil.getFluidContained(bucket).orElse(FluidStack.EMPTY).getFluid();
                            if (fluid != Fluids.EMPTY) {
                                playerIn.awardStat(Stats.ITEM_USED.get(this));

                                ItemStack itemstack1 = this.fillBucket(itemstack, playerIn, fluid);
                                this.playFillSound(playerIn, itemstack1);
                                if (!level.isClientSide) {
                                    CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer) playerIn, new ItemStack(fluid.getBucket()));
                                }

                                return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack1);
                            }
                        }
                    }

                }
                BlockState blockstate = level.getBlockState(blockpos);

                //TODO support cauldron interaction
                /*
                if (blockstate.getBlock() instanceof CauldronBlock) {
                    Fluid fluid = this.getFluid(itemstack);
                    CauldronBlock cauldron = (CauldronBlock) blockstate.getBlock();
                    int fillLevel = blockstate.getValue(CauldronBlock.LEVEL);
                    if (!(itemstack.getItem() instanceof CeramicEntityBucketItem)) {
                        if (fluid.is(FluidTags.WATER)) {
                            if (fillLevel < 3) {
                                ItemStack emptyStack = this.getEmptySuccessItem(itemstack, playerIn);
                                if (!level.isClientSide) {
                                    playerIn.awardStat(Stats.FILL_CAULDRON);
                                    cauldron.setWaterLevel(fillLevel, blockpos, blockstate, 3);
                                }
                                this.playEmptySound(playerIn, level, blockpos, itemstack);
                                itemstack = emptyStack;
                            }
                            return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
                        } else if (fluid == Fluids.EMPTY) {
                            if (fillLevel == 3) {
                                itemstack = this.fillBucket(itemstack, playerIn, Fluids.WATER);
                                if (!level.isClientSide) {
                                    playerIn.awardStat(Stats.USE_CAULDRON);
                                    cauldron.setWaterLevel(fillLevel, blockpos, blockstate, 0);
                                }
                                this.playFillSound(playerIn, itemstack);
                            }
                            return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
                        }
                    }
                }
                 */

                BlockPos blockpos1 = canBlockContainFluid(level, blockpos, blockstate, itemstack) ? blockpos : blockraytraceresult.getBlockPos().relative(blockraytraceresult.getDirection());
                if (this.tryPlaceContainedLiquid(playerIn, level, blockpos1, blockraytraceresult, itemstack)) {
                    this.checkExtraContent(playerIn, level, itemstack, blockpos1);
                    if (playerIn instanceof ServerPlayer) {
                        CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) playerIn, blockpos1, itemstack);
                    }

                    playerIn.awardStat(Stats.ITEM_USED.get(this));
                    return new InteractionResultHolder<>(InteractionResult.SUCCESS, this.internalGetEmptySuccessItem(itemstack, playerIn));
                } else {
                    return new InteractionResultHolder<>(InteractionResult.FAIL, itemstack);
                }
            } else {
                return new InteractionResultHolder<>(InteractionResult.FAIL, itemstack);
            }
        }
    }

    private ItemStack fillBucket(ItemStack stack, Player player, Fluid fluid) {
        if (player == null || !player.getAbilities().instabuild) {
            if (stack.getCount() > 1) {
                ItemStack newStack = CeramicBucketUtils.getFilledCeramicBucket(fluid, stack);
                stack.shrink(1);
                if (player != null && !player.getInventory().add(newStack)) {
                    player.drop(newStack, false);
                }
                //old stack must be returned
            } else {
                return fill(stack, new FluidStack(fluid, FluidAttributes.BUCKET_VOLUME));
            }
        }
        return stack;
    }

    @Nonnull
    public ItemStack internalGetEmptySuccessItem(@Nonnull ItemStack stack, @Nullable Player player) {
        if (player != null && player.getAbilities().instabuild) {
            return stack;
        }
        return drain(stack, FluidAttributes.BUCKET_VOLUME);
    }

    public boolean tryPlaceContainedLiquid(@Nullable Player player, Level worldIn, BlockPos posIn, @Nullable BlockHitResult raytrace, ItemStack stack) {
        Fluid fluid = this.getFluid(stack);
        FluidAttributes fluidAttributes = fluid.getAttributes();
        if (!(fluid instanceof FlowingFluid)) {
            return false;
        } else if (!fluidAttributes.canBePlacedInWorld(worldIn, posIn, fluid.defaultFluidState())) {
            return false;
        } else {
            BlockState blockstate = worldIn.getBlockState(posIn);
            Material material = blockstate.getMaterial();
            boolean flag = !material.isSolid();
            boolean flag1 = material.isReplaceable();
            boolean canContainFluid = canBlockContainFluid(worldIn, posIn, blockstate, stack);
            if (worldIn.isEmptyBlock(posIn) || flag || flag1 || canContainFluid) {
                IFluidHandlerItem fluidHandler = FluidUtil.getFluidHandler(stack).orElse(null);
                FluidStack fluidStack = fluidHandler != null ? fluidHandler.drain(FluidAttributes.BUCKET_VOLUME, IFluidHandler.FluidAction.SIMULATE) : null;
                if (fluidStack != null && worldIn.dimensionType().ultraWarm() && this.getFluid(stack).is(FluidTags.WATER)) {
                    fluidAttributes.vaporize(player, worldIn, posIn, fluidStack);
                } else if (canContainFluid) {
                    if (((LiquidBlockContainer) blockstate.getBlock()).placeLiquid(worldIn, posIn, blockstate, ((FlowingFluid) fluid).getSource(false))) {
                        this.playEmptySound(player, worldIn, posIn, stack);
                    }
                } else {
                    if (!worldIn.isClientSide && (flag || flag1) && !material.isLiquid()) {
                        worldIn.destroyBlock(posIn, true);
                    }

                    this.playEmptySound(player, worldIn, posIn, stack);
                    worldIn.setBlock(posIn, fluid.defaultFluidState().createLegacyBlock(), 11);
                }

                return true;
            } else {
                return raytrace != null && this.tryPlaceContainedLiquid(player, worldIn, raytrace.getBlockPos().relative(raytrace.getDirection()), null, stack);
            }
        }
    }

    @Deprecated
    @Override
    @Nonnull
    public Fluid getFluid() {
        return Fluids.EMPTY;
    }

    public Fluid getFluid(ItemStack stack) {
        final LazyOptional<IFluidHandlerItem> cap = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY);
        if (cap.isPresent()) {
            final FluidHandlerItemStack fluidHandler = (FluidHandlerItemStack) cap.orElseThrow(NullPointerException::new);
            return fluidHandler.getFluid().getFluid();
        }
        return Fluids.EMPTY;
    }

    public ItemStack fill(ItemStack stack, FluidStack fluidStack) {
        final LazyOptional<IFluidHandlerItem> cap = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY);
        if (cap.isPresent()) {
            final FluidHandlerItemStack fluidHandler = (FluidHandlerItemStack) cap.orElseThrow(NullPointerException::new);
            fluidHandler.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
            return fluidHandler.getContainer();
        }
        return stack;
    }

    public ItemStack drain(ItemStack stack, int drainAmount) {
        final LazyOptional<IFluidHandlerItem> cap = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY);
        if (cap.isPresent()) {
            final FluidHandlerItemStack fluidHandler = (FluidHandlerItemStack) cap.orElseThrow(NullPointerException::new);
            fluidHandler.drain(drainAmount, IFluidHandler.FluidAction.EXECUTE);
            return fluidHandler.getContainer();
        }
        return stack;
    }

    private boolean canBlockContainFluid(Level worldIn, BlockPos posIn, BlockState blockstate, ItemStack itemStack) {
        return blockstate.getBlock() instanceof LiquidBlockContainer && ((LiquidBlockContainer)blockstate.getBlock()).canPlaceLiquid(worldIn, posIn, blockstate, this.getFluid(itemStack));
    }

    public boolean isCrackedBucket(ItemStack stack) {
        return CeramicBucketUtils.isFluidTooHotForCeramicBucket(this.getFluid(stack))
                && !CeramicBucketUtils.isAffectedByInfinityEnchantment(stack);
    }

    public static boolean hasColor(ItemStack stack) {
        CompoundTag compoundnbt = stack.getTagElement("display");
        return compoundnbt != null && compoundnbt.contains("color", 99);
    }

    public static int getColor(ItemStack stack) {
        CompoundTag compoundnbt = stack.getTagElement("display");
        //rawColorFromRGB(219, 107, 76); //14379852 //on white
        //rawColorFromRGB(228, 129, 104); //14975336 //on white terracotta
        return compoundnbt != null && compoundnbt.contains("color", 99) ? compoundnbt.getInt("color") : 14975336;
    }

    public static void removeColor(ItemStack stack) {
        CompoundTag compoundnbt = stack.getTagElement("display");
        if (compoundnbt != null && compoundnbt.contains("color")) {
            compoundnbt.remove("color");
        }
    }

    public static void setColor(ItemStack stack, int color) {
        stack.getOrCreateTagElement("display").putInt("color", color);
    }

    public static ItemStack dyeItem(ItemStack stack, List<DyeItem> dyes) {
        ItemStack resultStack = ItemStack.EMPTY;
        int[] color = new int[3];
        int i = 0;
        int j = 0;
        AbstractCeramicBucketItem bucketItem = null;
        Item item = stack.getItem();
        if (item instanceof AbstractCeramicBucketItem) {
            bucketItem = (AbstractCeramicBucketItem)item;
            resultStack = stack.copy();
            resultStack.setCount(1);
            if (hasColor(stack)) {
                int k = getColor(resultStack);
                float f = (float)(k >> 16 & 255) / 255.0F;
                float f1 = (float)(k >> 8 & 255) / 255.0F;
                float f2 = (float)(k & 255) / 255.0F;
                i = (int)((float)i + Math.max(f, Math.max(f1, f2)) * 255.0F);
                color[0] = (int)((float)color[0] + f * 255.0F);
                color[1] = (int)((float)color[1] + f1 * 255.0F);
                color[2] = (int)((float)color[2] + f2 * 255.0F);
                ++j;
            }

            for (DyeItem dyeitem : dyes) {
                float[] afloat = dyeitem.getDyeColor().getTextureDiffuseColors();
                int i2 = (int)(afloat[0] * 255.0F);
                int l = (int)(afloat[1] * 255.0F);
                int i1 = (int)(afloat[2] * 255.0F);
                i += Math.max(i2, Math.max(l, i1));
                color[0] += i2;
                color[1] += l;
                color[2] += i1;
                ++j;
            }
        }

        if (bucketItem == null) {
            return ItemStack.EMPTY;
        } else {
            int j1 = color[0] / j;
            int k1 = color[1] / j;
            int l1 = color[2] / j;
            float f3 = (float)i / (float)j;
            float f4 = (float)Math.max(j1, Math.max(k1, l1));
            j1 = (int)((float)j1 * f3 / f4);
            k1 = (int)((float)k1 * f3 / f4);
            l1 = (int)((float)l1 * f3 / f4);
            int j2 = (j1 << 8) + k1;
            j2 = (j2 << 8) + l1;
            setColor(resultStack, j2);
            return resultStack;
        }
    }


}
