package cech12.ceramicbucket.init;

import cech12.ceramicbucket.api.item.CeramicBucketItems;
import cech12.ceramicbucket.item.CeramicBucketItem;
import cech12.ceramicbucket.item.CeramicMilkBucketItem;
import net.minecraft.fluid.Fluids;
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
        CeramicBucketItems.CERAMIC_BUCKET = registerItem("ceramic_bucket", new CeramicBucketItem(Fluids.EMPTY.delegate, (new Item.Properties()).maxStackSize(16).group(ItemGroup.MISC)));
        CeramicBucketItems.CERAMIC_LAVA_BUCKET = registerItem("ceramic_lava_bucket", new CeramicBucketItem(Fluids.LAVA.delegate, (new Item.Properties()).containerItem(CeramicBucketItems.CERAMIC_BUCKET).maxStackSize(1).group(ItemGroup.MISC)));
        CeramicBucketItems.CERAMIC_MILK_BUCKET = registerItem("ceramic_milk_bucket", new CeramicMilkBucketItem((new Item.Properties()).containerItem(CeramicBucketItems.CERAMIC_BUCKET).maxStackSize(1).group(ItemGroup.MISC)));
        CeramicBucketItems.CERAMIC_WATER_BUCKET = registerItem("ceramic_water_bucket", new CeramicBucketItem(Fluids.WATER.delegate, (new Item.Properties()).containerItem(CeramicBucketItems.CERAMIC_BUCKET).maxStackSize(1).group(ItemGroup.MISC)));
    }

    private static Item registerItem(String name, Item item) {
        item.setRegistryName(name);
        ForgeRegistries.ITEMS.register(item);
        return item;
    }

}
