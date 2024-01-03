package de.cech12.ceramicbucket;

import de.cech12.bucketlib.api.BucketLibApi;
import de.cech12.bucketlib.api.item.UniversalBucketItem;
import de.cech12.ceramicbucket.init.ModTags;
import de.cech12.ceramicbucket.platform.Services;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import static de.cech12.ceramicbucket.CeramicBucketMod.MOD_ID;

@Mod(MOD_ID)
public class CeramicBucketMod {

    public static final String MOD_ID = "ceramicbucket";

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MOD_ID);

    public static final DeferredItem<Item> UNFIRED_CLAY_BUCKET = ITEMS.register("unfired_clay_bucket", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> CERAMIC_BUCKET = ITEMS.register("ceramic_bucket", () -> new UniversalBucketItem(
            new UniversalBucketItem.Properties()
                    .upperCrackingTemperature(Services.CONFIG::getBreakTemperature)
                    .crackingFluids(ModTags.Fluids.CERAMIC_CRACKING)
                    .milking(Services.CONFIG::isMilkingEnabled)
                    .entityObtaining(Services.CONFIG::isFishObtainingEnabled)
                    .dyeable(14975336)
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

    private void sendImc(InterModEnqueueEvent evt) {
        BucketLibApi.registerBucket(CERAMIC_BUCKET.getId());
    }

    private void addItemsToTabs(BuildCreativeModeTabContentsEvent event) {
        //CERAMIC_BUCKET is added by BucketLib
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(UNFIRED_CLAY_BUCKET);
        }
    }

}
