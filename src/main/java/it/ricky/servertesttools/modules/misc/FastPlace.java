package it.ricky.servertesttools.modules.misc;

import it.ricky.servertesttools.modules.Module;
import net.minecraft.client.MinecraftClient;

/**
 * Without a mixin, itemUseCooldown is private.
 * This module is a placeholder that stays enabled so you can add a mixin later.
 * For a working version you need an access widener or mixin on MinecraftClient.itemUseCooldown.
 */
public class FastPlace extends Module {
    public FastPlace() {
        super("Fast Place", Category.MISC);
    }

    @Override
    public void onTick(MinecraftClient client) {
        // itemUseCooldown is private in 1.21.11 — requires mixin/access widener
    }
}
