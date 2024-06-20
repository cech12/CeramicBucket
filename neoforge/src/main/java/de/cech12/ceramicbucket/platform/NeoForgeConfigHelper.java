package de.cech12.ceramicbucket.platform;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import de.cech12.ceramicbucket.Constants;
import de.cech12.ceramicbucket.platform.services.IConfigHelper;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLConfig;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.nio.file.Path;

/**
 * The config service implementation for NeoForge.
 */
public class NeoForgeConfigHelper implements IConfigHelper {

    private static final ModConfigSpec SERVER_CONFIG;

    private static final ModConfigSpec.IntValue BREAK_TEMPERATURE;
    private static final ModConfigSpec.IntValue DURABILITY;
    private static final ModConfigSpec.BooleanValue FISH_OBTAINING_ENABLED;
    private static final ModConfigSpec.BooleanValue MILKING_ENABLED;

    static {
        final ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.push("Balance Options");

        BREAK_TEMPERATURE = builder
                .comment(BREAK_TEMPERATURE_DESCRIPTION)
                .defineInRange("crackingTemperature", BREAK_TEMPERATURE_DEFAULT, BREAK_TEMPERATURE_MIN, BREAK_TEMPERATURE_MAX);

        DURABILITY = builder
                .comment(DURABILITY_DESCRIPTION)
                .defineInRange("durability", DURABILITY_DEFAULT, DURABILITY_MIN, DURABILITY_MAX);

        FISH_OBTAINING_ENABLED = builder
                .comment(FISH_OBTAINING_ENABLED_DESCRIPTION)
                .define("fishObtainingEnabled", FISH_OBTAINING_ENABLED_DEFAULT);

        MILKING_ENABLED = builder
                .comment(MILKING_ENABLED_DESCRIPTION)
                .define("milkingEnabled", MILKING_ENABLED_DEFAULT);

        builder.pop();

        SERVER_CONFIG = builder.build();
    }

    @Override
    public void init() {
        ModLoadingContext.get().getActiveContainer().registerConfig(ModConfig.Type.SERVER, SERVER_CONFIG);
        Path path = FMLPaths.GAMEDIR.get().resolve(FMLConfig.defaultConfigPath()).resolve(Constants.MOD_ID + "-server.toml");
        final CommentedFileConfig configData = CommentedFileConfig.builder(path).sync().autosave().writingMode(WritingMode.REPLACE).build();
        configData.load();
        SERVER_CONFIG.setConfig(configData);
    }

    @Override
    public int getBreakTemperature() {
        try {
            return BREAK_TEMPERATURE.get();
        } catch (IllegalStateException ex) {
            return BREAK_TEMPERATURE_DEFAULT;
        }
    }

    @Override
    public int getDurability() {
        try {
            return DURABILITY.get();
        } catch (IllegalStateException ex) {
            return DURABILITY_DEFAULT;
        }
    }

    @Override
    public boolean isFishObtainingEnabled() {
        try {
            return FISH_OBTAINING_ENABLED.get();
        } catch (IllegalStateException ex) {
            return FISH_OBTAINING_ENABLED_DEFAULT;
        }
    }

    @Override
    public boolean isMilkingEnabled() {
        try {
            return MILKING_ENABLED.get();
        } catch (IllegalStateException ex) {
            return MILKING_ENABLED_DEFAULT;
        }
    }

}
