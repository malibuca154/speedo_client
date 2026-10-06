package it.ricky.servertesttools.modules.combat;

import it.ricky.servertesttools.modules.Module;
import net.minecraft.client.MinecraftClient;

/**
 * attackCooldown is private on MinecraftClient in 1.21.11.
 * Requires a mixin/access widener to work fully.
 */
public class NoHitDelay extends Module {
    public NoHitDelay() {
        super("No Hit Delay", Category.COMBAT);
    }

    @Override
    public void onTick(MinecraftClient client) {
        // client.attackCooldown = 0; // private — needs mixin
    }
}
