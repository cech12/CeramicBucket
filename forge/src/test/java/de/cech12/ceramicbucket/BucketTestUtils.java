package de.cech12.ceramicbucket;

/*
import cech12.ceramicbucket.api.item.CeramicBucketItems;
import cech12.ceramicbucket.item.CeramicEntityBucketItem;
import cech12.ceramicbucket.item.CeramicMilkBucketItem;
import cech12.ceramicbucket.item.FilledCeramicBucketItem;
import cech12.ceramicbucket.util.CeramicBucketUtils;
import com.alcatrazescapee.mcjunitlib.framework.IntegrationTestHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.DispenserTileEntity;
import net.minecraft.util.math.BlockPos;

import java.util.Objects;

public class BucketTestUtils {

    public static final EntityType<?>[] FISH_ENTITY_TYPES = new EntityType[]{EntityType.COD, EntityType.PUFFERFISH, EntityType.SALMON, EntityType.TROPICAL_FISH};

    private BucketTestUtils() {
    }

    public static boolean isEmptyBucket(ItemStack bucket) {
        return !bucket.isEmpty() && bucket.getItem() == CeramicBucketItems.CERAMIC_BUCKET;
    }

    public static boolean isMilkBucket(ItemStack bucket) {
        return !bucket.isEmpty() && bucket.getItem() == CeramicBucketItems.CERAMIC_MILK_BUCKET;
    }

    public static boolean bucketContainsFluid(ItemStack bucket, Fluid fluid) {
        return !bucket.isEmpty()
                && bucket.getItem() instanceof FilledCeramicBucketItem
                && ((FilledCeramicBucketItem) bucket.getItem()).getFluid(bucket) == fluid;
    }

    public static boolean bucketContainsEntity(ItemStack bucket, EntityType<?> entityType) {
        return !bucket.isEmpty()
                && bucket.getItem() instanceof CeramicEntityBucketItem
                && ((CeramicEntityBucketItem) bucket.getItem()).getEntityTypeFromStack(bucket) == entityType;
    }

    public static ItemStack getEmptyBucket() {
        return new ItemStack(CeramicBucketItems.CERAMIC_BUCKET);
    }

    public static ItemStack getMilkBucket() {
        return ((CeramicMilkBucketItem)CeramicBucketItems.CERAMIC_MILK_BUCKET).getFilledInstance(getEmptyBucket());
    }

    public static ItemStack getFilledBucket(Fluid fluid) {
        return CeramicBucketUtils.getFilledCeramicBucket(fluid, getEmptyBucket());
    }

    public static ItemStack getFilledEntityBucket(Fluid fluid, Entity entity) {
        return ((CeramicEntityBucketItem)CeramicBucketItems.CERAMIC_ENTITY_BUCKET).getFilledInstance(fluid, entity, getEmptyBucket());
    }

    public static void addBucketToDispenser(IntegrationTestHelper helper, BlockPos pos) {
        addItemStackToDispenser(helper, pos, getEmptyBucket());
    }

    public static void addBucketToDispenser(IntegrationTestHelper helper, BlockPos pos, Fluid fluid) {
        ItemStack bucket = new ItemStack(CeramicBucketItems.CERAMIC_BUCKET);
        if (fluid != Fluids.EMPTY) {
            bucket = CeramicBucketUtils.getFilledCeramicBucket(fluid, bucket);
        }
        addItemStackToDispenser(helper, pos, bucket);
    }

    public static void addItemStackToDispenser(IntegrationTestHelper helper, BlockPos pos, ItemStack stack) {
        DispenserTileEntity tileEntity = (DispenserTileEntity) Objects.requireNonNull(helper.getTileEntity(pos));
        tileEntity.addItem(stack);
    }

    public static ItemStack getFirstItemFromDispenser(IntegrationTestHelper helper, BlockPos pos) {
        DispenserTileEntity tileEntity = (DispenserTileEntity) Objects.requireNonNull(helper.getTileEntity(pos));
        return tileEntity.getItem(0);
    }

}
 */
