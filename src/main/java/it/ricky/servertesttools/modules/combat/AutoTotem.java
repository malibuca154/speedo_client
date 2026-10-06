package it.ricky.servertesttools.modules.combat;

import it.ricky.servertesttools.modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.SlotActionType;

public class AutoTotem extends Module {
    public AutoTotem() {
        super("Auto Totem", Category.COMBAT);
    }

    @Override
    public void onTick(MinecraftClient client) {
        ClientPlayerEntity player = client.player;
        if (player == null || client.interactionManager == null) return;

        ItemStack offhand = player.getOffHandStack();
        if (offhand.isOf(Items.TOTEM_OF_UNDYING)) return;

        PlayerInventory inv = player.getInventory();
        int totemSlot = -1;
        for (int i = 0; i < 36; i++) {
            if (inv.getStack(i).isOf(Items.TOTEM_OF_UNDYING)) {
                totemSlot = i;
                break;
            }
        }
        if (totemSlot == -1) return;

        int containerSlot = totemSlot < 9 ? totemSlot + 36 : totemSlot;
        client.interactionManager.clickSlot(
                player.playerScreenHandler.syncId,
                containerSlot,
                40,
                SlotActionType.SWAP,
                player
        );
    }
}
