package it.ricky.servertesttools.modules.combat;

import it.ricky.servertesttools.modules.Module;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;

public class AutoCrystal extends Module {
    private int placeCooldown = 0;
    private int breakCooldown = 0;

    public AutoCrystal() {
        super("Auto Crystal", Category.COMBAT);
    }

    @Override
    public void onTick(MinecraftClient client) {
        ClientPlayerEntity player = client.player;
        if (player == null || client.world == null || client.interactionManager == null) return;
        if (client.currentScreen != null) return;

        if (placeCooldown > 0) placeCooldown--;
        if (breakCooldown > 0) breakCooldown--;

        if (breakCooldown <= 0) {
            for (Entity entity : client.world.getEntities()) {
                if (entity instanceof EndCrystalEntity crystal && player.distanceTo(crystal) <= 4.5) {
                    client.interactionManager.attackEntity(player, crystal);
                    player.swingHand(Hand.MAIN_HAND);
                    breakCooldown = 2;
                    break;
                }
            }
        }

        if (placeCooldown <= 0 && player.getMainHandStack().isOf(Items.END_CRYSTAL)) {
            HitResult hit = client.crosshairTarget;
            if (hit != null && hit.getType() == HitResult.Type.BLOCK) {
                BlockHitResult blockHit = (BlockHitResult) hit;
                var state = client.world.getBlockState(blockHit.getBlockPos());
                if (state.isOf(Blocks.OBSIDIAN) || state.isOf(Blocks.BEDROCK)) {
                    if (client.world.getBlockState(blockHit.getBlockPos().up()).isAir()) {
                        client.interactionManager.interactBlock(player, Hand.MAIN_HAND, blockHit);
                        player.swingHand(Hand.MAIN_HAND);
                        placeCooldown = 3;
                    }
                }
            }
        }
    }
}
