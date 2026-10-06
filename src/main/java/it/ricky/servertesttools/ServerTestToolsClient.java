package it.ricky.servertesttools;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public final class ServerTestToolsClient implements ClientModInitializer {
    public static boolean showCoordinates = true;
    public static boolean showFps = true;
    public static boolean showLocalChunk = false;

    @Override
    public void onInitializeClient() {
        KeyBinding menuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.servertesttools.open_menu",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_SPACE,
                KeyBinding.Category.MISC
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (menuKey.wasPressed()) {
                if (client.options.sneakKey.isPressed() && client.currentScreen == null) {
                    client.setScreen(new DiagnosticsScreen(null));
                }
            }
        });

        HudRenderCallback.EVENT.register((context, tickCounter) -> renderHud(MinecraftClient.getInstance(), context));
    }

    private static void renderHud(MinecraftClient client, DrawContext context) {
        if (client.player == null || client.world == null) {
            return;
        }

        int y = 8;
        if (showCoordinates) {
            String coordinates = String.format("XYZ: %d %d %d",
                    client.player.getBlockX(), client.player.getBlockY(), client.player.getBlockZ());
                context.drawTextWithShadow(client.textRenderer, Text.literal(coordinates), 8, y, 0xFFFFFF);
            y += 12;
        }
        if (showFps) {
                context.drawTextWithShadow(client.textRenderer, Text.literal("FPS: " + client.getCurrentFps()), 8, y, 0xFFFFFF);
            y += 12;
        }
        if (showLocalChunk) {
            int chunkX = client.player.getBlockX() >> 4;
            int chunkZ = client.player.getBlockZ() >> 4;
                context.drawTextWithShadow(client.textRenderer,
                    Text.literal("Current chunk: " + chunkX + ", " + chunkZ), 8, y, 0xFFFFFF);
        }
    }
}
