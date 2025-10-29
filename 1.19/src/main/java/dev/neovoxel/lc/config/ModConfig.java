package dev.neovoxel.lc.config;

import dev.neovoxel.lc.type.PositionType;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "location-clipboard")
public class ModConfig implements ConfigData {
    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Gui.CollapsibleObject
    public General general = new General();

    @ConfigEntry.Gui.CollapsibleObject
    public Data data = new Data();

    @ConfigEntry.Gui.CollapsibleObject
    public Record record = new Record();

    @ConfigEntry.Gui.CollapsibleObject
    public Message message = new Message();

    public static class General {
        @ConfigEntry.Gui.Tooltip
        public boolean replaceEscapeChar = true;

        @ConfigEntry.Gui.Tooltip
        public String format = "/tp ${player} ${x} ${y} ${z}";

        @ConfigEntry.Gui.Tooltip
        public PositionType positionType = PositionType.PLAYER;

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

    public static class Record {
        @ConfigEntry.Gui.PrefixText
        public boolean enable = true;

        @ConfigEntry.Gui.Tooltip
        public boolean copyWithRecord = false;

        @ConfigEntry.Gui.Tooltip
        public boolean autoRecord = false;

        @ConfigEntry.Gui.Tooltip
        public boolean autoRecordAndSelfRecord = true;

        @ConfigEntry.Gui.Tooltip
        public int autoRecordPeriod = 1;

        @ConfigEntry.Gui.Tooltip
        public String separator = "\\n";
    }

    public static class Message {
        @ConfigEntry.Gui.Tooltip
        public boolean basic = true;

        @ConfigEntry.Gui.Tooltip
        public boolean advanced = false;

        @ConfigEntry.Gui.Tooltip
        public boolean logBasic = false;

        @ConfigEntry.Gui.Tooltip
        public boolean logAdvanced = false;
    }
}
