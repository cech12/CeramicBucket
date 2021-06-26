package cech12.ceramicbucket.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.fish.AbstractFishEntity;
import net.minecraft.entity.passive.fish.TropicalFishEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

//TODO can be removed with 1.17 update
@Deprecated
public class CeramicFishBucketItem extends FilledCeramicBucketItem {

    private final EntityType<?> fishType;

    public CeramicFishBucketItem(EntityType<?> fishTypeIn, Item.Properties builder) {
        super(builder);
        this.fishType = fishTypeIn;
    }

    public void checkExtraContent(World worldIn, @Nonnull ItemStack p_203792_2_, @Nonnull BlockPos pos) {
        if (!worldIn.isClientSide && worldIn instanceof ServerWorld) {
            this.placeFish((ServerWorld) worldIn, p_203792_2_, pos);
        }
    }

    protected void playEmptySound(@Nullable PlayerEntity player, IWorld worldIn, @Nonnull BlockPos pos) {
        worldIn.playSound(player, pos, SoundEvents.BUCKET_EMPTY_FISH, SoundCategory.NEUTRAL, 1.0F, 1.0F);
    }

    private void placeFish(ServerWorld worldIn, ItemStack p_205357_2_, BlockPos pos) {
        Entity entity = this.fishType.spawn(worldIn, p_205357_2_, null, pos, SpawnReason.BUCKET, true, false);
        if (entity != null) {
            ((AbstractFishEntity)entity).setFromBucket(true);
        }
    }

    /**
     * allows items to add custom lines of information to the mouseover description
     */
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable World worldIn, @Nonnull List<ITextComponent> tooltip, @Nonnull ITooltipFlag flagIn) {
        if (this.fishType == EntityType.TROPICAL_FISH) {
            CompoundNBT compoundnbt = stack.getTag();
            if (compoundnbt != null && compoundnbt.contains("BucketVariantTag", 3)) {
                int i = compoundnbt.getInt("BucketVariantTag");
                TextFormatting[] atextformatting = new TextFormatting[]{TextFormatting.ITALIC, TextFormatting.GRAY};
                String s = "color.minecraft." + TropicalFishEntity.getBaseColor(i);
                String s1 = "color.minecraft." + TropicalFishEntity.getPatternColor(i);

                for(int j = 0; j < TropicalFishEntity.COMMON_VARIANTS.length; ++j) {
                    if (i == TropicalFishEntity.COMMON_VARIANTS[j]) {
                        tooltip.add((new TranslationTextComponent(TropicalFishEntity.getPredefinedName(j))).withStyle(atextformatting));
                        return;
                    }
                }

                tooltip.add((new TranslationTextComponent(TropicalFishEntity.getFishTypeName(i))).withStyle(atextformatting));
                TranslationTextComponent textComponent = new TranslationTextComponent(s);
                if (!s.equals(s1)) {
                    textComponent.append(", ").append(new TranslationTextComponent(s1));
                }

                textComponent.withStyle(atextformatting);
                tooltip.add(textComponent);
            }
        }
    }

    @Override
    public Collection<ItemGroup> getCreativeTabs() {
        //do nothing
        return Collections.singletonList(null);
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    @Override
    public void fillItemCategory(@Nonnull ItemGroup group, @Nonnull NonNullList<ItemStack> items) {
        //do nothing
    }

    @Override
    @Nonnull
    public ITextComponent getName(@Nonnull ItemStack stack) {
        return new TranslationTextComponent("item.ceramicbucket.ceramic_entity_bucket", fishType.getDescription());
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment.category.canEnchant(stack.getItem());
    }
}
