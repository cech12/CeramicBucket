package de.cech12.ceramicbucket;

import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class that contains all common constants.
 */
public class Constants {

    /** mod id */
    public static final String MOD_ID = "ceramicbucket";
    /** mod name*/
    public static final String MOD_NAME = "Ceramic Bucket";
    /** Logger instance */
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

    private Constants() {}

    public static ResourceLocation id(String name) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
    }

}