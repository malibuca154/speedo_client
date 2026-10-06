package it.ricky.servertesttools.modules.misc;

import it.ricky.servertesttools.modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;

public class AutoReconnect extends Module {
    private int ticksDisconnected = 0;
    private static final int RECONNECT_DELAY = 60; // 3 seconds

    public AutoReconnect() {
        super("Auto Reconnect", Category.MISC);
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.currentScreen instanceof DisconnectedScreen) {
            ticksDisconnected++;
            if (ticksDisconnected >= RECONNECT_DELAY) {
                ticksDisconnected = 0;
                ServerInfo info = client.getCurrentServerEntry();
                if (info != null) {
                    ConnectScreen.connect(new TitleScreen(), client,
                            ServerAddress.parse(info.address), info, false, null);
                }
            }
        } else {
            ticksDisconnected = 0;
        }
    }
}
