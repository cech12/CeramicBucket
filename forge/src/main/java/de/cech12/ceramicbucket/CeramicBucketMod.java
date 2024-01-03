package de.cech12.ceramicbucket;

import de.cech12.bucketlib.api.BucketLibApi;
import de.cech12.bucketlib.api.item.UniversalBucketItem;
import de.cech12.ceramicbucket.init.ModTags;
import de.cech12.ceramicbucket.platform.Services;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.MissingMappingsEvent;
import net.minecraftforge.registries.RegistryObject;

import java.util.Arrays;
import java.util.List;

import static de.cech12.ceramicbucket.CeramicBucketMod.MOD_ID;

@Mod(MOD_ID)
@Mod.EventBusSubscriber(modid = MOD_ID)
public class CeramicBucketMod {

    public static final String MOD_ID = "ceramicbucket";

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);

    public static final RegistryObject<Item> UNFIRED_CLAY_BUCKET = ITEMS.register("unfired_clay_bucket", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CERAMIC_BUCKET = ITEMS.register("ceramic_bucket", () -> new UniversalBucketItem(
            new UniversalBucketItem.Properties()
                    .upperCrackingTemperature(Services.CONFIG::getBreakTemperature)
                    .crackingFluids(ModTags.Fluids.CERAMIC_CRACKING)
                    .milking(Services.CONFIG::isMilkingEnabled)
                    .entityObtaining(Services.CONFIG::isFishObtainingEnabled)
                    .dyeable(14975336)
                    .durability(Services.CONFIG::getDurability)
    ));

    private static final List<ResourceLocation> oldResourceLocations = Arrays.stream(new String[]{
            "filled_ceramic_bucket",
            "ceramic_milk_bucket",
            "ceramic_entity_bucket",
            "pufferfish_ceramic_bucket",
            "salmon_ceramic_bucket",
            "cod_ceramic_bucket",
            "tropical_fish_ceramic_bucket"
    }).map(oldId -> new ResourceLocation(MOD_ID, oldId)).toList();

    public CeramicBucketMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
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

    @SubscribeEvent
    public static void remapOldIds(MissingMappingsEvent event) {
        //to support old versions of this mod
        event.getMappings(ForgeRegistries.ITEMS.getRegistryKey(), MOD_ID).forEach(itemMapping -> {
            if (oldResourceLocations.stream().anyMatch(itemMapping.getKey()::equals)) {
                itemMapping.remap(CERAMIC_BUCKET.get());
            }
        });
    }

    private void addItemsToTabs(BuildCreativeModeTabContentsEvent event) {
        //CERAMIC_BUCKET is added by BucketLib
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(UNFIRED_CLAY_BUCKET);
        }
    }

}
