package cech12.ceramicbucket.integration;

import cech12.ceramicbucket.BucketTestUtils;
import cech12.ceramicbucket.IntegrationTestUtils;
import com.alcatrazescapee.mcjunitlib.framework.IntegrationTest;
import com.alcatrazescapee.mcjunitlib.framework.IntegrationTestClass;
import com.alcatrazescapee.mcjunitlib.framework.IntegrationTestHelper;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;

@IntegrationTestClass(value = "world")
public class WorldTests {

    private static final BlockPos INTERACTION_POSITION = new BlockPos(1, 1, 1);

    //--------------------------------------
    //------------ Water Tests -------------
    //--------------------------------------

    @IntegrationTest(value = "fluid_pit")
    public void testBucketPlacesWater(IntegrationTestHelper helper) {
        ItemStack bucket = BucketTestUtils.getFilledBucket(Fluids.WATER);

        ActionResult<ItemStack> result = IntegrationTestUtils.useItem(helper, INTERACTION_POSITION, bucket);

        helper.assertFluidAt(INTERACTION_POSITION, Fluids.WATER, "Water should have been placed");
        helper.assertTrue(() -> BucketTestUtils.isEmptyBucket(result.getObject()), "Empty bucket should remain after placing water");
    }

    @IntegrationTest(value = "fluid_pit")
    public void testBucketPicksUpWater(IntegrationTestHelper helper) {
        IntegrationTestUtils.placeFluid(helper, INTERACTION_POSITION, Fluids.WATER);
        ItemStack bucket = BucketTestUtils.getEmptyBucket();

        ActionResult<ItemStack> result = IntegrationTestUtils.useItem(helper, INTERACTION_POSITION, bucket);

        helper.assertAirAt(INTERACTION_POSITION, "Water should have been picked up");
        helper.assertTrue(() -> BucketTestUtils.bucketContainsFluid(result.getObject(), Fluids.WATER), "Bucket should contain water after picking it up");
    }

    //--------------------------------------
    //------------ Lava Tests --------------
    //--------------------------------------

    @IntegrationTest(value = "fluid_pit")
    public void testBucketPlacesLava(IntegrationTestHelper helper) {
        ItemStack bucket = BucketTestUtils.getFilledBucket(Fluids.LAVA);

        ActionResult<ItemStack> result = IntegrationTestUtils.useItem(helper, INTERACTION_POSITION, bucket);

        helper.assertFluidAt(INTERACTION_POSITION, Fluids.LAVA, "Lava should have been placed");
        helper.assertTrue(() -> result.getObject().isEmpty(), "No bucket should remain after placing lava");
    }

    @IntegrationTest(value = "fluid_pit")
    public void testBucketPicksUpLava(IntegrationTestHelper helper) {
        IntegrationTestUtils.placeFluid(helper, INTERACTION_POSITION, Fluids.LAVA);
        ItemStack bucket = BucketTestUtils.getEmptyBucket();

        ActionResult<ItemStack> result = IntegrationTestUtils.useItem(helper, INTERACTION_POSITION, bucket);

        helper.assertAirAt(INTERACTION_POSITION, "Lava should have been picked up");
        helper.assertTrue(() -> BucketTestUtils.bucketContainsFluid(result.getObject(), Fluids.LAVA), "Bucket should contain lava after picking it up");
    }

    //--------------------------------------
    //-------- Empty Bucket Tests ----------
    //--------------------------------------

    @IntegrationTest(value = "fluid_pit")
    public void testNothingHappensUsingEmptyBucket(IntegrationTestHelper helper) {
        ItemStack bucket = BucketTestUtils.getEmptyBucket();

        ActionResult<ItemStack> result = IntegrationTestUtils.useItem(helper, INTERACTION_POSITION, bucket);

        helper.assertAirAt(INTERACTION_POSITION, "No fluid is placed");
        helper.assertTrue(() -> BucketTestUtils.isEmptyBucket(result.getObject()), "Empty bucket should remain");
    }


}
