package it.ricky.servertesttools.modules.misc;

import it.ricky.servertesttools.modules.Module;
import net.minecraft.client.MinecraftClient;

/** Replaces your real name with a protected name in the HUD list (limited without mixins) */
public class NameProtect extends Module {
    public static final String PROTECTED_NAME = "Player";

    public NameProtect() {
        super("Name Protect", Category.MISC);
    }

    public static String protect(String name) {
        Module m = it.ricky.servertesttools.modules.ModuleManager.getByName("Name Protect");
        if (m == null || !m.isEnabled()) return name;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null && name.equals(client.player.getName().getString())) {
            return PROTECTED_NAME;
        }
        return name;
    }
}
