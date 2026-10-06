package it.ricky.servertesttools.modules.render;

import it.ricky.servertesttools.modules.Module;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.WorldChunk;

/**
 * Block ESP with tracers toward valuable blocks (ores, spawners, portals…).
 * Same angle-based tracer system as Storage ESP / Player ESP.
 */
public class BlockESP extends Module {

    private static final int RANGE_CHUNKS = 3; // ±3 chunks
    private static final double MAX_DIST = 48.0;

    public BlockESP() {
        super("Block ESP", Category.RENDER);
    }

    @Override
    public void onRender(MinecraftClient client, DrawContext context) {
        if (client.player == null || client.world == null) return;

        BlockPos playerPos = client.player.getBlockPos();
        ChunkPos playerChunk = new ChunkPos(playerPos);

        for (int cx = -RANGE_CHUNKS; cx <= RANGE_CHUNKS; cx++) {
            for (int cz = -RANGE_CHUNKS; cz <= RANGE_CHUNKS; cz++) {
                WorldChunk chunk = client.world.getChunk(playerChunk.x + cx, playerChunk.z + cz);
                if (chunk == null) continue;

                ChunkSection[] sections = chunk.getSectionArray();
                int baseY = chunk.getBottomY();

                for (int si = 0; si < sections.length; si++) {
                    ChunkSection section = sections[si];
                    if (section == null || section.isEmpty()) continue;

                    int sectionY = baseY + si * 16;

                    for (int x = 0; x < 16; x++) {
                        for (int y = 0; y < 16; y++) {
                            for (int z = 0; z < 16; z++) {
                                Block block = section.getBlockState(x, y, z).getBlock();
                                int color = colorFor(block);
                                if (color == 0) continue;

                                int wx = (playerChunk.x + cx) * 16 + x;
                                int wy = sectionY + y;
                                int wz = (playerChunk.z + cz) * 16 + z;

                                double distSq = playerPos.getSquaredDistance(wx, wy, wz);
                                if (distSq > MAX_DIST * MAX_DIST) continue;

                                Vec3d target = new Vec3d(wx + 0.5, wy + 0.5, wz + 0.5);
                                TracerUtils.drawTracerFromCenter(context, client, target, color);
                            }
                        }
                    }
                }
            }
        }
    }

    /** 0 = ignore. ARGB color otherwise. */
    private static int colorFor(Block block) {
        // Ores
        if (block == Blocks.DIAMOND_ORE || block == Blocks.DEEPSLATE_DIAMOND_ORE) return 0xFF00FFFF;
        if (block == Blocks.EMERALD_ORE || block == Blocks.DEEPSLATE_EMERALD_ORE) return 0xFF00FF66;
        if (block == Blocks.ANCIENT_DEBRIS) return 0xFFAA4422;
        if (block == Blocks.GOLD_ORE || block == Blocks.DEEPSLATE_GOLD_ORE || block == Blocks.NETHER_GOLD_ORE)
            return 0xFFFFD700;
        if (block == Blocks.IRON_ORE || block == Blocks.DEEPSLATE_IRON_ORE) return 0xFFD8D8D8;
        if (block == Blocks.LAPIS_ORE || block == Blocks.DEEPSLATE_LAPIS_ORE) return 0xFF2244FF;
        if (block == Blocks.REDSTONE_ORE || block == Blocks.DEEPSLATE_REDSTONE_ORE) return 0xFFFF2222;
        if (block == Blocks.COAL_ORE || block == Blocks.DEEPSLATE_COAL_ORE) return 0xFF333333;
        if (block == Blocks.NETHER_QUARTZ_ORE) return 0xFFE8E8FF;

        // Utility / rare
        if (block == Blocks.SPAWNER) return 0xFFAA00FF;
        if (block == Blocks.END_PORTAL_FRAME) return 0xFF00FFAA;
        if (block == Blocks.END_PORTAL) return 0xFF00AA88;
        if (block == Blocks.NETHER_PORTAL) return 0xFF8800FF;
        if (block == Blocks.BEDROCK) return 0xFF555555;
        if (block == Blocks.OBSIDIAN) return 0xFF220044;
        if (block == Blocks.CRYING_OBSIDIAN) return 0xFF6600AA;
        if (block == Blocks.ENCHANTING_TABLE) return 0xFFCC44FF;
        if (block == Blocks.BEACON) return 0xFF44FFFF;
        if (block == Blocks.DRAGON_EGG) return 0xFF8800CC;

        return 0;
    }
}
