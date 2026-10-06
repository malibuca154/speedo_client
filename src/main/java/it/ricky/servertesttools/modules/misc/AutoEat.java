package it.ricky.servertesttools.modules.misc;

import it.ricky.servertesttools.modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public class AutoEat extends Module {
    public AutoEat() {
        super("Auto Eat", Category.MISC);
    }

    @Override
    public void onTick(MinecraftClient client) {
        ClientPlayerEntity player = client.player;
        if (player == null || client.interactionManager == null) return;
        if (player.getHungerManager().getFoodLevel() > 14) return;
        if (player.isUsingItem()) return;

        for (int i = 0; i < 9; i++) {
            ItemStack stack = player.getInventory().getStack(i);
            if (stack.contains(DataComponentTypes.FOOD)) {
                player.getInventory().setSelectedSlot(i);
                client.interactionManager.interactItem(player, Hand.MAIN_HAND);
                return;
            }
        }
    }
}
