package it.ricky.servertesttools;

import it.ricky.servertesttools.modules.Module;
import it.ricky.servertesttools.modules.ModuleManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public final class ServerTestToolsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // Force ModuleManager static init
        ModuleManager.getModules();

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

            // Tick all enabled modules
            for (Module module : ModuleManager.getModules()) {
                if (module.isEnabled()) {
                    module.onTick(client);
                }
            }
        });

        HudRenderCallback.EVENT.register((context, tickCounter) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            for (Module module : ModuleManager.getModules()) {
                if (module.isEnabled()) {
                    module.onRender(client, context);
                }
            }
        });
    }
}
