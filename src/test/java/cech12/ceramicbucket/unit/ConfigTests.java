package cech12.ceramicbucket.unit;

/*
import cech12.ceramicbucket.BucketTestUtils;
import cech12.ceramicbucket.IntegrationTestUtils;
import cech12.ceramicbucket.config.ServerConfig;
import cech12.ceramicbucket.util.CeramicBucketUtils;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.Alphanumeric.class) //to avoid config interferences during parallel execution
public class ConfigTests {

    @Test
    public void testTemperatureConfig() {
        int waterTemperature = Fluids.WATER.getAttributes().getTemperature();
        ServerConfig.CERAMIC_BUCKET_BREAK_TEMPERATURE.set(waterTemperature);
        assertTrue(CeramicBucketUtils.isFluidTooHotForCeramicBucket(Fluids.WATER), "Water should be too hot for ceramic buckets with low temperature config");
        ServerConfig.CERAMIC_BUCKET_BREAK_TEMPERATURE.set(waterTemperature + 1);
        assertFalse(CeramicBucketUtils.isFluidTooHotForCeramicBucket(Fluids.WATER), "Water should not be too hot for ceramic buckets with high temperature config");

        int lavaTemperature = Fluids.LAVA.getAttributes().getTemperature();
        ServerConfig.CERAMIC_BUCKET_BREAK_TEMPERATURE.set(lavaTemperature);
        assertTrue(CeramicBucketUtils.isFluidTooHotForCeramicBucket(Fluids.LAVA), "Lava should be too hot for ceramic buckets with low temperature config");
        ServerConfig.CERAMIC_BUCKET_BREAK_TEMPERATURE.set(lavaTemperature + 1);
        assertFalse(CeramicBucketUtils.isFluidTooHotForCeramicBucket(Fluids.LAVA), "Lava should not be too hot for ceramic buckets with high temperature config");

        // Reset defaults
        ServerConfig.CERAMIC_BUCKET_BREAK_TEMPERATURE.set(ServerConfig.DEFAULT_CERAMIC_BUCKET_BREAK_TEMPERATURE);
        assertFalse(CeramicBucketUtils.isFluidTooHotForCeramicBucket(Fluids.WATER), "Water should not be too hot for ceramic buckets with default temperature config");
        assertTrue(CeramicBucketUtils.isFluidTooHotForCeramicBucket(Fluids.LAVA), "Lava should be too hot for ceramic buckets with default temperature config");
    }

    @Test
    public void testMilkingConfig() {
        final ServerWorld world = ServerLifecycleHooks.getCurrentServer().overworld();
        final Entity cow = EntityType.COW.create(world);

        ItemStack emptyBucket = BucketTestUtils.getEmptyBucket();
        ActionResult<ItemStack> actionResult;

        ServerConfig.MILKING_ENABLED.set(false);
        actionResult = IntegrationTestUtils.useItemOnEntity(world, cow, emptyBucket.copy());
        assertFalse(BucketTestUtils.isMilkBucket(actionResult.getObject()), "Cow should not be milkable with disabled config");

        ServerConfig.MILKING_ENABLED.set(true);
        actionResult = IntegrationTestUtils.useItemOnEntity(world, cow, emptyBucket.copy());
        assertTrue(BucketTestUtils.isMilkBucket(actionResult.getObject()), "Cow should be milkable with enabled config");
    }

    @Test
    public void testFishObtainingConfig() {
        final ServerWorld world = ServerLifecycleHooks.getCurrentServer().overworld();
        final Entity cod = EntityType.COD.create(world);

        ItemStack waterBucket = BucketTestUtils.getFilledBucket(Fluids.WATER);
        ActionResult<ItemStack> actionResult;

        ServerConfig.FISH_OBTAINING_ENABLED.set(false);
        actionResult = IntegrationTestUtils.useItemOnEntity(world, cod, waterBucket.copy());
        assertFalse(BucketTestUtils.bucketContainsEntity(actionResult.getObject(), EntityType.COD), "Fish should not be obtained with disabled config");

        ServerConfig.FISH_OBTAINING_ENABLED.set(true);
        actionResult = IntegrationTestUtils.useItemOnEntity(world, cod, waterBucket.copy());
        assertTrue(BucketTestUtils.bucketContainsEntity(actionResult.getObject(), EntityType.COD), "Fish should be obtained with disabled config");
    }

    @Test
    public void testInfinityEnchantmentConfig() {
        final ServerWorld world = ServerLifecycleHooks.getCurrentServer().overworld();

        ItemStack waterBucket = BucketTestUtils.getFilledBucket(Fluids.WATER);
        ItemStack emptyBucket = BucketTestUtils.getEmptyBucket();
        ItemStack lavaBucket = BucketTestUtils.getFilledBucket(Fluids.LAVA);
        ItemStack milkBucket = BucketTestUtils.getMilkBucket();
        ItemStack getFishBucket = BucketTestUtils.getFilledEntityBucket(Fluids.WATER, EntityType.COD.create(world));
        ItemStack enchantedWaterBucket = waterBucket.copy();
        enchantedWaterBucket.enchant(Enchantments.INFINITY_ARROWS, 1);
        ItemStack[] notAffectedBucketsWhenEnabled = new ItemStack[] {emptyBucket, lavaBucket, milkBucket, getFishBucket};
        ItemStack[] notAffectedBucketsWhenDisabled = new ItemStack[] {emptyBucket, lavaBucket, milkBucket, getFishBucket, waterBucket, enchantedWaterBucket};

        ServerConfig.INFINITY_ENCHANTMENT_ENABLED.set(true);
        assertTrue(waterBucket.canApplyAtEnchantingTable(Enchantments.INFINITY_ARROWS), "Ceramic Water Bucket should be enchantable with infinity enchantment with enabled config.");
        assertFalse(CeramicBucketUtils.isAffectedByInfinityEnchantment(waterBucket), "Ceramic Water Bucket should not be affected by infinity enchantment with enabled config.");
        assertFalse(enchantedWaterBucket.canApplyAtEnchantingTable(Enchantments.INFINITY_ARROWS), "Enchanted Ceramic Water Bucket should not be enchantable with infinity enchantment with enabled config.");
        assertTrue(CeramicBucketUtils.isAffectedByInfinityEnchantment(enchantedWaterBucket), "Enchanted Ceramic Water Bucket should be affected by infinity enchantment with enabled config.");
        for (ItemStack bucket : notAffectedBucketsWhenEnabled) {
            assertFalse(bucket.canApplyAtEnchantingTable(Enchantments.INFINITY_ARROWS), bucket + " should not be enchantable with infinity enchantment with enabled config.");
            assertFalse(CeramicBucketUtils.isAffectedByInfinityEnchantment(bucket), bucket + " should not be affected by infinity enchantment with enabled config.");
        }
        //fish should not be obtainable with an enchanted water bucket
        ActionResult<ItemStack> actionResult = IntegrationTestUtils.useItemOnEntity(world, EntityType.COD.create(world), enchantedWaterBucket.copy());
        assertTrue(enchantedWaterBucket.equals(actionResult.getObject(), false), "Fish should not be obtainable with an enchanted water bucket with enabled config");

        ServerConfig.INFINITY_ENCHANTMENT_ENABLED.set(false);
        for (ItemStack bucket : notAffectedBucketsWhenDisabled) {
            assertFalse(bucket.canApplyAtEnchantingTable(Enchantments.INFINITY_ARROWS), bucket + " should not be enchantable with infinity enchantment with disabled config.");
            assertFalse(CeramicBucketUtils.isAffectedByInfinityEnchantment(bucket), bucket + " should not be affected by infinity enchantment with disabled config.");
        }
        //fish should be obtainable with an enchanted water bucket
        actionResult = IntegrationTestUtils.useItemOnEntity(world, EntityType.COD.create(world), enchantedWaterBucket.copy());
        assertTrue(BucketTestUtils.bucketContainsEntity(actionResult.getObject(), EntityType.COD), "Fish should be obtainable with an enchanted water bucket with disabled config");
        //TODO Fix bug?!
        //assertEquals(0, EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, resultStack), "Bucket should not be enchanted with infinity enchantment after obtaining a fish with disabled config");
    }

}
 */
