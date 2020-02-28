package cech12.ceramicbucket.jei;

import cech12.ceramicbucket.CeramicBucketMod;
import cech12.ceramicbucket.api.item.CeramicBucketItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

@JeiPlugin
public class ModJEIPlugin implements IModPlugin {

    private static ResourceLocation ID = new ResourceLocation(CeramicBucketMod.MOD_ID, "jei_plugin");

    @Nonnull
    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registration.useNbtForSubtypes(CeramicBucketItems.FILLED_CERAMIC_BUCKET);
    }

}
