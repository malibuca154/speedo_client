package it.ricky.servertesttools.modules.misc;

import it.ricky.servertesttools.modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Hand;

public class AutoClicker extends Module {
    private int delay = 0;
    private final int cps = 12; // clicks per second target

    public AutoClicker() {
        super("Auto Clicker", Category.MISC);
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null || client.interactionManager == null) return;
        if (client.currentScreen != null) return;
        if (!client.options.attackKey.isPressed()) return;

        delay++;
        int ticksPerClick = Math.max(1, 20 / cps);
        if (delay >= ticksPerClick) {
            delay = 0;
            client.interactionManager.attackEntity(client.player, client.targetedEntity != null ? client.targetedEntity : client.player);
            client.player.swingHand(Hand.MAIN_HAND);
        }
    }
}
