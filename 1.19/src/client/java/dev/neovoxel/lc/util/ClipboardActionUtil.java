package dev.neovoxel.lc.util;

import dev.neovoxel.lc.LocationClipboardClient;
import dev.neovoxel.lc.config.ModConfig;
import dev.neovoxel.lc.type.PositionType;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static dev.neovoxel.lc.LocationClipboard.MOD_ID;

public class ClipboardActionUtil {
    private final ClientPlayerEntity player;
    private final MinecraftClient client = MinecraftClient.getInstance();
    private final ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    private static final Logger logger = LoggerFactory.getLogger(MOD_ID);

    public ClipboardActionUtil(ClientPlayerEntity player) {
        this.player = player;
    }
    
    public String copyContent() {
        if (config.general.positionType == PositionType.PLAYER) {
            return copyPlayerLocation();
        } else if (config.general.positionType == PositionType.UNDERFOOT_BLOCK) {
            return copyUnderfootBlock();
        } else if (config.general.positionType == PositionType.TARGETED_BLOCK) {
            return copyTargetedBlock(true);
        } else if (config.general.positionType == PositionType.TARGETED_BLOCK_WITHOUT_FLUID) {
            return copyTargetedBlock(false);
        }
        return null;
    }

    public void copy() {
        if (LocationClipboardClient.recordActionUtil != null) {
            LocationClipboardClient.recordActionUtil.record(true);
            return;
        }
        String content = null;
        if (config.general.positionType == PositionType.PLAYER) {
            content = copyPlayerLocation();
        } else if (config.general.positionType == PositionType.UNDERFOOT_BLOCK) {
            content = copyUnderfootBlock();
        } else if (config.general.positionType == PositionType.TARGETED_BLOCK) {
            content = copyTargetedBlock(true);
        } else if (config.general.positionType == PositionType.TARGETED_BLOCK_WITHOUT_FLUID) {
            content = copyTargetedBlock(false);
        }
        if (content != null) {
            client.keyboard.setClipboard(content);
            if (config.message.logBasic) {
                logger.info("Player copy the position content: {}", content);
            }
            if (config.message.basic) {
                client.player.sendMessage(Text.of(Text.translatable("text.location-clipboard.message.copy")
                        .getString().replace("${content}", content)));
            }
        }
    }

    public String copyPlayerLocation() {
        String name = player.getName().getString();
        double x = player.getX();
        double y = player.getY();
        double z = player.getZ();
        double pitch = player.getPitch();
        double yaw = player.getYaw();
        String content = config.general.format
                .replace("${player}", name)
                .replace("${x}", String.format("%." + config.data.xPrecision + "f", x))
                .replace("${y}", String.format("%." + config.data.yPrecision + "f", y))
                .replace("${z}", String.format("%." + config.data.zPrecision + "f", z))
                .replace("${pitch}", String.format("%." + config.data.pitchPrecision + "f", pitch))
                .replace("${yaw}", String.format("%." + config.data.yawPrecision + "f", yaw));
        if (config.general.replaceEscapeChar) {
            content = EscapeUtil.unescapeAll(content);
        }
        return content;
    }

    public String copyUnderfootBlock() {
        BlockPos pos = player.getBlockPos();
        int x = pos.getX();
        int y = pos.getY() - 1;
        int z = pos.getZ();
        String content = config.general.format
                .replace("${player}", player.getName().getString())
                .replace("${x}", String.valueOf(x))
                .replace("${y}", String.valueOf(y))
                .replace("${z}", String.valueOf(z));
        if (config.general.replaceEscapeChar) {
            content = EscapeUtil.unescapeAll(content);
        }
        return content;
    }

    public String copyTargetedBlock(boolean withFluid) {
        Vec3d pos = player.raycast(config.general.targetedMaxDistance, 0.0F, withFluid).getPos();
        double x = pos.getX();
        double y = pos.getY();
        double z = pos.getZ();
        String content = config.general.format
                .replace("${player}", player.getName().getString())
                .replace("${x}", String.format("%." + config.data.xPrecision + "f", x))
                .replace("${y}", String.format("%." + config.data.yPrecision + "f", y))
                .replace("${z}", String.format("%." + config.data.zPrecision + "f", z));
        if (config.general.replaceEscapeChar) {
            content = EscapeUtil.unescapeAll(content);
        }
        return content;
    }
}
