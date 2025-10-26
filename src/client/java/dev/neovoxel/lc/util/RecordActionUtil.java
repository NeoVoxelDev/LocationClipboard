package dev.neovoxel.lc.util;

import dev.neovoxel.lc.LocationClipboardClient;
import dev.neovoxel.lc.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class RecordActionUtil {
    private final ClientPlayerEntity player;
    private final ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    private static final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture future;
    private final StringBuilder stringBuilder = new StringBuilder();

    public RecordActionUtil(ClientPlayerEntity player) {
        this.player = player;
    }

    public void start() {
        future = executor.scheduleAtFixedRate(() -> record(false), 0,
                config.record.autoRecordPeriod, TimeUnit.SECONDS);
    }

    public void record(boolean isPlayerExecuted) {
        if (isPlayerExecuted) {
            if (!config.record.autoRecord || config.record.autoRecordAndSelfRecord) {
                ClipboardActionUtil util = new ClipboardActionUtil(player);
                String content = util.copyContent();
                if (content != null) {
                    stringBuilder.append(content).append(config.record.separator);
                }
            }
        } else {
            ClipboardActionUtil util = new ClipboardActionUtil(player);
            String content = util.copyContent();
            if (config.record.copyWithRecord) {
                MinecraftClient.getInstance().keyboard.setClipboard(content);
            }
            if (content != null) {
                stringBuilder.append(content).append(config.record.separator);
            }
        }
    }

    public void stop() {
        if (future != null) {
            future.cancel(true);
        }
        if (stringBuilder.isEmpty()) {
            return;
        }
        String content = stringBuilder.substring(0, stringBuilder.length() - config.record.separator.length());
        if (config.general.replaceEscapeChar) {
            content = StringEscapeUtils.unescapeJava(content);
        }
        MinecraftClient.getInstance().keyboard.setClipboard(content);
    }
}
