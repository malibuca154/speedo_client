package it.ricky.servertesttools.modules.combat;

import it.ricky.servertesttools.modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Items;

/**
 * Without a mixin, itemUseCooldown is private.
 * Placeholder until an access widener/mixin is added.
 */
public class CrystalOptimizer extends Module {
    public CrystalOptimizer() {
        super("Crystal Optimizer", Category.COMBAT);
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null) return;
        // Would reset itemUseCooldown when holding crystals — needs mixin
        if (client.player.getMainHandStack().isOf(Items.END_CRYSTAL)
                || client.player.getOffHandStack().isOf(Items.END_CRYSTAL)) {
            // no-op without mixin
        }
    }
}
