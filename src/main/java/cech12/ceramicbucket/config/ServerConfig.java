package cech12.ceramicbucket.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;

import java.nio.file.Path;

public class ServerConfig {
    public static ForgeConfigSpec SERVER_CONFIG;

    public static final ForgeConfigSpec.IntValue CERAMIC_BUCKET_BREAK_TEMPERATURE;
    public static final ForgeConfigSpec.BooleanValue MILKING_ENABLED;
    public static final ForgeConfigSpec.BooleanValue FISH_OBTAINING_ENABLED;
    public static final ForgeConfigSpec.BooleanValue INFINITY_ENCHANTMENT_ENABLED;
    public static final ForgeConfigSpec.IntValue INFINITY_ENCHANTMENT_COST;

    static {
        final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.push("Balance Options");

        CERAMIC_BUCKET_BREAK_TEMPERATURE = builder
                .comment("Minimum temperature of fluid at which the Ceramic Bucket breaks when emptied. (-1 means that bucket never breaks caused by high fluid temperature)")
                .defineInRange("ceramicBucketBreakTemperature", 1000, -1, 10000);

        MILKING_ENABLED = builder
                .comment("Whether or not milking entities with a Ceramic Bucket should be enabled.")
                .define("milkingEnabled", true);

        FISH_OBTAINING_ENABLED = builder
                .comment("Whether or not obtaining fish with a Ceramic Bucket should be enabled.")
                .define("fishObtainingEnabled", true);

        INFINITY_ENCHANTMENT_ENABLED = builder
                .comment("Whether or not Infinity enchantment for Ceramic Buckets filled with multiplying sources like water should be enabled.")
                .define("infinityEnchantmentEnabled", false);

        INFINITY_ENCHANTMENT_COST = builder
                .comment("Level cost of enchanting filled Ceramic Buckets with Infinity enchantment. (if enabled)")
                .defineInRange("infinityEnchantmentCost", 10, 1, 1000);

        builder.pop();

        SERVER_CONFIG = builder.build();
    }

    public static void loadConfig(ForgeConfigSpec spec, Path path) {
        final CommentedFileConfig configData = CommentedFileConfig.builder(path).sync().autosave().writingMode(WritingMode.REPLACE).build();
        configData.load();
        spec.setConfig(configData);
    }

}
