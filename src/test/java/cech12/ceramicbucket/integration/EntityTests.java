package cech12.ceramicbucket.integration;

import cech12.ceramicbucket.BucketTestUtils;
import cech12.ceramicbucket.IntegrationTestUtils;
import com.alcatrazescapee.mcjunitlib.framework.IntegrationTest;
import com.alcatrazescapee.mcjunitlib.framework.IntegrationTestClass;
import com.alcatrazescapee.mcjunitlib.framework.IntegrationTestHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
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

        ItemStack resultBucket = IntegrationTestUtils.useItemOnEntity(helper, entity, bucket);

        helper.assertAirAt(INTERACTION_POSITION, "No fluid should have been placed");
        helper.assertTrue(() -> BucketTestUtils.isMilkBucket(resultBucket), "Bucket should contain milk");
        helper.assertTrue(entity::isAlive, "Cow should remain after milking");
    }

    @IntegrationTest(value = "entity_pit")
    public void testBucketMilksAMooshroom(IntegrationTestHelper helper) {
        Entity entity = IntegrationTestUtils.placeEntity(helper, INTERACTION_POSITION, EntityType.MOOSHROOM);
        ItemStack bucket = BucketTestUtils.getEmptyBucket();

        ItemStack resultBucket = IntegrationTestUtils.useItemOnEntity(helper, entity, bucket);

        helper.assertAirAt(INTERACTION_POSITION, "No fluid should have been placed");
        helper.assertTrue(() -> BucketTestUtils.isMilkBucket(resultBucket), "Bucket should contain milk");
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
            ItemStack resultBucket = IntegrationTestUtils.useItemOnEntity(helper, entity, bucket);
            helper.assertTrue(() -> !entity.isAlive(), entityType + " should be removed after picking it up");
            helper.assertTrue(() -> BucketTestUtils.bucketContainsFluid(resultBucket, Fluids.WATER), "Bucket should contain water after picking " + entityType + " up");
            helper.assertTrue(() -> BucketTestUtils.bucketContainsEntity(resultBucket, entityType), "Bucket should contain " + entityType + " after picking it up");
        }
    }

}
