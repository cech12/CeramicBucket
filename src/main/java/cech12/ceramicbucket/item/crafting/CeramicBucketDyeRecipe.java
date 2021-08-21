package cech12.ceramicbucket.item.crafting;

import cech12.ceramicbucket.CeramicBucketMod;
import cech12.ceramicbucket.item.AbstractCeramicBucketItem;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class CeramicBucketDyeRecipe extends CustomRecipe {

    public static final SimpleRecipeSerializer<CeramicBucketDyeRecipe> SERIALIZER = new Serializer();

    public CeramicBucketDyeRecipe(ResourceLocation id) {
        super(id);
    }

    @Nullable
    private Pair<ItemStack, List<DyeItem>> getBucketAndDyes(@Nonnull CraftingContainer inv) {
        ItemStack bucket = ItemStack.EMPTY;
        List<DyeItem> dyeItems = Lists.newArrayList();
        for (int i = 0; i < inv.getContainerSize(); ++i) {
            ItemStack stack = inv.getItem(i);
            if (!stack.isEmpty()) {
                Item item = stack.getItem();
                if (item instanceof AbstractCeramicBucketItem) {
                    if (!bucket.isEmpty()) {
                        return null;
                    }
                    bucket = stack;
                } else {
                    if (!(item instanceof DyeItem)) {
                        return null;
                    }
                    dyeItems.add((DyeItem)item);
                }
            }
        }
        if (bucket.isEmpty() || dyeItems.isEmpty()) {
            return null;
        }
        return new Pair<>(bucket, dyeItems);
    }

    /**
     * Used to check if a recipe matches current crafting inventory
     */
    public boolean matches(@Nonnull CraftingContainer inv, @Nonnull Level worldIn) {
        Pair<ItemStack, List<DyeItem>> bucketAndDyes = getBucketAndDyes(inv);
        return bucketAndDyes != null;
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    @Nonnull
    public ItemStack assemble(@Nonnull CraftingContainer inv) {
        Pair<ItemStack, List<DyeItem>> bucketAndDyes = getBucketAndDyes(inv);
        if (bucketAndDyes != null) {
            return AbstractCeramicBucketItem.dyeItem(bucketAndDyes.getFirst(), bucketAndDyes.getSecond());
        }
        return ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer inv) {
        //override it to avoid remaining container items
        return NonNullList.withSize(inv.getContainerSize(), ItemStack.EMPTY);
    }

    /**
     * Used to determine if this recipe can fit in a grid of the given width/height
     */
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Nonnull
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    private static class Serializer extends SimpleRecipeSerializer<CeramicBucketDyeRecipe> {
        public Serializer() {
            super(CeramicBucketDyeRecipe::new);
            this.setRegistryName(CeramicBucketMod.MOD_ID, "ceramic_bucket_dye_recipe");
        }
    }

}
