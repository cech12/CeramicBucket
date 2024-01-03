package de.cech12.ceramicbucket.integration;

/*
import cech12.ceramicbucket.BucketTestUtils;
import cech12.ceramicbucket.IntegrationTestUtils;
import com.alcatrazescapee.mcjunitlib.framework.IntegrationTest;
import com.alcatrazescapee.mcjunitlib.framework.IntegrationTestClass;
import com.alcatrazescapee.mcjunitlib.framework.IntegrationTestHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.fish.SalmonEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

@IntegrationTestClass(value = "dispenser")
public class DispenserTests {

    private static final BlockPos BUTTON_POSITION = new BlockPos(0, 1, 0);
    private static final BlockPos DISPENSER_POSITION = new BlockPos(0, 1, 1);
    private static final BlockPos DISPENSER_INTERACTION_POSITION = new BlockPos(1, 1, 1);

    //--------------------------------------
    //------------ Water Tests -------------
    //--------------------------------------

    @IntegrationTest(value = "empty_dispenser")
    public void testDispenserPlacesWater(IntegrationTestHelper helper) {
        BucketTestUtils.addBucketToDispenser(helper, DISPENSER_POSITION, Fluids.WATER);

        helper.pushButton(BUTTON_POSITION);

        helper.assertFluidAt(DISPENSER_INTERACTION_POSITION, Fluids.WATER, "Water should have been dispensed");
        helper.assertTrue(() -> {
            ItemStack dispenserItemStack = BucketTestUtils.getFirstItemFromDispenser(helper, DISPENSER_POSITION);
            return BucketTestUtils.isEmptyBucket(dispenserItemStack);
        }, "Empty bucket should have been remain in dispenser");
    }

    @IntegrationTest(value = "empty_dispenser")
    public void testDispenserPicksUpWater(IntegrationTestHelper helper) {
        IntegrationTestUtils.placeFluid(helper, DISPENSER_INTERACTION_POSITION, Fluids.WATER);
        BucketTestUtils.addBucketToDispenser(helper, DISPENSER_POSITION);

        helper.pushButton(BUTTON_POSITION);

        helper.assertAirAt(DISPENSER_INTERACTION_POSITION, "Water should have been picked up");
        helper.assertTrue(() -> {
            ItemStack dispenserItemStack = BucketTestUtils.getFirstItemFromDispenser(helper, DISPENSER_POSITION);
            return BucketTestUtils.bucketContainsFluid(dispenserItemStack, Fluids.WATER);
        }, "Bucket in dispenser should contain water");
    }

    //--------------------------------------
    //------------ Lava Tests --------------
    //--------------------------------------

    @IntegrationTest(value = "empty_dispenser")
    public void testDispenserPlacesLava(IntegrationTestHelper helper) {
        BucketTestUtils.addBucketToDispenser(helper, DISPENSER_POSITION, Fluids.LAVA);

        helper.pushButton(BUTTON_POSITION);

        helper.assertFluidAt(DISPENSER_INTERACTION_POSITION, Fluids.LAVA, "Lava should have been dispensed");
        helper.assertTrue(() -> {
            ItemStack dispenserItemStack = BucketTestUtils.getFirstItemFromDispenser(helper, DISPENSER_POSITION);
            return dispenserItemStack.isEmpty();
        }, "No bucket should have been remain in dispenser");
    }

    @IntegrationTest(value = "empty_dispenser")
    public void testDispenserPicksUpLava(IntegrationTestHelper helper) {
        IntegrationTestUtils.placeFluid(helper, DISPENSER_INTERACTION_POSITION, Fluids.LAVA);
        BucketTestUtils.addBucketToDispenser(helper, DISPENSER_POSITION);

        helper.pushButton(BUTTON_POSITION);

        helper.assertAirAt(DISPENSER_INTERACTION_POSITION, "Lava should have been picked up");
        helper.assertTrue(() -> {
            ItemStack dispenserItemStack = BucketTestUtils.getFirstItemFromDispenser(helper, DISPENSER_POSITION);
            return BucketTestUtils.bucketContainsFluid(dispenserItemStack, Fluids.LAVA);
        }, "Bucket in dispenser should contain lava");
    }

    //--------------------------------------
    //-------- Empty Bucket Tests ----------
    //--------------------------------------

    @IntegrationTest(value = "empty_dispenser")
    public void testDispenserDispenseEmptyBucket(IntegrationTestHelper helper) {
        BucketTestUtils.addBucketToDispenser(helper, DISPENSER_POSITION);

        helper.pushButton(BUTTON_POSITION);

        helper.assertAirAt(DISPENSER_INTERACTION_POSITION, "No fluid should have been dispensed");
        helper.assertTrue(() -> {
            ItemStack dispenserItemStack = BucketTestUtils.getFirstItemFromDispenser(helper, DISPENSER_POSITION);
            return dispenserItemStack.isEmpty();
        }, "No bucket should have been remain in dispenser");
        helper.assertTrue(() -> {
            Entity entity = IntegrationTestUtils.getEntity(helper, DISPENSER_INTERACTION_POSITION);
            return entity instanceof ItemEntity && BucketTestUtils.isEmptyBucket(((ItemEntity) entity).getItem());
        }, "An empty bucket item entity should lie in front of the dispenser");
    }

    //--------------------------------------
    //--------- Milk Bucket Tests ----------
    //--------------------------------------
    // Milking entities with dispensers does not work with vanilla buckets
    // So, there is no need to implement corresponding tests

    @IntegrationTest(value = "empty_dispenser")
    public void testDispenserDispenseMilkBucket(IntegrationTestHelper helper) {
        BucketTestUtils.addItemStackToDispenser(helper, DISPENSER_POSITION, BucketTestUtils.getMilkBucket());

        helper.pushButton(BUTTON_POSITION);

        helper.assertAirAt(DISPENSER_INTERACTION_POSITION, "No fluid should have been dispensed");
        helper.assertTrue(() -> {
            ItemStack dispenserItemStack = BucketTestUtils.getFirstItemFromDispenser(helper, DISPENSER_POSITION);
            return dispenserItemStack.isEmpty();
        }, "No bucket should have been remain in dispenser");
        helper.assertTrue(() -> {
            Entity entity = IntegrationTestUtils.getEntity(helper, DISPENSER_INTERACTION_POSITION);
            return entity instanceof ItemEntity && BucketTestUtils.isMilkBucket(((ItemEntity) entity).getItem());
        }, "A milk bucket item entity should lie in front of the dispenser");
    }

    //--------------------------------------
    //--------- Fish Bucket Tests ----------
    //--------------------------------------
    // Pickup fish entities with dispensers does not work with vanilla buckets
    // So, there is no need to implement corresponding tests

    @IntegrationTest(value = "empty_dispenser")
    public void testDispenserDispenseFish(IntegrationTestHelper helper) {
        BucketTestUtils.addItemStackToDispenser(helper, DISPENSER_POSITION, BucketTestUtils.getFilledEntityBucket(Fluids.WATER, EntityType.SALMON.create(helper.getWorld())));

        helper.pushButton(BUTTON_POSITION);

        helper.assertFluidAt(DISPENSER_INTERACTION_POSITION, Fluids.WATER, "Water should have been dispensed");
        helper.assertTrue(() -> {
            ItemStack dispenserItemStack = BucketTestUtils.getFirstItemFromDispenser(helper, DISPENSER_POSITION);
            return BucketTestUtils.isEmptyBucket(dispenserItemStack);
        }, "Empty bucket should have been remain in dispenser");
        helper.assertTrue(() -> {
            Entity entity = IntegrationTestUtils.getEntity(helper, DISPENSER_INTERACTION_POSITION);
            return entity instanceof SalmonEntity && entity.isAlive();
        }, "A salmon entity should be in front of the dispenser");
    }

}
 */
