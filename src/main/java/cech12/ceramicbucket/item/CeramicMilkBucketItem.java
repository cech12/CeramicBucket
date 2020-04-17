package cech12.ceramicbucket.item;

import cech12.ceramicbucket.api.item.CeramicBucketItems;
import cech12.ceramicbucket.util.CeramicBucketUtils;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.UseAction;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;

public class CeramicMilkBucketItem extends FilledCeramicBucketItem {

    public CeramicMilkBucketItem(Properties builder) {
        super(builder);
    }

    @Override
    public ItemStack getFilledInstance(@Nonnull Fluid fluid) {
        //can only be filled with milk
        if (CeramicBucketUtils.isMilkFluid(fluid)) {
            return super.getFilledInstance(fluid);
        }
        return super.getFilledInstance(Fluids.EMPTY);
    }

    /**
     * Get a milk bucket with the first fluid that is milk. If there is no milk fluid, the bucket gets empty fluid.
     */
    public ItemStack getFilledInstance() {
        for (Fluid fluid : ForgeRegistries.FLUIDS) {
            //search first milk fluid
            if (fluid.getDefaultState().isSource() && CeramicBucketUtils.isMilkFluid(fluid)) {
                return super.getFilledInstance(fluid);
            }
        }
        return super.getFilledInstance(Fluids.EMPTY);
    }

    @Nonnull
    @Override
    public ItemStack getDefaultInstance() {
        return this.getFilledInstance();
    }

    /**
     * Like vanilla milk bucket.
     */
    @Override
    @Nonnull
    public ItemStack onItemUseFinish(@Nonnull ItemStack stack, World worldIn, @Nonnull LivingEntity entityLiving) {
        if (!worldIn.isRemote) entityLiving.curePotionEffects(new ItemStack(Items.MILK_BUCKET)); // FORGE - move up so stack.shrink does not turn stack into air

        if (entityLiving instanceof ServerPlayerEntity) {
            ServerPlayerEntity serverplayerentity = (ServerPlayerEntity)entityLiving;
            CriteriaTriggers.CONSUME_ITEM.trigger(serverplayerentity, stack);
            serverplayerentity.addStat(Stats.ITEM_USED.get(this));
        }

        if (entityLiving instanceof PlayerEntity && !((PlayerEntity)entityLiving).abilities.isCreativeMode) {
            stack.shrink(1);
        }

        return stack.isEmpty() ? new ItemStack(CeramicBucketItems.CERAMIC_BUCKET) : stack;
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
    public UseAction getUseAction(@Nonnull ItemStack stack) {
        return UseAction.DRINK;
    }

    /**
     * Place milk or on failure drink it.
     */
    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World worldIn, PlayerEntity playerIn, @Nonnull Hand handIn) {
        ActionResult<ItemStack> result = super.onItemRightClick(worldIn, playerIn, handIn);
        //when no fluid can be placed, drink it
        if (result.getType() != ActionResultType.SUCCESS) {
            playerIn.setActiveHand(handIn);
            result = new ActionResult<>(ActionResultType.SUCCESS, playerIn.getHeldItem(handIn));
        }
        return result;
    }

    @Override
    public void fillItemGroup(@Nonnull ItemGroup group, @Nonnull NonNullList<ItemStack> items) {
        if (this.isInGroup(group)) {
            items.add(this.getFilledInstance());
        }
    }

    @Override
    @Nonnull
    public ITextComponent getDisplayName(@Nonnull ItemStack stack) {
        return new TranslationTextComponent("item.ceramicbucket.ceramic_milk_bucket");
    }

}
