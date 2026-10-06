package it.ricky.servertesttools.modules.misc;

import it.ricky.servertesttools.modules.Module;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;

public class AutoTool extends Module {
    public AutoTool() {
        super("Auto Tool", Category.MISC);
    }

    @Override
    public void onTick(MinecraftClient client) {
        ClientPlayerEntity player = client.player;
        if (player == null || client.world == null) return;
        if (!client.options.attackKey.isPressed()) return;

        HitResult hit = client.crosshairTarget;
        if (hit == null || hit.getType() != HitResult.Type.BLOCK) return;

        BlockState state = client.world.getBlockState(((BlockHitResult) hit).getBlockPos());
        int bestSlot = -1;
        float bestSpeed = 1.0f;

        for (int i = 0; i < 9; i++) {
            ItemStack stack = player.getInventory().getStack(i);
            if (stack.isEmpty()) continue;
            float speed = stack.getMiningSpeedMultiplier(state);
            if (speed > bestSpeed) {
                bestSpeed = speed;
                bestSlot = i;
            }
        }
        if (bestSlot != -1 && bestSlot != player.getInventory().getSelectedSlot()) {
            player.getInventory().setSelectedSlot(bestSlot);
        }
    }
}
