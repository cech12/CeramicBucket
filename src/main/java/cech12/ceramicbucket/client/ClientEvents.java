package cech12.ceramicbucket.client;

import cech12.ceramicbucket.CeramicBucketMod;
import cech12.ceramicbucket.api.data.ObtainableEntityType;
import cech12.ceramicbucket.client.model.CeramicBucketModel;
import cech12.ceramicbucket.client.model.CeramicEntityBucketModel;
import cech12.ceramicbucket.compat.ModCompat;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid= CeramicBucketMod.MOD_ID, bus= Mod.EventBusSubscriber.Bus.MOD, value= Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void clientSetup(ModelRegistryEvent event) {
        ModelLoaderRegistry.registerLoader(new ResourceLocation(CeramicBucketMod.MOD_ID, "bucket"), CeramicBucketModel.Loader.INSTANCE);
        ModelLoaderRegistry.registerLoader(new ResourceLocation(CeramicBucketMod.MOD_ID, "entity_bucket"), CeramicEntityBucketModel.Loader.INSTANCE);
    }

    @SubscribeEvent
    public static void onTextureStitch(TextureStitchEvent.Pre event){
        if (event.getMap().getTextureLocation().equals(PlayerContainer.LOCATION_BLOCKS_TEXTURE)) { //AtlasTexture.LOCATION_BLOCKS_TEXTURE
            for (ObtainableEntityType entityType : ModCompat.getObtainableEntityTypes()) {
                event.addSprite(CeramicEntityBucketModel.getEntityTexture(EntityType.getKey(entityType.getEntityType())));
            }
        }
    }
}
