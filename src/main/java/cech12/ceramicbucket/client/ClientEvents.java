package cech12.ceramicbucket.client;

import cech12.ceramicbucket.CeramicBucketMod;
import cech12.ceramicbucket.client.model.CeramicBucketModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid= CeramicBucketMod.MOD_ID, bus= Mod.EventBusSubscriber.Bus.MOD, value= Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        ModelLoaderRegistry.registerLoader(new ResourceLocation(CeramicBucketMod.MOD_ID, "bucket"), CeramicBucketModel.Loader.INSTANCE);
    }
}
