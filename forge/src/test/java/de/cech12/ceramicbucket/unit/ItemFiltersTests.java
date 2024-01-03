package de.cech12.ceramicbucket.unit;

/*
import cech12.ceramicbucket.api.item.CeramicBucketItems;
import cech12.ceramicbucket.item.CeramicEntityBucketItem;
import cech12.ceramicbucket.item.FilledCeramicBucketItem;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ITag;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.util.ResourceLocation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This class tests the ItemStack equality tests of the Item Filters mod. (to support FTB Quests)
 * https://www.curseforge.com/minecraft/mc-mods/item-filters-forge
 /
public class ItemFiltersTests {

    @Test
    public void testTagDoesNotMatchForDifferentFilledBuckets() {
        ItemStack waterBucket = ((FilledCeramicBucketItem) CeramicBucketItems.FILLED_CERAMIC_BUCKET).getFilledInstance(Fluids.WATER, null);
        ItemStack lavaBucket = ((FilledCeramicBucketItem) CeramicBucketItems.FILLED_CERAMIC_BUCKET).getFilledInstance(Fluids.LAVA, null);

        assertFalse(ItemStack.tagMatches(waterBucket, lavaBucket), "Filled buckets with different fluids should return false for ItemStack.tagMatches");
        assertFalse(ItemStack.tagMatches(waterBucket, lavaBucket), "Filled buckets with different fluids should return false for areItemStacksEqual");
    }

    @Test
    public void testTagMatchesForEqualFilledBuckets() {
        ItemStack waterBucket1 = ((FilledCeramicBucketItem) CeramicBucketItems.FILLED_CERAMIC_BUCKET).getFilledInstance(Fluids.WATER, null);
        ItemStack waterBucket2 = ((FilledCeramicBucketItem) CeramicBucketItems.FILLED_CERAMIC_BUCKET).getFilledInstance(Fluids.WATER, null);

        assertTrue(ItemStack.tagMatches(waterBucket1, waterBucket2), "Filled buckets with same fluid should return true for ItemStack.tagMatches");
        assertTrue(areItemStacksEqual(waterBucket1, waterBucket2), "Filled buckets with same fluid should return true for areItemStacksEqual");
    }

    @Test
    public void testTagDoesNotMatchForDifferentFilledEntityBuckets() {
        ItemStack codBucket = ((CeramicEntityBucketItem) CeramicBucketItems.CERAMIC_ENTITY_BUCKET).getFilledInstance(Fluids.WATER, EntityType.COD);
        ItemStack salmonBucket = ((CeramicEntityBucketItem) CeramicBucketItems.CERAMIC_ENTITY_BUCKET).getFilledInstance(Fluids.WATER, EntityType.SALMON);

        assertFalse(ItemStack.tagMatches(codBucket, salmonBucket), "Filled entity buckets with different fluid/entity should return false for ItemStack.tagMatches");
        assertFalse(areItemStacksEqual(codBucket, salmonBucket), "Filled entity buckets with different fluid/entity should return false for areItemStacksEqual");
    }

    @Test
    public void testTagMatchesForEqualFilledEntityBuckets() {
        ItemStack salmonBucket1 = ((CeramicEntityBucketItem) CeramicBucketItems.CERAMIC_ENTITY_BUCKET).getFilledInstance(Fluids.WATER, EntityType.SALMON);
        ItemStack salmonBucket2 = ((CeramicEntityBucketItem) CeramicBucketItems.CERAMIC_ENTITY_BUCKET).getFilledInstance(Fluids.WATER, EntityType.SALMON);

        assertTrue(ItemStack.tagMatches(salmonBucket1, salmonBucket2), "Filled entity buckets with same fluid/entity should return true for ItemStack.tagMatches");
        assertTrue(areItemStacksEqual(salmonBucket1, salmonBucket2), "Filled entity buckets with same fluid/entity should return true for areItemStacksEqual");
    }

    // copy of the relevant method
    // https://github.com/LatvianModder/Item-Filters/blob/main/common/src/main/java/dev/latvian/mods/itemfilters/api/ItemFiltersAPI.java#L52
    public boolean areItemStacksEqual(ItemStack stackA, ItemStack stackB) {
        if (stackA == stackB) {
            return true;
        }

        if (stackA.getItem() != stackB.getItem()) {
            return false;
        } else if (!stackA.hasTag() && !stackB.hasTag()) {
            return true;
        }

        ITag<Item> tag = TagCollectionManager.getInstance().getItems().getTag(new ResourceLocation("itemfilters", "check_nbt"));

        if (tag == null) {
            return false;
        }

        return !tag.contains(stackA.getItem()) || ItemStack.tagMatches(stackA, stackB);
    }

}
 */
