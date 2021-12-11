package cech12.ceramicbucket.item;

import cech12.ceramicbucket.api.data.ObtainableEntityType;
import cech12.ceramicbucket.compat.ModCompat;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.NonNullList;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.ClipContext;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CeramicEntityBucketItem extends FilledCeramicBucketItem {

    public CeramicEntityBucketItem(Item.Properties builder) {
        super(builder);
    }

    @Deprecated
    @Override
    public ItemStack getFilledInstance(@Nonnull Fluid fluid, @Nullable ItemStack oldStack) {
        return ItemStack.EMPTY;
    }

    public ItemStack getFilledInstance(@Nonnull Fluid fluid, @Nonnull Entity entity, @Nullable ItemStack oldStack) {
        return this.putEntityInStack(super.getFilledInstance(fluid, oldStack), entity);
    }

    public ItemStack getFilledInstance(@Nonnull Fluid fluid, @Nonnull EntityType<?> entityType) {
        return this.putEntityTypeInStack(super.getFilledInstance(fluid, null), entityType);
    }

    @Override
    @Nonnull
    public InteractionResultHolder<ItemStack> use(@Nonnull Level level, Player playerIn, @Nonnull InteractionHand handIn) {
        //support empty fluids
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        if (this.getFluid(itemstack) == Fluids.EMPTY) {
            BlockHitResult raytraceresult = getPlayerPOVHitResult(level, playerIn, ClipContext.Fluid.NONE);
            //ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onBucketUse(playerIn, worldIn, itemstack, raytraceresult);
            BlockPos blockpos = raytraceresult.getBlockPos().relative(raytraceresult.getDirection());
            this.checkExtraContent(playerIn, level, itemstack, blockpos);
            ItemStack result = (!playerIn.getAbilities().instabuild) ? this.getContainerItem(itemstack) : itemstack;
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, result);
        }
        return super.use(level, playerIn, handIn);
    }

    @Override
    public void checkExtraContent(@Nullable Player player, Level worldIn, @Nonnull ItemStack stack, @Nonnull BlockPos blockPos) {
        if (!worldIn.isClientSide && worldIn instanceof ServerLevel) {
            EntityType<?> entityType = getEntityTypeFromStack(stack);
            if (entityType != null) {
                Entity entity = entityType.spawn((ServerLevel) worldIn, stack, null, blockPos, MobSpawnType.BUCKET, true, false);
                if (entity instanceof AbstractFish) {
                    ((AbstractFish)entity).setFromBucket(true);
                } else if (entity instanceof Axolotl) {
                    ((Axolotl)entity).setFromBucket(true);
                } else if (entity instanceof Mob) {
                    ((Mob)entity).setPersistenceRequired(); //TODO really?
                }
            }
        }
    }

    @Override
    public void playEmptySound(@Nullable Player player, @Nonnull LevelAccessor worldIn, @Nonnull BlockPos pos, @Nonnull ItemStack stack) {
        ObtainableEntityType type = ModCompat.getObtainableEntityType(this.getEntityTypeFromStack(stack));
        if (type != null) {
            SoundEvent soundevent = type.getEmptySound();
            worldIn.playSound(player, pos, soundevent, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    @Override
    public void playFillSound(@Nullable Player player, @Nonnull ItemStack stack) {
        if (player == null) return;
        ObtainableEntityType type = ModCompat.getObtainableEntityType(this.getEntityTypeFromStack(stack));
        if (type != null) {
            SoundEvent soundevent = type.getFillSound();
            player.playSound(soundevent, 1.0F, 1.0F);
        }
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    @Override
    public void fillItemCategory(@Nonnull CreativeModeTab group, @Nonnull NonNullList<ItemStack> items) {
        if (this.allowdedIn(group)) {
            for (ObtainableEntityType type : ModCompat.getObtainableEntityTypes()) {
                EntityType<?> entityType = type.getEntityType();
                if (entityType != null) {
                    items.add(this.getFilledInstance(type.getOneFluid(), entityType));
                }
            }
        }
    }

    @Override
    @Nonnull
    public Component getName(@Nonnull ItemStack stack) {
        EntityType<?> type = getEntityTypeFromStack(stack);
        Component name = (type != null) ? type.getDescription() : new TextComponent("?");
        return new TranslatableComponent("item.ceramicbucket.ceramic_entity_bucket", name);
    }

    public ItemStack putEntityInStack(ItemStack stack, Entity entity) {
        CompoundTag nbt = stack.getOrCreateTag();
        nbt.putString("EntityType", EntityType.getKey(entity.getType()).toString());
        CompoundTag entityNbt = entity.saveWithoutId(new CompoundTag());
        entityNbt.remove("Pos");
        entityNbt.remove("Motion");
        entityNbt.remove("FallDistance");
        nbt.put("EntityTag", entityNbt); //is read by spawn method
        stack.setTag(nbt);
        entity.discard();
        return stack;
    }

    private ItemStack putEntityTypeInStack(ItemStack stack, EntityType<?> type) {
        CompoundTag nbt = stack.getOrCreateTag();
        nbt.putString("EntityType", EntityType.getKey(type).toString());
        stack.setTag(nbt);
        return stack;
    }

    @Nullable
    public EntityType<?> getEntityTypeFromStack(ItemStack stack) {
        if (stack.hasTag()) {
            return EntityType.byString(stack.getTag().getString("EntityType")).orElse(null);
        }
        return null;
    }

    @Override
    public boolean isCrackedBucket(ItemStack stack) {
        ObtainableEntityType type = ModCompat.getObtainableEntityType(this.getEntityTypeFromStack(stack));
        if (type != null) {
            Boolean cracksBucket = type.cracksBucket();
            if (cracksBucket != null) {
                return cracksBucket;
            }
        }
        return super.isCrackedBucket(stack);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment.category.canEnchant(stack.getItem());
    }
}
