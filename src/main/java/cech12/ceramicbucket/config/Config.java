package cech12.ceramicbucket.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
    public static ForgeConfigSpec COMMON;

    public static final ForgeConfigSpec.IntValue CERAMIC_BUCKET_BREAK_TEMPERATURE;
    public static final ForgeConfigSpec.BooleanValue FISH_OBTAINING_ENABLED;

    static {
        final ForgeConfigSpec.Builder common = new ForgeConfigSpec.Builder();

        common.push("Balance Options");

        CERAMIC_BUCKET_BREAK_TEMPERATURE = common
                .comment("Minimum temperature of fluid at which the Ceramic Bucket breaks when emptied. (-1 means that bucket never breaks caused by high fluid temperature)")
                .defineInRange("ceramicBucketBreakTemperature", 1000, -1, 10000);

        FISH_OBTAINING_ENABLED = common
                .comment("Whether or not obtaining fish with a Ceramic Bucket should be enabled.")
                .define("fishObtainingEnabled", true);

        common.pop();

        COMMON = common.build();
    }

}
