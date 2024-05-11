package de.cech12.ceramicbucket;

import de.cech12.bucketlib.api.BucketLibApi;
import de.cech12.bucketlib.api.item.UniversalBucketItem;
import de.cech12.ceramicbucket.init.ModTags;
import de.cech12.ceramicbucket.platform.Services;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;

@SuppressWarnings("unused")
public class CeramicBucketMod implements ModInitializer {

    public static final ResourceLocation UNFIRED_CLAY_BUCKET_LOCATION = new ResourceLocation(Constants.MOD_ID, "unfired_clay_bucket");
    public static final ResourceLocation CERAMIC_BUCKET_LOCATION = new ResourceLocation(Constants.MOD_ID, "ceramic_bucket");

    public static final Item UNFIRED_CLAY_BUCKET = Registry.register(BuiltInRegistries.ITEM, UNFIRED_CLAY_BUCKET_LOCATION, new Item(new Item.Properties()));
    public static final Item CERAMIC_BUCKET = Registry.register(BuiltInRegistries.ITEM, CERAMIC_BUCKET_LOCATION, new UniversalBucketItem(new UniversalBucketItem.Properties()
            .upperCrackingTemperature(Services.CONFIG::getBreakTemperature)
            .crackingFluids(ModTags.Fluids.CERAMIC_CRACKING)
            .milking(Services.CONFIG::isMilkingEnabled)
            .entityObtaining(Services.CONFIG::isFishObtainingEnabled)
            .dyeable(14975336)
            .durability(Services.CONFIG::getDurability)
    ));

    @Override
    public void onInitialize() {
        CommonLoader.init();
        //register bucket
        BucketLibApi.registerBucket(CERAMIC_BUCKET_LOCATION);
        //register creative tab
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.INGREDIENTS).register(content -> {
            content.accept(UNFIRED_CLAY_BUCKET);
        });
    }

}
