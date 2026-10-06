package it.ricky.servertesttools.modules.render;

import it.ricky.servertesttools.modules.Module;
import net.minecraft.client.MinecraftClient;

public class Fullbright extends Module {
    private double previousGamma = 1.0;

    public Fullbright() {
        super("Fullbright", Category.RENDER);
    }

    @Override
    protected void onEnable() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.options != null) {
            previousGamma = client.options.getGamma().getValue();
            client.options.getGamma().setValue(16.0);
        }
    }

    @Override
    protected void onDisable() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.options != null) {
            client.options.getGamma().setValue(previousGamma);
        }
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.options != null && client.options.getGamma().getValue() < 15.0) {
            client.options.getGamma().setValue(16.0);
        }
    }
}
