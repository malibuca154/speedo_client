package it.ricky.servertesttools.modules.combat;

import it.ricky.servertesttools.modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.SlotActionType;

/** Keeps a totem in offhand (same logic as Auto Totem) */
public class TotemOffhand extends Module {
    public TotemOffhand() {
        super("Totem Offhand", Category.COMBAT);
    }

    @Override
    public void onTick(MinecraftClient client) {
        ClientPlayerEntity player = client.player;
        if (player == null || client.interactionManager == null) return;
        if (player.getOffHandStack().isOf(Items.TOTEM_OF_UNDYING)) return;

        for (int i = 0; i < 36; i++) {
            ItemStack stack = player.getInventory().getStack(i);
            if (stack.isOf(Items.TOTEM_OF_UNDYING)) {
                int containerSlot = i < 9 ? i + 36 : i;
                client.interactionManager.clickSlot(
                        player.playerScreenHandler.syncId,
                        containerSlot, 40, SlotActionType.SWAP, player);
                return;
            }
        }
    }
}
