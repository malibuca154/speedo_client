package it.ricky.servertesttools.modules.render;

import it.ricky.servertesttools.modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class CurrentChunkHUD extends Module {
    public CurrentChunkHUD() {
        super("Current chunk", Category.RENDER);
    }

    @Override
    public void onRender(MinecraftClient client, DrawContext context) {
        if (client.player == null) return;
        int y = 8;
        if (isEnabled("Coordinates HUD")) y += 12;
        if (isEnabled("FPS HUD")) y += 12;
        int cx = client.player.getBlockX() >> 4;
        int cz = client.player.getBlockZ() >> 4;
        context.drawTextWithShadow(client.textRenderer,
                Text.literal("Current chunk: " + cx + ", " + cz), 8, y, 0xFFFFFF);
    }

    private boolean isEnabled(String name) {
        Module m = it.ricky.servertesttools.modules.ModuleManager.getByName(name);
        return m != null && m.isEnabled();
    }
}
