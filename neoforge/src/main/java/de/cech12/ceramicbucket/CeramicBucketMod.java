package de.cech12.ceramicbucket;

import de.cech12.bucketlib.api.BucketLibApi;
import de.cech12.bucketlib.api.item.UniversalBucketItem;
import de.cech12.ceramicbucket.init.ModTags;
import de.cech12.ceramicbucket.platform.Services;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ARGB;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(Constants.MOD_ID)
public class CeramicBucketMod {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Constants.MOD_ID);

    public static final DeferredItem<Item> UNFIRED_CLAY_BUCKET = ITEMS.register(Constants.UNFIRED_CLAY_BUCKET_NAME, () -> new Item(new Item.Properties().setId(ResourceKey.create(BuiltInRegistries.ITEM.key(), Constants.id(Constants.UNFIRED_CLAY_BUCKET_NAME)))));
    public static final DeferredItem<Item> CERAMIC_BUCKET = ITEMS.register(Constants.CERAMIC_BUCKET_NAME, () -> new UniversalBucketItem(
            ResourceKey.create(BuiltInRegistries.ITEM.key(), Constants.id(Constants.CERAMIC_BUCKET_NAME)),
            new UniversalBucketItem.Properties()
                    .upperCrackingTemperature(Services.CONFIG::getBreakTemperature)
                    .crackingFluids(ModTags.Fluids.CERAMIC_CRACKING)
                    .milking(Services.CONFIG::isMilkingEnabled)
                    .entityObtaining(Services.CONFIG::isFishObtainingEnabled)
                    .dyeable(ARGB.color(255, 14975336))
                    .durability(Services.CONFIG::getDurability)
    ));

    public CeramicBucketMod(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
        //listeners
        modEventBus.addListener(this::addItemsToTabs);
        //Config
        CommonLoader.init();
        //register for IMC event
        modEventBus.addListener(this::sendImc);
    }

    private void sendImc(RegisterCapabilitiesEvent evt) {
        BucketLibApi.registerBucket(evt, CERAMIC_BUCKET.getId());
    }

    private void addItemsToTabs(BuildCreativeModeTabContentsEvent event) {
        //CERAMIC_BUCKET is added by BucketLib
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(UNFIRED_CLAY_BUCKET);
        }
    }

}
