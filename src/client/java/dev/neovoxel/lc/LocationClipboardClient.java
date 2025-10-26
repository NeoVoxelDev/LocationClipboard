package dev.neovoxel.lc;

import dev.neovoxel.lc.config.ModConfig;
import dev.neovoxel.lc.util.ClipboardActionUtil;
import dev.neovoxel.lc.util.RecordActionUtil;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class LocationClipboardClient implements ClientModInitializer {
	public static KeyBinding copyKey;
	public static KeyBinding recordKey;
	public static RecordActionUtil recordActionUtil;

	@Override
    public void onInitializeClient() {
		copyKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.location-clipboard.copy",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_F7,
				"category.location-clipboard.default"
		));
		recordKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.location-clipboard.record",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_F8,
				"category.location-clipboard.default"
		));
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (copyKey.wasPressed()) {
				ClientPlayerEntity player = client.player;
				ClipboardActionUtil actionUtil = new ClipboardActionUtil(player);
				actionUtil.copy();
			}
		});
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (recordKey.wasPressed()) {
				ClientPlayerEntity player = client.player;
				ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
				if (!config.record.enable) continue;
				if (recordActionUtil != null) {
					recordActionUtil.stop();
					recordActionUtil = null;
				} else {
					recordActionUtil = new RecordActionUtil(player);
					recordActionUtil.start();
				}
			}
		});
	}
}