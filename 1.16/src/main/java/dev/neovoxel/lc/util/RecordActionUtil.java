package dev.neovoxel.lc.util;

import dev.neovoxel.lc.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.loader.impl.util.log.Log;
import net.fabricmc.loader.impl.util.log.LogCategory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

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
        if (config.message.basic) {
            player.sendMessage(new TranslatableText("text.location-clipboard.message.record-start"), false);
        }
        if (config.message.logBasic) {
            Log.info(LogCategory.LOG, "Player started recording");
        }
    }

    public void record(boolean isPlayerExecuted) {
        if (isPlayerExecuted) {
            if (!config.record.autoRecord || config.record.autoRecordAndSelfRecord) {
                ClipboardActionUtil util = new ClipboardActionUtil(player);
                String content = util.copyContent();
                if (content != null) {
                    if (config.message.advanced) {
                        player.sendMessage(Text.of(new TranslatableText("text.location-clipboard.message.record")
                                .getString().replace("${content}", content)), false);
                    }
                    if (config.message.logAdvanced) {
                        Log.debug(LogCategory.LOG, "Recorded by player: " + content);
                    }
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
                if (config.message.advanced) {
                    player.sendMessage(Text.of(new TranslatableText("text.location-clipboard.message.auto-record")
                            .getString().replace("${content}", content)), false);
                }
                if (config.message.logAdvanced) {
                    Log.debug(LogCategory.LOG, "Recorded automatically: " + content);
                }
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
            content = EscapeUtil.unescapeAll(content);
        }
        MinecraftClient.getInstance().keyboard.setClipboard(content);
        if (config.message.basic) {
            player.sendMessage(Text.of(new TranslatableText("text.location-clipboard.message.record-stop")
                    .getString().replace("${content}", content)), false);
        }
        if (config.message.logBasic) {
            Log.info(LogCategory.LOG, "Player stopped recording, the final result is " + content);
        }
    }
}
