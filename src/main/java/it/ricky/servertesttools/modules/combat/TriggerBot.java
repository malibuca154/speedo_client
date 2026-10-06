package it.ricky.servertesttools.modules.combat;

import it.ricky.servertesttools.modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

public class TriggerBot extends Module {
    public TriggerBot() {
        super("Trigger Bot", Category.COMBAT);
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null || client.world == null || client.interactionManager == null) return;
        if (client.currentScreen != null) return;

        HitResult hit = client.crosshairTarget;
        if (hit == null || hit.getType() != HitResult.Type.ENTITY) return;

        Entity target = ((EntityHitResult) hit).getEntity();
        if (!(target instanceof LivingEntity living) || !living.isAlive()) return;
        if (target == client.player) return;

        // Only attack when cooldown is ready
        if (client.player.getAttackCooldownProgress(0.5f) < 1.0f) return;

        client.interactionManager.attackEntity(client.player, target);
        client.player.swingHand(Hand.MAIN_HAND);
    }
}
