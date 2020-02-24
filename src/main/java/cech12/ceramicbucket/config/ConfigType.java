package cech12.ceramicbucket.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigType {

    public static class Boolean implements IResettableConfigType {

        public ForgeConfigSpec.BooleanValue configObj;
        private boolean defaultValue;

        public Boolean(boolean defaultValueIn) {
            this.defaultValue = defaultValueIn;
            Config.allValues.add(this);
        }

        public boolean getDefaultValue() {
            return this.defaultValue;
        }

        public void setDefaultValue(boolean value) {
            this.defaultValue = value;
        }

        public boolean getValue() {

            if (this.configObj != null)
                return this.configObj.get();
            else
                return defaultValue;
        }

        public void reset() {
            this.configObj.set(this.defaultValue);
            this.configObj.save();
        }
    }

    public static class Integer implements IResettableConfigType {
        public ForgeConfigSpec.IntValue configObj;
        private int defaultValue;

        public Integer(int defaultValueIn) {
            this.defaultValue = defaultValueIn;
            Config.allValues.add(this);
        }

        public int getDefaultValue() {
            return this.defaultValue;
        }

        public void setDefaultValue(int value) {
            this.defaultValue = value;
        }

        public int getValue() {

            if (this.configObj != null)
                return this.configObj.get();
            else
                return defaultValue;
        }

        public void reset() {
            this.configObj.set(this.defaultValue);
            this.configObj.save();
        }
    }

    public static class Double implements IResettableConfigType {
        public ForgeConfigSpec.DoubleValue configObj;
        private double defaultValue;

        public Double(double defaultValueIn) {
            this.defaultValue = defaultValueIn;
            Config.allValues.add(this);
        }

        public double getDefaultValue() {
            return this.defaultValue;
        }

        public void setDefaultValue(double value) {
            this.defaultValue = value;
        }

        public double getValue() {

            if (this.configObj != null)
                return this.configObj.get();
            else
                return defaultValue;
        }

        public void reset() {
            this.configObj.set(this.defaultValue);
            this.configObj.save();
        }
    }

}
