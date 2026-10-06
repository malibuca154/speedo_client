package it.ricky.servertesttools.modules.render;

import it.ricky.servertesttools.modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class FpsHUD extends Module {
    public FpsHUD() {
        super("FPS HUD", Category.RENDER, true);
    }

    @Override
    public void onRender(MinecraftClient client, DrawContext context) {
        int y = 8;
        if (ModuleManagerHasCoordinates()) y += 12;
        context.drawTextWithShadow(client.textRenderer,
                Text.literal("FPS: " + client.getCurrentFps()), 8, y, 0xFFFFFF);
    }

    private boolean ModuleManagerHasCoordinates() {
        Module m = it.ricky.servertesttools.modules.ModuleManager.getByName("Coordinates HUD");
        return m != null && m.isEnabled();
    }
}
