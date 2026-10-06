package it.ricky.servertesttools.modules.render;

import it.ricky.servertesttools.modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class CoordinatesHUD extends Module {
    public CoordinatesHUD() {
        super("Coordinates HUD", Category.RENDER, true);
    }

    @Override
    public void onRender(MinecraftClient client, DrawContext context) {
        if (client.player == null) return;
        String text = String.format("XYZ: %d %d %d",
                client.player.getBlockX(),
                client.player.getBlockY(),
                client.player.getBlockZ());
        context.drawTextWithShadow(client.textRenderer, Text.literal(text), 8, 8, 0xFFFFFF);
    }
}
