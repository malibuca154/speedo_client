package it.ricky.servertesttools.modules.render;

import it.ricky.servertesttools.modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.util.math.Vec3d;

/**
 * Player ESP – tracers to other players (health-colored).
 */
public class PlayerESP extends Module {
    public PlayerESP() {
        super("Player ESP", Category.RENDER);
    }

    @Override
    public void onRender(MinecraftClient client, DrawContext context) {
        if (client.player == null || client.world == null) return;

        for (AbstractClientPlayerEntity player : client.world.getPlayers()) {
            if (player == client.player || !player.isAlive()) continue;
            if (client.player.distanceTo(player) > 128) continue;

            // Body center
            Vec3d target = player.getEntityPos().add(0, player.getHeight() * 0.5, 0);

            float health = player.getHealth() + player.getAbsorptionAmount();
            int color;
            if (health > 15) color = 0xFF40FF40;      // green – healthy
            else if (health > 8) color = 0xFFFFFF40;  // yellow
            else color = 0xFFFF4040;                  // red – low HP

            TracerUtils.drawTracerFromCenter(context, client, target, color);
        }
    }
}
