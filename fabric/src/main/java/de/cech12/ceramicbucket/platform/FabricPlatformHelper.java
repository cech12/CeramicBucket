package de.cech12.ceramicbucket.platform;

import de.cech12.ceramicbucket.platform.services.IPlatformHelper;
import net.fabricmc.loader.api.FabricLoader;

/**
 * The platform service implementation for Fabric.
 */
public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

}
