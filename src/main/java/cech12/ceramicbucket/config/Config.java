package cech12.ceramicbucket.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.List;

public class Config {
    public static ForgeConfigSpec COMMON;

    public static List<IResettableConfigType> allValues = new ArrayList<>();

    public static final ConfigType.Integer CERAMIC_BUCKET_BREAK_TEMPERATURE = new ConfigType.Integer(1000);
    public static final ConfigType.Boolean FISH_OBTAINING_ENABLED = new ConfigType.Boolean(true);

    static {
        final ForgeConfigSpec.Builder common = new ForgeConfigSpec.Builder();

        common.push("Balance Options");

        CERAMIC_BUCKET_BREAK_TEMPERATURE.configObj = common
                .comment("Minimum temperature of fluid at which the Ceramic Bucket breaks when emptied. (-1 means that bucket never breaks caused by high fluid temperature)")
                .defineInRange("ceramicBucketBreakTemperature", CERAMIC_BUCKET_BREAK_TEMPERATURE.getDefaultValue(), -1, 10000);

        FISH_OBTAINING_ENABLED.configObj = common
                .comment("Whether or not obtaining fish with a Ceramic Bucket should be enabled.")
                .define("fishObtainingEnabled", FISH_OBTAINING_ENABLED.getDefaultValue());

        common.pop();

        COMMON = common.build();
    }

    public static void resetConfig() {
        for (IResettableConfigType par : allValues){
            par.reset();
        }
    }
}
