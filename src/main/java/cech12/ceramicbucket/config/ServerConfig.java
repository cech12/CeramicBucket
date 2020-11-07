package cech12.ceramicbucket.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;

import java.nio.file.Path;

public class ServerConfig {
    public static ForgeConfigSpec SERVER_CONFIG;

    public static final ForgeConfigSpec.IntValue CERAMIC_BUCKET_BREAK_TEMPERATURE;
    public static final ForgeConfigSpec.BooleanValue MILKING_ENABLED;
    public static final ForgeConfigSpec.BooleanValue FISH_OBTAINING_ENABLED;
    public static final ForgeConfigSpec.BooleanValue INFINITY_ENCHANTMENT_ENABLED;
    public static final ForgeConfigSpec.ConfigValue<String> INFINITY_ENCHANTMENT_FLUIDS;

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

        INFINITY_ENCHANTMENT_FLUIDS = builder
                .comment("If a ceramic bucket contains one of these fluids, it can be enchanted with Infinity (comma separated list). Example: \"water,minecraft:lava,othermod:other_fluid\"")
                .define("infinityEnchantmentFluids", "water");

        builder.pop();

        SERVER_CONFIG = builder.build();
    }

    public static void loadConfig(ForgeConfigSpec spec, Path path) {
        final CommentedFileConfig configData = CommentedFileConfig.builder(path).sync().autosave().writingMode(WritingMode.REPLACE).build();
        configData.load();
        spec.setConfig(configData);
    }

    public static boolean canFluidBeEnchantedWithInfinity(final Fluid fluid) {
        if (fluid == null) return false;
        String configValue = INFINITY_ENCHANTMENT_FLUIDS.get().trim();
        if (!configValue.isEmpty()) {
            String[] ids = configValue.split(",");
            if (ids.length < 1) {
                return new ResourceLocation(configValue).equals(fluid.getRegistryName());
            } else {
                for (String fluidId : ids) {
                    if (new ResourceLocation(fluidId.trim()).equals(fluid.getRegistryName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
