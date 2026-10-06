package it.ricky.servertesttools.modules.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

/**
 * Angle-based tracers (reliable for HUD).
 *
 * Instead of fragile matrix projection, we compute the yaw/pitch difference
 * between the player's look direction and the target, then map that to
 * screen offsets. This is what many lightweight clients use for 2D tracers
 * and matches what you expect: line from crosshair toward the chest.
 */
public final class TracerUtils {
    private TracerUtils() {}

    /**
     * Project world pos to screen using angle delta from camera look.
     * Returns null if target is behind the player (more than 90° off).
     */
    public static float[] worldToScreen(Vec3d world, MinecraftClient client) {
        if (client.player == null) return null;

        Vec3d eye = client.player.getEyePos();
        Vec3d diff = world.subtract(eye);

        double dist = diff.length();
        if (dist < 0.1) return null;

        // Direction to target as yaw/pitch (same convention as MC)
        double targetYaw = Math.toDegrees(Math.atan2(-diff.x, diff.z));
        double targetPitch = Math.toDegrees(Math.asin(MathHelper.clamp(-diff.y / dist, -1.0, 1.0)));

        float playerYaw = client.player.getYaw();
        float playerPitch = client.player.getPitch();

        // Smallest angle difference
        double deltaYaw = MathHelper.wrapDegrees(targetYaw - playerYaw);
        double deltaPitch = targetPitch - playerPitch;

        // Behind player (more than ~90° to the side)
        if (Math.abs(deltaYaw) > 90.0) return null;

        int sw = client.getWindow().getScaledWidth();
        int sh = client.getWindow().getScaledHeight();
        double fov = client.options.getFov().getValue();

        // Approximate horizontal FOV from vertical FOV + aspect
        double aspect = (double) sw / (double) sh;
        double fovRad = Math.toRadians(fov);
        double fovH = Math.toDegrees(2.0 * Math.atan(Math.tan(fovRad / 2.0) * aspect));

        // Map angle → pixels (half screen = half FOV)
        float screenX = (float) (sw / 2.0 + (deltaYaw / fovH) * sw);
        float screenY = (float) (sh / 2.0 + (deltaPitch / fov) * sh);

        return new float[]{screenX, screenY};
    }

    private static void plot(DrawContext ctx, int x, int y, int argb) {
        ctx.fill(x, y, x + 1, y + 1, argb);
    }

    public static void drawLine(DrawContext ctx, float x0, float y0, float x1, float y1, int argb) {
        int xA = MathHelper.floor(x0);
        int yA = MathHelper.floor(y0);
        int xB = MathHelper.floor(x1);
        int yB = MathHelper.floor(y1);

        int dx = Math.abs(xB - xA);
        int dy = Math.abs(yB - yA);
        int sx = xA < xB ? 1 : -1;
        int sy = yA < yB ? 1 : -1;
        int err = dx - dy;

        int x = xA;
        int y = yA;
        int guard = 0;
        while (true) {
            plot(ctx, x, y, argb);
            if (x == xB && y == yB) break;
            if (++guard > 2000) break;
            int e2 = 2 * err;
            if (e2 > -dy) { err -= dy; x += sx; }
            if (e2 < dx)  { err += dx; y += sy; }
        }
    }

    public static void drawTracerFromCenter(DrawContext ctx, MinecraftClient client, Vec3d worldPos, int argb) {
        float[] screen = worldToScreen(worldPos, client);
        if (screen == null) return;

        int sw = client.getWindow().getScaledWidth();
        int sh = client.getWindow().getScaledHeight();
        float cx = sw / 2f;
        float cy = sh / 2f;
        float sx = screen[0];
        float sy = screen[1];

        // If projected point is basically the center, skip (too close to look dir)
        if (Math.abs(sx - cx) < 2 && Math.abs(sy - cy) < 2) return;

        // Gap at crosshair
        float t0 = 0.05f;
        float x0 = cx + (sx - cx) * t0;
        float y0 = cy + (sy - cy) * t0;

        float x1 = sx;
        float y1 = sy;

        // Clip to screen
        if (sx < 2 || sx > sw - 2 || sy < 2 || sy > sh - 2) {
            float[] clipped = clipToScreen(cx, cy, sx, sy, sw, sh);
            if (clipped == null) return;
            x1 = clipped[0];
            y1 = clipped[1];
        }

        drawLine(ctx, x0, y0, x1, y1, argb);

        int tx = MathHelper.floor(x1);
        int ty = MathHelper.floor(y1);
        plot(ctx, tx, ty, argb);
        plot(ctx, tx - 1, ty, argb);
        plot(ctx, tx + 1, ty, argb);
        plot(ctx, tx, ty - 1, argb);
        plot(ctx, tx, ty + 1, argb);
    }

    private static float[] clipToScreen(float cx, float cy, float sx, float sy, int sw, int sh) {
        float dx = sx - cx;
        float dy = sy - cy;
        if (Math.abs(dx) < 1e-6 && Math.abs(dy) < 1e-6) return null;

        float tMin = 1f;
        if (dx > 0) tMin = Math.min(tMin, (sw - 2 - cx) / dx);
        else if (dx < 0) tMin = Math.min(tMin, (2 - cx) / dx);
        if (dy > 0) tMin = Math.min(tMin, (sh - 2 - cy) / dy);
        else if (dy < 0) tMin = Math.min(tMin, (2 - cy) / dy);

        if (tMin <= 0.01f) return null;
        return new float[]{cx + dx * tMin, cy + dy * tMin};
    }
}
