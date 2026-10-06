package it.ricky.servertesttools.modules.render;

import it.ricky.servertesttools.modules.Module;
import net.minecraft.block.entity.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.chunk.WorldChunk;

/**
 * Storage ESP with tracers (Meteor-style: line from player toward chests).
 * Color by distance so you know how far the chest is (including underground).
 */
public class StorageESP extends Module {
    public StorageESP() {
        super("Storage ESP", Category.RENDER);
    }

    @Override
    public void onRender(MinecraftClient client, DrawContext context) {
        if (client.player == null || client.world == null) return;

        BlockPos playerPos = client.player.getBlockPos();
        ChunkPos playerChunk = new ChunkPos(playerPos);

        // ±5 chunks = up to ~80 blocks radius
        for (int cx = -5; cx <= 5; cx++) {
            for (int cz = -5; cz <= 5; cz++) {
                WorldChunk chunk = client.world.getChunk(playerChunk.x + cx, playerChunk.z + cz);
                if (chunk == null) continue;

                for (BlockEntity be : chunk.getBlockEntities().values()) {
                    if (!isStorage(be)) continue;

                    BlockPos pos = be.getPos();
                    double distSq = playerPos.getSquaredDistance(pos);
                    if (distSq > 80 * 80) continue;

                    Vec3d target = new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                    double dist = Math.sqrt(distSq);

                    // Distance colors (like Meteor distance-colors option)
                    int color = colorByDistance(dist);

                    // Slightly tint by type (alpha kept high)
                    color = blendTypeHint(color, be);

                    TracerUtils.drawTracerFromCenter(context, client, target, color);
                }
            }
        }
    }

    private static boolean isStorage(BlockEntity be) {
        return be instanceof ChestBlockEntity
                || be instanceof EnderChestBlockEntity
                || be instanceof ShulkerBoxBlockEntity
                || be instanceof BarrelBlockEntity
                || be instanceof HopperBlockEntity
                || be instanceof AbstractFurnaceBlockEntity
                || be instanceof DispenserBlockEntity
                || be instanceof DropperBlockEntity
                || be instanceof BrewingStandBlockEntity;
    }

    /** Green near → yellow → orange → red far */
    private static int colorByDistance(double dist) {
        if (dist < 10) return 0xFF22FF55;   // green
        if (dist < 24) return 0xFFFFFF33;   // yellow
        if (dist < 48) return 0xFFFFAA22;   // orange
        return 0xFFFF4444;                 // red
    }

    /** Mild type tint without killing distance readability */
    private static int blendTypeHint(int base, BlockEntity be) {
        if (be instanceof EnderChestBlockEntity) return 0xFFCC66FF; // purple
        if (be instanceof ShulkerBoxBlockEntity) return 0xFFFF66CC; // pink
        return base;
    }
}
