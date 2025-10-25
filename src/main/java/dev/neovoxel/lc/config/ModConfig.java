package dev.neovoxel.lc.config;

import dev.neovoxel.lc.type.PositionType;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "location-clipboard")
public class ModConfig implements ConfigData {
    @ConfigEntry.Gui.CollapsibleObject
    public General general = new General();

    @ConfigEntry.Gui.CollapsibleObject
    public Data data = new Data();

    public static class General {
        @ConfigEntry.Gui.PrefixText
        @ConfigEntry.Gui.Tooltip
        public String format = "/tp ${player} ${x} ${y} ${z}";

        @ConfigEntry.Gui.Tooltip
        public PositionType positionType = PositionType.PLAYER;


        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int targetedMaxDistance = 20;
    }

    public static class Data {
        @ConfigEntry.Gui.PrefixText
        @ConfigEntry.BoundedDiscrete(min = 0, max = 14)
        public int xPrecision = 14;

        @ConfigEntry.BoundedDiscrete(min = 0, max = 14)
        public int yPrecision = 14;

        @ConfigEntry.BoundedDiscrete(min = 0, max = 14)
        public int zPrecision = 14;

        @ConfigEntry.BoundedDiscrete(min = 0, max = 14)
        public int pitchPrecision = 14;

        @ConfigEntry.BoundedDiscrete(min = 0, max = 14)
        public int yawPrecision = 14;
    }
}
