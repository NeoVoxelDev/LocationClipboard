package dev.neovoxel.lc.integration;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.neovoxel.lc.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> AutoConfig.getConfigScreen(ModConfig.class, parent).get();
    }
}