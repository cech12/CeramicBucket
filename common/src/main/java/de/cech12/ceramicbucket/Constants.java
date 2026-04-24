package de.cech12.ceramicbucket;

import net.minecraft.resources.Identifier;
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

    public static final String UNFIRED_CLAY_BUCKET_NAME = "unfired_clay_bucket";
    public static final String CERAMIC_BUCKET_NAME = "ceramic_bucket";

    private Constants() {}

    public static Identifier id(String name) {
        return Identifier.fromNamespaceAndPath(MOD_ID, name);
    }

}