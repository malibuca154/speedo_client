package it.ricky.servertesttools.modules.combat;

import it.ricky.servertesttools.modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

public class ShieldBreaker extends Module {
    public ShieldBreaker() {
        super("Shield Breaker", Category.COMBAT);
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null || client.interactionManager == null) return;
        if (client.currentScreen != null) return;

        HitResult hit = client.crosshairTarget;
        if (hit == null || hit.getType() != HitResult.Type.ENTITY) return;

        Entity target = ((EntityHitResult) hit).getEntity();
        if (!(target instanceof PlayerEntity playerTarget) || !playerTarget.isBlocking()) return;

        int axeSlot = -1;
        for (int i = 0; i < 9; i++) {
            ItemStack stack = client.player.getInventory().getStack(i);
            if (stack.getItem() instanceof AxeItem) {
                axeSlot = i;
                break;
            }
        }
        if (axeSlot == -1) return;

        client.player.getInventory().setSelectedSlot(axeSlot);
        if (client.player.getAttackCooldownProgress(0.5f) >= 1.0f) {
            client.interactionManager.attackEntity(client.player, target);
            client.player.swingHand(Hand.MAIN_HAND);
        }
    }
}
