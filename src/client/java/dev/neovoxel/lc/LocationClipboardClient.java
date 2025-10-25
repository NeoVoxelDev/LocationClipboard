package dev.neovoxel.lc;

import dev.neovoxel.lc.config.ModConfig;
import dev.neovoxel.lc.util.ClipboardActionUtil;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class LocationClipboardClient implements ClientModInitializer {
	public static KeyBinding keyBinding;

	@Override
    public void onInitializeClient() {
		keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.location-clipboard.copy",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_F7,
				"category.location-clipboard.default"
		));
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (keyBinding.wasPressed()) {
				ClientPlayerEntity player = client.player;
				ClipboardActionUtil actionUtil = new ClipboardActionUtil(player);
				actionUtil.copy();
			}
		});
	}
}