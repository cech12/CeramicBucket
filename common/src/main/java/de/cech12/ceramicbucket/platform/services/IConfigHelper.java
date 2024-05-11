package de.cech12.ceramicbucket.platform.services;

/**
 * Common configuration helper service interface.
 */
public interface IConfigHelper {

    /** Default value of break temperature */
    int BREAK_TEMPERATURE_DEFAULT = 1000;
    /** Config description of the break temperature */
    String BREAK_TEMPERATURE_DESCRIPTION = "Minimum temperature of fluid at which the Ceramic Bucket breaks when emptied. (default: " + BREAK_TEMPERATURE_DEFAULT + ") (Deactivation by setting a number larger than the hottest fluid)";
    /** Minimal value of the break temperature */
    int BREAK_TEMPERATURE_MIN = -10000;
    /** Maximal value of the break temperature */
    int BREAK_TEMPERATURE_MAX = 10000;
    /** Default value of durability */
    int DURABILITY_DEFAULT = 0;
    /** Config description of the durability */
    String DURABILITY_DESCRIPTION = "Defines the maximum durability of Ceramic Bucket. (default: " + DURABILITY_DEFAULT + ", 0: deactivates the durability)";
    /** Minimal value of the durability */
    int DURABILITY_MIN = 0;
    /** Maximal value of the durability */
    int DURABILITY_MAX = 10000;
    /** Default value of fish obtaining option */
    boolean FISH_OBTAINING_ENABLED_DEFAULT = true;
    /** Config description of the durability */
    String FISH_OBTAINING_ENABLED_DESCRIPTION = "Whether or not obtaining fish with a Ceramic Bucket should be enabled. (default: " + FISH_OBTAINING_ENABLED_DEFAULT + ")";
    /** Default value of milking option */
    boolean MILKING_ENABLED_DEFAULT = true;
    /** Config description of the milking option */
    String MILKING_ENABLED_DESCRIPTION = "Whether or not milking entities with a Ceramic Bucket should be enabled. (default: " + MILKING_ENABLED_DEFAULT + ")";


    /**
     * Initialization method for the Service implementations.
     */
    void init();

    /**
     * Gets the configured break temperature value.
     *
     * @return configured break temperature value
     */
    int getBreakTemperature();

    /**
     * Gets the configured durability value.
     *
     * @return configured durability value
     */
    int getDurability();

    /**
     * Gets the fish obtaining enabled value.
     *
     * @return configured fish obtaining enabled value
     */
    boolean isFishObtainingEnabled();

    /**
     * Gets the configured milking enabled value.
     *
     * @return configured milking enabled value
     */
    boolean isMilkingEnabled();

}