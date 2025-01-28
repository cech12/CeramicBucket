package de.cech12.ceramicbucket;

import de.cech12.bucketlib.api.BucketLibApi;
import de.cech12.bucketlib.api.item.UniversalBucketItem;
import de.cech12.ceramicbucket.init.ModTags;
import de.cech12.ceramicbucket.platform.Services;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;

@SuppressWarnings("unused")
public class CeramicBucketMod implements ModInitializer {

    public static final ResourceLocation UNFIRED_CLAY_BUCKET_LOCATION = Constants.id(Constants.UNFIRED_CLAY_BUCKET_NAME);
    public static final ResourceLocation CERAMIC_BUCKET_LOCATION = Constants.id(Constants.CERAMIC_BUCKET_NAME);

    public static final Item UNFIRED_CLAY_BUCKET = Registry.register(BuiltInRegistries.ITEM, UNFIRED_CLAY_BUCKET_LOCATION, new Item(new Item.Properties().setId(ResourceKey.create(BuiltInRegistries.ITEM.key(), UNFIRED_CLAY_BUCKET_LOCATION))));
    public static final Item CERAMIC_BUCKET = Registry.register(BuiltInRegistries.ITEM, CERAMIC_BUCKET_LOCATION, new UniversalBucketItem(
            ResourceKey.create(BuiltInRegistries.ITEM.key(), CERAMIC_BUCKET_LOCATION),
            new UniversalBucketItem.Properties()
                    .upperCrackingTemperature(Services.CONFIG::getBreakTemperature)
                    .crackingFluids(ModTags.Fluids.CERAMIC_CRACKING)
                    .milking(Services.CONFIG::isMilkingEnabled)
                    .entityObtaining(Services.CONFIG::isFishObtainingEnabled)
                    .dyeable(ARGB.color(255, 14975336))
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
