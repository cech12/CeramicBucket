package cech12.ceramicbucket.item;

import cech12.ceramicbucket.util.CeramicBucketUtils;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CeramicMilkBucketItem extends FilledCeramicBucketItem {

    public CeramicMilkBucketItem(Properties builder) {
        super(builder);
    }

    @Override
    public ItemStack getFilledInstance(@Nonnull Fluid fluid, @Nullable ItemStack oldStack) {
        //can only be filled with milk
        if (CeramicBucketUtils.isMilkFluid(fluid)) {
            return super.getFilledInstance(fluid, oldStack);
        }
        return super.getFilledInstance(Fluids.EMPTY, oldStack);
    }

    /**
     * Get a milk bucket with the first fluid that is milk. If there is no milk fluid, the bucket gets empty fluid.
     */
    public ItemStack getFilledInstance(boolean checkTag, @Nullable ItemStack oldStack) {
        for (Fluid fluid : ForgeRegistries.FLUIDS) {
            //search first milk fluid
            if (fluid.defaultFluidState().isSource() && CeramicBucketUtils.isMilkFluid(fluid, checkTag)) {
                return super.getFilledInstance(fluid, oldStack);
            }
        }
        return super.getFilledInstance(Fluids.EMPTY, oldStack);
    }

    /**
     * Get a milk bucket with the first fluid that is milk. If there is no milk fluid, the bucket gets empty fluid.
     */
    @Nonnull
    public ItemStack getFilledInstance(@Nullable ItemStack oldStack) {
        return this.getFilledInstance(true, oldStack);
    }

    @Nonnull
    @Override
    public ItemStack getDefaultInstance() {
        return this.getFilledInstance(null);
    }

    /**
     * Like vanilla milk bucket.
     */
    @Override
    @Nonnull
    public ItemStack finishUsingItem(@Nonnull ItemStack stack, Level worldIn, @Nonnull LivingEntity entityLiving) {
        ItemStack vanillaStack = new ItemStack(Items.MILK_BUCKET);
        if (!worldIn.isClientSide) entityLiving.curePotionEffects(vanillaStack); // FORGE - move up so stack.shrink does not turn stack into air

        if (entityLiving instanceof ServerPlayer) {
            ServerPlayer serverplayerentity = (ServerPlayer)entityLiving;
            CriteriaTriggers.CONSUME_ITEM.trigger(serverplayerentity, vanillaStack);
            serverplayerentity.awardStat(Stats.ITEM_USED.get(this));
        }

        if (entityLiving instanceof Player && !((Player)entityLiving).getAbilities().instabuild) {
            stack.shrink(1);
        }

        return stack.isEmpty() ? this.getContainerItem(stack) : stack;
    }

    /**
     * Like vanilla milk bucket.
     */
    @Override
    public int getUseDuration(@Nonnull ItemStack stack) {
        return 32;
    }

    /**
     * Like vanilla milk bucket.
     */
    @Override
    @Nonnull
    public UseAnim getUseAnimation(@Nonnull ItemStack stack) {
        return UseAnim.DRINK;
    }

    /**
     * Place milk or on failure drink it.
     */
    @Override
    @Nonnull
    public InteractionResultHolder<ItemStack> use(@Nonnull Level level, Player playerIn, @Nonnull InteractionHand handIn) {
        InteractionResultHolder<ItemStack> result = super.use(level, playerIn, handIn);
        //when no fluid can be placed, drink it
        if (result.getResult() != InteractionResult.SUCCESS) {
            playerIn.startUsingItem(handIn);
            result = new InteractionResultHolder<>(InteractionResult.SUCCESS, playerIn.getItemInHand(handIn));
        }
        return result;
    }

    @Override
    public void fillItemCategory(@Nonnull CreativeModeTab group, @Nonnull NonNullList<ItemStack> items) {
        if (this.allowdedIn(group)) {
            items.add(this.getFilledInstance(false, null));
        }
    }

    @Override
    @Nonnull
    public Component getName(@Nonnull ItemStack stack) {
        return new TranslatableComponent("item.ceramicbucket.ceramic_milk_bucket");
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        //super method checks if a fluid is inside. Milk does not have to be a fluid.
        Fluid fluid = this.getFluid(stack);
        return fluid == Fluids.EMPTY || !CeramicBucketUtils.isFluidTooHotForCeramicBucket(fluid);
    }

}
