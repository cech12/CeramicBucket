package cech12.ceramicbucket.init;

import cech12.ceramicbucket.api.item.CeramicBucketItems;
import cech12.ceramicbucket.item.CeramicBucketItem;
import cech12.ceramicbucket.item.CeramicEntityBucketItem;
import cech12.ceramicbucket.item.CeramicMilkBucketItem;
import cech12.ceramicbucket.item.FilledCeramicBucketItem;
import cech12.ceramicbucket.util.CeramicBucketUtils;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.BlockSource;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;

import static cech12.ceramicbucket.CeramicBucketMod.MOD_ID;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

@Mod.EventBusSubscriber(modid= MOD_ID, bus= Mod.EventBusSubscriber.Bus.MOD)
public class ModItems {

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        CeramicBucketItems.UNFIRED_CLAY_BUCKET = registerItem("unfired_clay_bucket", new Item((new Item.Properties()).tab(CreativeModeTab.TAB_MISC)));
        CeramicBucketItems.CERAMIC_BUCKET = registerItem("ceramic_bucket", new CeramicBucketItem((new Item.Properties()).stacksTo(16).tab(CreativeModeTab.TAB_MISC)));
        CeramicBucketItems.FILLED_CERAMIC_BUCKET = registerItem("filled_ceramic_bucket", new FilledCeramicBucketItem((new Item.Properties()).stacksTo(1).tab(CreativeModeTab.TAB_MISC)));
        CeramicBucketItems.CERAMIC_MILK_BUCKET = registerItem("ceramic_milk_bucket", new CeramicMilkBucketItem((new Item.Properties()).stacksTo(1).tab(CreativeModeTab.TAB_MISC)));

        CeramicBucketItems.CERAMIC_ENTITY_BUCKET = registerItem("ceramic_entity_bucket", new CeramicEntityBucketItem((new Item.Properties()).stacksTo(1).tab(CreativeModeTab.TAB_MISC)));

        //dispense behaviour empty bucket
        DispenserBlock.registerBehavior(CeramicBucketItems.CERAMIC_BUCKET, new DefaultDispenseItemBehavior() {
            private final DefaultDispenseItemBehavior dispenseBehavior = new DefaultDispenseItemBehavior();

            /**
             * Dispense the specified stack, play the dispense sound and spawn particles.
             */
            @Nonnull
            public ItemStack execute(@Nonnull BlockSource source, @Nonnull ItemStack stack) {
                LevelAccessor levelaccessor = source.getLevel();
                BlockPos blockpos = source.getPos().relative(source.getBlockState().getValue(DispenserBlock.FACING));
                BlockState blockstate = levelaccessor.getBlockState(blockpos);
                Block block = blockstate.getBlock();
                if (block instanceof BucketPickup) {
                    ItemStack itemstack = ((BucketPickup)block).pickupBlock(levelaccessor, blockpos, blockstate);
                    ItemStack bucket = CeramicBucketUtils.getFilledCeramicBucket(itemstack, stack);
                    if (bucket == null) {
                        return super.execute(source, stack);
                    } else {
                        levelaccessor.gameEvent(null, GameEvent.FLUID_PICKUP, blockpos);
                        stack.shrink(1);
                        if (stack.isEmpty()) {
                            return bucket;
                        } else {
                            if (source.<DispenserBlockEntity>getEntity().addItem(bucket) < 0) {
                                this.dispenseBehavior.dispense(source, bucket);
                            }
                            return stack;
                        }
                    }
                } else {
                    return super.execute(source, stack);
                }
            }
        });

        //dispense behaviour filled buckets
        DispenseItemBehavior idispenseitembehavior = new DefaultDispenseItemBehavior() {
            private final DefaultDispenseItemBehavior dispenseBehaviour = new DefaultDispenseItemBehavior();

            /**
             * Dispense the specified stack, play the dispense sound and spawn particles.
             */
            @Nonnull
            public ItemStack execute(@Nonnull BlockSource source, @Nonnull ItemStack stack) {
                FilledCeramicBucketItem bucketItem = (FilledCeramicBucketItem)stack.getItem();
                BlockPos blockpos = source.getPos().relative(source.getBlockState().getValue(DispenserBlock.FACING));
                Level level = source.getLevel();
                if (bucketItem.tryPlaceContainedLiquid(null, level, blockpos, null, stack)) {
                    bucketItem.checkExtraContent(null, level, stack, blockpos);
                    return bucketItem.internalGetEmptySuccessItem(stack, null);
                } else {
                    return this.dispenseBehaviour.dispense(source, stack);
                }
            }
        };
        DispenserBlock.registerBehavior(CeramicBucketItems.FILLED_CERAMIC_BUCKET, idispenseitembehavior);
        DispenserBlock.registerBehavior(CeramicBucketItems.CERAMIC_MILK_BUCKET, idispenseitembehavior);
        DispenserBlock.registerBehavior(CeramicBucketItems.CERAMIC_ENTITY_BUCKET, idispenseitembehavior);
    }

    private static Item registerItem(String name, Item item) {
        item.setRegistryName(name);
        ForgeRegistries.ITEMS.register(item);
        return item;
    }

}
