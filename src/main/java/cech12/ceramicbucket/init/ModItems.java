package cech12.ceramicbucket.init;

import cech12.ceramicbucket.api.item.CeramicBucketItems;
import cech12.ceramicbucket.item.CeramicBucketItem;
import cech12.ceramicbucket.item.FilledCeramicBucketItem;
import net.minecraft.item.*;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import static cech12.ceramicbucket.CeramicBucketMod.MOD_ID;

@Mod.EventBusSubscriber(modid= MOD_ID, bus= Mod.EventBusSubscriber.Bus.MOD)
public class ModItems {

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        CeramicBucketItems.UNFIRED_CLAY_BUCKET = registerItem("unfired_clay_bucket", new Item((new Item.Properties()).group(ItemGroup.MISC)));
        CeramicBucketItems.CERAMIC_BUCKET = registerItem("ceramic_bucket", new CeramicBucketItem((new Item.Properties()).maxStackSize(16).group(ItemGroup.MISC)));
        CeramicBucketItems.FILLED_CERAMIC_BUCKET = registerItem("filled_ceramic_bucket", new FilledCeramicBucketItem((new Item.Properties()).maxStackSize(1).group(ItemGroup.MISC)));
        /*
        CeramicBucketItems.CERAMIC_LAVA_BUCKET = registerItem("ceramic_lava_bucket", new CeramicBucketItem(Fluids.LAVA.delegate, (new Item.Properties()).containerItem(CeramicBucketItems.CERAMIC_BUCKET).maxStackSize(1).group(ItemGroup.MISC)));
        CeramicBucketItems.CERAMIC_MILK_BUCKET = registerItem("ceramic_milk_bucket", new CeramicMilkBucketItem((new Item.Properties()).containerItem(CeramicBucketItems.CERAMIC_BUCKET).maxStackSize(1).group(ItemGroup.MISC)));
        CeramicBucketItems.CERAMIC_WATER_BUCKET = registerItem("ceramic_water_bucket", new CeramicBucketItem(Fluids.WATER.delegate, (new Item.Properties()).containerItem(CeramicBucketItems.CERAMIC_BUCKET).maxStackSize(1).group(ItemGroup.MISC)));

        CeramicBucketItems.PUFFERFISH_CERAMIC_BUCKET = registerItem("pufferfish_ceramic_bucket", new CeramicFishBucketItem(EntityType.PUFFERFISH, Fluids.WATER.delegate, (new Item.Properties()).maxStackSize(1).group(ItemGroup.MISC)));
        CeramicBucketItems.SALMON_CERAMIC_BUCKET = registerItem("salmon_ceramic_bucket", new CeramicFishBucketItem(EntityType.SALMON, Fluids.WATER.delegate, (new Item.Properties()).maxStackSize(1).group(ItemGroup.MISC)));
        CeramicBucketItems.COD_CERAMIC_BUCKET = registerItem("cod_ceramic_bucket", new CeramicFishBucketItem(EntityType.COD, Fluids.WATER.delegate, (new Item.Properties()).maxStackSize(1).group(ItemGroup.MISC)));
        CeramicBucketItems.TROPICAL_FISH_CERAMIC_BUCKET = registerItem("tropical_fish_ceramic_bucket", new CeramicFishBucketItem(EntityType.TROPICAL_FISH, Fluids.WATER.delegate, (new Item.Properties()).maxStackSize(1).group(ItemGroup.MISC)));
         */
        //dispense behaviour empty bucket
        /*
        DispenserBlock.registerDispenseBehavior(CeramicBucketItems.CERAMIC_BUCKET, new DefaultDispenseItemBehavior() {
            private final DefaultDispenseItemBehavior dispenseBehavior = new DefaultDispenseItemBehavior();

            /**
             * Dispense the specified stack, play the dispense sound and spawn particles.
             /
            public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
                IWorld iworld = source.getWorld();
                BlockPos blockpos = source.getBlockPos().offset(source.getBlockState().get(DispenserBlock.FACING));
                BlockState blockstate = iworld.getBlockState(blockpos);
                Block block = blockstate.getBlock();
                if (block instanceof IBucketPickupHandler) {
                    Fluid fluid = ((IBucketPickupHandler)block).pickupFluid(iworld, blockpos, blockstate);
                    if (!(fluid instanceof FlowingFluid)) {
                        return super.dispenseStack(source, stack);
                    } else {
                        Item item = fluid.getFilledBucket();
                        //Ceramic conversion
                        if (stack.getItem() == CeramicBucketItems.CERAMIC_BUCKET) {
                            item = ((CeramicBucketItem) stack.getItem()).getCeramicVariant(item);
                        }
                        stack.shrink(1);
                        if (stack.isEmpty()) {
                            return new ItemStack(item);
                        } else {
                            if (source.<DispenserTileEntity>getBlockTileEntity().addItemStack(new ItemStack(item)) < 0) {
                                this.dispenseBehavior.dispense(source, new ItemStack(item));
                            }

                            return stack;
                        }
                    }
                } else {
                    return super.dispenseStack(source, stack);
                }
            }
        });

        //dispense behaviour filled buckets
        IDispenseItemBehavior idispenseitembehavior = new DefaultDispenseItemBehavior() {
            private final DefaultDispenseItemBehavior dispenseBehaviour = new DefaultDispenseItemBehavior();

            /**
             * Dispense the specified stack, play the dispense sound and spawn particles.
             /
            public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
                BucketItem bucketitem = (BucketItem)stack.getItem();
                BlockPos blockpos = source.getBlockPos().offset(source.getBlockState().get(DispenserBlock.FACING));
                World world = source.getWorld();
                if (bucketitem.tryPlaceContainedLiquid((PlayerEntity)null, world, blockpos, (BlockRayTraceResult)null)) {
                    bucketitem.onLiquidPlaced(world, stack, blockpos);
                    //Ceramic conversion
                    if (stack.getItem() instanceof CeramicBucketItem) {
                        return ((CeramicBucketItem) stack.getItem()).emptyBucket(stack, null);
                    }
                    return new ItemStack(CeramicBucketItems.CERAMIC_BUCKET);
                } else {
                    return this.dispenseBehaviour.dispense(source, stack);
                }
            }
        };
        DispenserBlock.registerDispenseBehavior(CeramicBucketItems.CERAMIC_LAVA_BUCKET, idispenseitembehavior);
        DispenserBlock.registerDispenseBehavior(CeramicBucketItems.CERAMIC_WATER_BUCKET, idispenseitembehavior);
        DispenserBlock.registerDispenseBehavior(CeramicBucketItems.PUFFERFISH_CERAMIC_BUCKET, idispenseitembehavior);
        DispenserBlock.registerDispenseBehavior(CeramicBucketItems.SALMON_CERAMIC_BUCKET, idispenseitembehavior);
        DispenserBlock.registerDispenseBehavior(CeramicBucketItems.COD_CERAMIC_BUCKET, idispenseitembehavior);
        DispenserBlock.registerDispenseBehavior(CeramicBucketItems.TROPICAL_FISH_CERAMIC_BUCKET, idispenseitembehavior);

         */
    }

    private static Item registerItem(String name, Item item) {
        item.setRegistryName(name);
        ForgeRegistries.ITEMS.register(item);
        return item;
    }

}
