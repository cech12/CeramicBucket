package cech12.ceramicbucket;

import cech12.bucketlib.api.BucketLibApi;
import cech12.bucketlib.api.item.UniversalBucketItem;
import cech12.ceramicbucket.config.ServerConfig;
import cech12.ceramicbucket.init.ModTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLConfig;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.MissingMappingsEvent;
import net.minecraftforge.registries.RegistryObject;

import java.util.Arrays;
import java.util.List;

import static cech12.ceramicbucket.CeramicBucketMod.MOD_ID;

@Mod(MOD_ID)
@Mod.EventBusSubscriber(modid = MOD_ID)
public class CeramicBucketMod {

    public static final String MOD_ID = "ceramicbucket";

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);

    public static final RegistryObject<Item> UNFIRED_CLAY_BUCKET = ITEMS.register("unfired_clay_bucket", () -> new Item((new Item.Properties()).tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> CERAMIC_BUCKET = ITEMS.register("ceramic_bucket", () -> new UniversalBucketItem(
            new UniversalBucketItem.Properties()
                    .upperCrackingTemperature(ServerConfig.CERAMIC_BUCKET_BREAK_TEMPERATURE)
                    .crackingFluids(ModTags.Fluids.CERAMIC_CRACKING)
                    .milking(ServerConfig.MILKING_ENABLED)
                    .entityObtaining(ServerConfig.FISH_OBTAINING_ENABLED)
                    .dyeable(14975336)
                    .durability(ServerConfig.CERAMIC_BUCKET_DURABILITY)
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
        //Config
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ServerConfig.SERVER_CONFIG);
        ServerConfig.loadConfig(ServerConfig.SERVER_CONFIG, FMLPaths.GAMEDIR.get().resolve(FMLConfig.defaultConfigPath()).resolve(MOD_ID + "-server.toml"));
        //register for IMC event
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::sendImc);
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

}
