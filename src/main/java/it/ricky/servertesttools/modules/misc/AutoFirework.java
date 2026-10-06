package it.ricky.servertesttools.modules.misc;

import it.ricky.servertesttools.modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public class AutoFirework extends Module {
    private int cooldown = 0;

    public AutoFirework() {
        super("Auto Firework", Category.MISC);
    }

    @Override
    public void onTick(MinecraftClient client) {
        ClientPlayerEntity player = client.player;
        if (player == null || client.interactionManager == null) return;
        if (!player.isGliding()) return;

        if (cooldown > 0) {
            cooldown--;
            return;
        }

        for (int i = 0; i < 9; i++) {
            ItemStack stack = player.getInventory().getStack(i);
            if (stack.isOf(Items.FIREWORK_ROCKET)) {
                int prev = player.getInventory().getSelectedSlot();
                player.getInventory().setSelectedSlot(i);
                client.interactionManager.interactItem(player, Hand.MAIN_HAND);
                player.getInventory().setSelectedSlot(prev);
                cooldown = 10;
                return;
            }
        }
    }
}
