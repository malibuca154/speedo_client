package it.ricky.servertesttools.modules.combat;

import it.ricky.servertesttools.modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;

public class AutoJumpReset extends Module {
    private boolean wasHurt = false;

    public AutoJumpReset() {
        super("Auto Jump Reset", Category.COMBAT);
    }

    @Override
    public void onTick(MinecraftClient client) {
        ClientPlayerEntity player = client.player;
        if (player == null) return;

        if (player.hurtTime > 0 && player.hurtTime < 5 && !wasHurt) {
            if (player.isOnGround()) {
                player.jump();
            }
            wasHurt = true;
        }
        if (player.hurtTime == 0) {
            wasHurt = false;
        }
    }
}
