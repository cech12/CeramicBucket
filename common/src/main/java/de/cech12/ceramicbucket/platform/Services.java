package de.cech12.ceramicbucket.platform;

import de.cech12.ceramicbucket.Constants;
import de.cech12.ceramicbucket.platform.services.IConfigHelper;
import de.cech12.ceramicbucket.platform.services.IPlatformHelper;

import java.util.ServiceLoader;

/**
 * Service loaders are a built-in Java feature that allow us to locate implementations of an interface that vary from one
 * environment to another. In the context of MultiLoader we use this feature to access a mock API in the common code that
 * is swapped out for the platform specific implementation at runtime.
 */
public class Services {

    /** Platform instance */
    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);
    /** Config instance */
    public static final IConfigHelper CONFIG = load(IConfigHelper.class);

    /**
     * This code is used to load a service for the current environment. Your implementation of the service must be defined
     * manually by including a text file in META-INF/services named with the fully qualified class name of the service.
     * Inside the file you should write the fully qualified class name of the implementation to load for the platform. For
     * example our file on Forge points to ForgePlatformHelper while Fabric points to FabricPlatformHelper.
     * @param clazz Service class, which should be loaded.
     * @return service instance
     * @param <T> Type of service class
     */
    public static <T> T load(Class<T> clazz) {
        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        Constants.LOG.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }

    private Services() {}

}