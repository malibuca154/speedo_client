package it.ricky.servertesttools.modules.combat;

import it.ricky.servertesttools.modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class AimAssist extends Module {
    private static final double RANGE = 4.5;
    private static final float SPEED = 0.35f;

    public AimAssist() {
        super("Aim Assist", Category.COMBAT);
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null || client.world == null) return;
        if (client.currentScreen != null) return;

        LivingEntity target = findTarget(client);
        if (target == null) return;

        Vec3d eyes = client.player.getEyePos();
        Vec3d targetPos = target.getEntityPos().add(0, target.getHeight() * 0.85, 0);
        Vec3d diff = targetPos.subtract(eyes);

        double dist = diff.horizontalLength();
        float targetYaw = (float) (MathHelper.atan2(diff.z, diff.x) * (180.0 / Math.PI)) - 90.0f;
        float targetPitch = (float) -(MathHelper.atan2(diff.y, dist) * (180.0 / Math.PI));

        float currentYaw = client.player.getYaw();
        float currentPitch = client.player.getPitch();

        float yawDiff = MathHelper.wrapDegrees(targetYaw - currentYaw);
        float pitchDiff = targetPitch - currentPitch;

        client.player.setYaw(currentYaw + yawDiff * SPEED);
        client.player.setPitch(MathHelper.clamp(currentPitch + pitchDiff * SPEED, -90f, 90f));
    }

    private LivingEntity findTarget(MinecraftClient client) {
        LivingEntity closest = null;
        double closestDist = RANGE;

        for (Entity entity : client.world.getEntities()) {
            if (!(entity instanceof LivingEntity living) || !living.isAlive()) continue;
            if (entity == client.player) continue;
            if (entity instanceof PlayerEntity && ((PlayerEntity) entity).isCreative()) continue;

            double d = client.player.distanceTo(entity);
            if (d < closestDist) {
                Vec3d look = client.player.getRotationVec(1.0f);
                Vec3d toEntity = entity.getEntityPos().subtract(client.player.getEyePos()).normalize();
                if (look.dotProduct(toEntity) > 0.3) {
                    closestDist = d;
                    closest = living;
                }
            }
        }
        return closest;
    }
}
