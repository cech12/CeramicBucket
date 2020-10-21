package cech12.ceramicbucket.client;

import cech12.ceramicbucket.CeramicBucketMod;
import cech12.ceramicbucket.api.data.ObtainableEntityType;
import cech12.ceramicbucket.client.model.CeramicBucketModel;
import cech12.ceramicbucket.client.model.CeramicEntityBucketModel;
import cech12.ceramicbucket.compat.ModCompat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry2;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid= CeramicBucketMod.MOD_ID, bus= Mod.EventBusSubscriber.Bus.MOD, value= Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void clientSetup(ModelRegistryEvent event) {
        ModelLoaderRegistry2.registerLoader(new ResourceLocation(CeramicBucketMod.MOD_ID, "bucket"), CeramicBucketModel.Loader.INSTANCE);
        ModelLoaderRegistry2.registerLoader(new ResourceLocation(CeramicBucketMod.MOD_ID, "entity_bucket"), CeramicEntityBucketModel.Loader.INSTANCE);
    }

    @SubscribeEvent
    public static void onTextureStitch(TextureStitchEvent.Pre event){
        if (event.getMap().getBasePath().equals("textures")) {
            for (ObtainableEntityType entityType : ModCompat.getObtainableEntityTypes()) {
                ResourceLocation entityTypeKey = ForgeRegistries.ENTITIES.getKey(entityType.getEntityType());
                if (entityTypeKey != null) {
                    event.addSprite(CeramicEntityBucketModel.getEntityTexture(entityTypeKey));
                }
            }
        }
    }
}
