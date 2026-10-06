package it.ricky.servertesttools.modules.misc;

import it.ricky.servertesttools.modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;

public class Sprint extends Module {
    public Sprint() {
        super("Sprint", Category.MISC);
    }

    @Override
    public void onTick(MinecraftClient client) {
        ClientPlayerEntity player = client.player;
        if (player == null) return;
        if (player.forwardSpeed > 0 && !player.isSneaking() && !player.horizontalCollision) {
            player.setSprinting(true);
        }
    }
}
