package cech12.ceramicbucket.integration;

/*
import cech12.ceramicbucket.BucketTestUtils;
import cech12.ceramicbucket.IntegrationTestUtils;
import com.alcatrazescapee.mcjunitlib.framework.IntegrationTest;
import com.alcatrazescapee.mcjunitlib.framework.IntegrationTestClass;
import com.alcatrazescapee.mcjunitlib.framework.IntegrationTestHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;

@IntegrationTestClass(value = "entity")
public class EntityTests {

    private static final BlockPos INTERACTION_POSITION = new BlockPos(1, 1, 1);

    //--------------------------------------
    //------------ Milk Tests --------------
    //--------------------------------------

    @IntegrationTest(value = "entity_pit")
    public void testBucketMilksACow(IntegrationTestHelper helper) {
        Entity entity = IntegrationTestUtils.placeEntity(helper, INTERACTION_POSITION, EntityType.COW);
        ItemStack bucket = BucketTestUtils.getEmptyBucket();

        ActionResult<ItemStack> result = IntegrationTestUtils.useItemOnEntity(helper, entity, bucket);

        helper.assertTrue(() -> result.getResult() == ActionResultType.SUCCESS, "Entity interaction should be marked as successful");
        helper.assertAirAt(INTERACTION_POSITION, "No fluid should have been placed");
        helper.assertTrue(() -> BucketTestUtils.isMilkBucket(result.getObject()), "Bucket should contain milk");
        helper.assertTrue(entity::isAlive, "Cow should remain after milking");
    }

    @IntegrationTest(value = "entity_pit")
    public void testBucketMilksAMooshroom(IntegrationTestHelper helper) {
        Entity entity = IntegrationTestUtils.placeEntity(helper, INTERACTION_POSITION, EntityType.MOOSHROOM);
        ItemStack bucket = BucketTestUtils.getEmptyBucket();

        ActionResult<ItemStack> result = IntegrationTestUtils.useItemOnEntity(helper, entity, bucket);

        helper.assertTrue(() -> result.getResult() == ActionResultType.SUCCESS, "Entity interaction should be marked as successful");
        helper.assertAirAt(INTERACTION_POSITION, "No fluid should have been placed");
        helper.assertTrue(() -> BucketTestUtils.isMilkBucket(result.getObject()), "Bucket should contain milk");
        helper.assertTrue(entity::isAlive, "Mooshroom should remain after milking");
    }

    //--------------------------------------
    //------------ Fish Tests --------------
    //--------------------------------------

    @IntegrationTest(value = "entity_pit")
    public void testBucketPicksUpFish(IntegrationTestHelper helper) {
        IntegrationTestUtils.placeFluid(helper, INTERACTION_POSITION, Fluids.WATER);
        for (EntityType<?> entityType : BucketTestUtils.FISH_ENTITY_TYPES) {
            Entity entity = IntegrationTestUtils.placeEntity(helper, INTERACTION_POSITION, entityType);
            ItemStack bucket = BucketTestUtils.getFilledBucket(Fluids.WATER);

            ActionResult<ItemStack> result = IntegrationTestUtils.useItemOnEntity(helper, entity, bucket);

            helper.assertTrue(() -> result.getResult() == ActionResultType.SUCCESS, "Interaction with " + entityType + " should be marked as successful");
            helper.assertTrue(() -> !entity.isAlive(), entityType + " should be removed after picking it up");
            helper.assertTrue(() -> BucketTestUtils.bucketContainsFluid(result.getObject(), Fluids.WATER), "Bucket should contain water after picking " + entityType + " up");
            helper.assertTrue(() -> BucketTestUtils.bucketContainsEntity(result.getObject(), entityType), "Bucket should contain " + entityType + " after picking it up");
        }
    }

    @IntegrationTest(value = "entity_pit")
    public void testLavaBucketDoesNotPickUpFish(IntegrationTestHelper helper) {
        EntityType<?> entityType = BucketTestUtils.FISH_ENTITY_TYPES[0];
        Entity entity = IntegrationTestUtils.placeEntity(helper, INTERACTION_POSITION, entityType);
        ItemStack bucket = BucketTestUtils.getFilledBucket(Fluids.LAVA);

        ActionResult<ItemStack> result = IntegrationTestUtils.useItemOnEntity(helper, entity, bucket);

        helper.assertTrue(() -> result.getResult() == ActionResultType.PASS, "Entity interaction should pass to other interactions");
        helper.assertTrue(entity::isAlive, "Fish should not be picked up");
        helper.assertAirAt(INTERACTION_POSITION, "No fluid should be placed after trying to pick up fish");
        helper.assertTrue(() -> BucketTestUtils.bucketContainsFluid(result.getObject(), Fluids.LAVA), "Lava bucket should remain after trying to pick up fish");
    }

    @IntegrationTest(value = "entity_pit")
    public void testEmptyBucketDoesNotPickUpFish(IntegrationTestHelper helper) {
        EntityType<?> entityType = BucketTestUtils.FISH_ENTITY_TYPES[0];
        Entity entity = IntegrationTestUtils.placeEntity(helper, INTERACTION_POSITION, entityType);
        ItemStack bucket = BucketTestUtils.getEmptyBucket();

        ActionResult<ItemStack> result = IntegrationTestUtils.useItemOnEntity(helper, entity, bucket);

        helper.assertTrue(() -> result.getResult() == ActionResultType.PASS, "Entity interaction should pass to other interactions");
        helper.assertTrue(entity::isAlive, "Fish should not be picked up");
        helper.assertAirAt(INTERACTION_POSITION, "No fluid should be placed after trying to pick up fish");
        helper.assertTrue(() -> BucketTestUtils.isEmptyBucket(result.getObject()), "Bucket should be empty after trying to pick up fish");
    }

    @IntegrationTest(value = "entity_pit")
    public void testMilkBucketDoesNotPickUpFish(IntegrationTestHelper helper) {
        EntityType<?> entityType = BucketTestUtils.FISH_ENTITY_TYPES[0];
        Entity entity = IntegrationTestUtils.placeEntity(helper, INTERACTION_POSITION, entityType);
        ItemStack bucket = BucketTestUtils.getMilkBucket();

        ActionResult<ItemStack> result = IntegrationTestUtils.useItemOnEntity(helper, entity, bucket);

        helper.assertTrue(() -> result.getResult() == ActionResultType.PASS, "Entity interaction should pass to other interactions");
        helper.assertTrue(entity::isAlive, "Fish should not be picked up");
        helper.assertAirAt(INTERACTION_POSITION, "No fluid should be placed after trying to pick up fish");
        helper.assertTrue(() -> BucketTestUtils.isMilkBucket(result.getObject()), "Milk bucket should remain after trying to pick up fish");
    }

}
 */
