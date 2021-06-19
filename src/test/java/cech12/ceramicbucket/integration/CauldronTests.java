package cech12.ceramicbucket.integration;

import cech12.ceramicbucket.BucketTestUtils;
import cech12.ceramicbucket.IntegrationTestUtils;
import com.alcatrazescapee.mcjunitlib.framework.IntegrationTest;
import com.alcatrazescapee.mcjunitlib.framework.IntegrationTestClass;
import com.alcatrazescapee.mcjunitlib.framework.IntegrationTestHelper;
import net.minecraft.block.CauldronBlock;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;

@IntegrationTestClass(value = "cauldron")
public class CauldronTests {

    private static final BlockPos CAULDRON_POSITION = new BlockPos(1, 1, 1);

    @IntegrationTest(value = "cauldron_empty")
    public void testWaterBucketFillsEmptyCauldron(IntegrationTestHelper helper) {
        ItemStack bucket = BucketTestUtils.getFilledBucket(Fluids.WATER);

        ActionResult<ItemStack> resultBucket = IntegrationTestUtils.useItem(helper, CAULDRON_POSITION, bucket);

        helper.assertAirAt(CAULDRON_POSITION.above(), "No fluid should have been placed above the cauldron");
        helper.assertTrue(() -> helper.getBlockState(CAULDRON_POSITION).getValue(CauldronBlock.LEVEL) == 3, "Cauldron should be filled completely");
        helper.assertTrue(() -> BucketTestUtils.isEmptyBucket(resultBucket.getObject()), "Empty bucket should remain after filling cauldron");
    }

    @IntegrationTest(value = "cauldron_empty")
    public void testEmptyBucketDoesNothingOnEmptyCauldron(IntegrationTestHelper helper) {
        ItemStack bucket = BucketTestUtils.getEmptyBucket();

        ActionResult<ItemStack> resultBucket = IntegrationTestUtils.useItem(helper, CAULDRON_POSITION, bucket);

        helper.assertAirAt(CAULDRON_POSITION.above(), "No fluid should have been placed above the cauldron");
        helper.assertTrue(() -> helper.getBlockState(CAULDRON_POSITION).getValue(CauldronBlock.LEVEL) == 0, "Cauldron should be empty");
        helper.assertTrue(() -> BucketTestUtils.isEmptyBucket(resultBucket.getObject()), "Empty bucket should remain after using it on cauldron");
    }

    @IntegrationTest(value = "cauldron_water_one_third")
    public void testWaterBucketFillsOneThirdCauldron(IntegrationTestHelper helper) {
        ItemStack bucket = BucketTestUtils.getFilledBucket(Fluids.WATER);

        ActionResult<ItemStack> resultBucket = IntegrationTestUtils.useItem(helper, CAULDRON_POSITION, bucket);

        helper.assertAirAt(CAULDRON_POSITION.above(), "No fluid should have been placed above the cauldron");
        helper.assertTrue(() -> helper.getBlockState(CAULDRON_POSITION).getValue(CauldronBlock.LEVEL) == 3, "Cauldron should be filled completely");
        helper.assertTrue(() -> BucketTestUtils.isEmptyBucket(resultBucket.getObject()), "Empty bucket should remain after filling cauldron");
    }

    @IntegrationTest(value = "cauldron_water_one_third")
    public void testEmptyBucketDoesNothingOnOneThirdCauldron(IntegrationTestHelper helper) {
        ItemStack bucket = BucketTestUtils.getEmptyBucket();

        ActionResult<ItemStack> resultBucket = IntegrationTestUtils.useItem(helper, CAULDRON_POSITION, bucket);

        helper.assertAirAt(CAULDRON_POSITION.above(), "No fluid should have been placed above the cauldron");
        helper.assertTrue(() -> helper.getBlockState(CAULDRON_POSITION).getValue(CauldronBlock.LEVEL) == 1, "One third of cauldron should be filled");
        helper.assertTrue(() -> BucketTestUtils.isEmptyBucket(resultBucket.getObject()), "Empty bucket should remain after using it on cauldron");
    }

    @IntegrationTest(value = "cauldron_water_two_thirds")
    public void testWaterBucketFillsTwoThirdsCauldron(IntegrationTestHelper helper) {
        ItemStack bucket = BucketTestUtils.getFilledBucket(Fluids.WATER);

        ActionResult<ItemStack> resultBucket = IntegrationTestUtils.useItem(helper, CAULDRON_POSITION, bucket);

        helper.assertAirAt(CAULDRON_POSITION.above(), "No fluid should have been placed above the cauldron");
        helper.assertTrue(() -> helper.getBlockState(CAULDRON_POSITION).getValue(CauldronBlock.LEVEL) == 3, "Cauldron should be filled completely");
        helper.assertTrue(() -> BucketTestUtils.isEmptyBucket(resultBucket.getObject()), "Empty bucket should remain after filling cauldron");
    }

    @IntegrationTest(value = "cauldron_water_two_thirds")
    public void testEmptyBucketDoesNothingOnTwoThirdsCauldron(IntegrationTestHelper helper) {
        ItemStack bucket = BucketTestUtils.getEmptyBucket();

        ActionResult<ItemStack> resultBucket = IntegrationTestUtils.useItem(helper, CAULDRON_POSITION, bucket);

        helper.assertAirAt(CAULDRON_POSITION.above(), "No fluid should have been placed above the cauldron");
        helper.assertTrue(() -> helper.getBlockState(CAULDRON_POSITION).getValue(CauldronBlock.LEVEL) == 2, "Two thirds of cauldron should be filled");
        helper.assertTrue(() -> BucketTestUtils.isEmptyBucket(resultBucket.getObject()), "Empty bucket should remain after using it on cauldron");
    }

    @IntegrationTest(value = "cauldron_water_full")
    public void testWaterBucketDoesNotFillFullCauldron(IntegrationTestHelper helper) {
        ItemStack bucket = BucketTestUtils.getFilledBucket(Fluids.WATER);

        ActionResult<ItemStack> resultBucket = IntegrationTestUtils.useItem(helper, CAULDRON_POSITION, bucket);

        helper.assertAirAt(CAULDRON_POSITION.above(), "No fluid should have been placed above the cauldron");
        helper.assertTrue(() -> helper.getBlockState(CAULDRON_POSITION).getValue(CauldronBlock.LEVEL) == 3, "Cauldron should be filled completely");
        helper.assertTrue(() -> BucketTestUtils.bucketContainsFluid(resultBucket.getObject(), Fluids.WATER), "Filled water bucket should remain after trying to fill cauldron");
    }

    @IntegrationTest(value = "cauldron_water_full")
    public void testEmptyBucketEmptiesFullCauldron(IntegrationTestHelper helper) {
        ItemStack bucket = BucketTestUtils.getEmptyBucket();

        ActionResult<ItemStack> resultBucket = IntegrationTestUtils.useItem(helper, CAULDRON_POSITION, bucket);

        helper.assertAirAt(CAULDRON_POSITION.above(), "No fluid should have been placed above the cauldron");
        helper.assertTrue(() -> helper.getBlockState(CAULDRON_POSITION).getValue(CauldronBlock.LEVEL) == 0, "Cauldron should be empty after emptying it");
        helper.assertTrue(() -> BucketTestUtils.bucketContainsFluid(resultBucket.getObject(), Fluids.WATER), "Filled water bucket should remain after emptying cauldron");
    }

    //TODO Fish Bucket

}
