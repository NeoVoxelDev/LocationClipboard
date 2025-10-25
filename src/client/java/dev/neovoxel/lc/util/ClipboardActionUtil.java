package dev.neovoxel.lc.util;

import dev.neovoxel.lc.config.ModConfig;
import dev.neovoxel.lc.type.PositionType;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class ClipboardActionUtil {
    private final ClientPlayerEntity player;
    private final MinecraftClient client = MinecraftClient.getInstance();
    private final ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

    public ClipboardActionUtil(ClientPlayerEntity player) {
        this.player = player;
    }

    public void copy() {
        if (config.general.positionType == PositionType.PLAYER) {
            copyPlayerLocation();
        } else if (config.general.positionType == PositionType.UNDERFOOT_BLOCK) {
            copyUnderfootBlock();
        } else if (config.general.positionType == PositionType.TARGETED_BLOCK) {
            copyTargetedBlock(true);
        } else if (config.general.positionType == PositionType.TARGETED_BLOCK_WITHOUT_FLUID) {
            copyTargetedBlock(false);
        }
    }

    public void copyPlayerLocation() {
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
        client.keyboard.setClipboard(content);
    }

    public void copyUnderfootBlock() {
        BlockPos pos = player.getBlockPos();
        int x = pos.getX();
        int y = pos.getY() - 1;
        int z = pos.getZ();
        String content = config.general.format
                .replace("${player}", player.getName().getString())
                .replace("${x}", String.valueOf(x))
                .replace("${y}", String.valueOf(y))
                .replace("${z}", String.valueOf(z));
        client.keyboard.setClipboard(content);
    }

    public void copyTargetedBlock(boolean withFluid) {
        Vec3d pos = player.raycast(config.general.targetedMaxDistance, 0.0F, withFluid).getPos();
        double x = pos.getX();
        double y = pos.getY();
        double z = pos.getZ();
        String content = config.general.format
                .replace("${player}", player.getName().getString())
                .replace("${x}", String.format("%." + config.data.xPrecision + "f", x))
                .replace("${y}", String.format("%." + config.data.yPrecision + "f", y))
                .replace("${z}", String.format("%." + config.data.zPrecision + "f", z));
        client.keyboard.setClipboard(content);
    }
}
