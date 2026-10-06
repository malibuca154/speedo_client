package it.ricky.servertesttools.modules.misc;

import it.ricky.servertesttools.modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;

public class Freecam extends Module {
    private Vec3d cameraPos;
    private Vec3d savedPlayerPos;
    private float savedYaw;
    private float savedPitch;

    public Freecam() {
        super("Freecam", Category.MISC);
    }

    @Override
    protected void onEnable() {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if (player == null) return;

        savedPlayerPos = player.getEntityPos();
        savedYaw = player.getYaw();
        savedPitch = player.getPitch();
        cameraPos = player.getEyePos();
        player.noClip = true;
        player.setVelocity(Vec3d.ZERO);
    }

    @Override
    protected void onDisable() {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if (player == null) return;

        player.noClip = false;
        if (savedPlayerPos != null) {
            player.setPosition(savedPlayerPos.x, savedPlayerPos.y, savedPlayerPos.z);
            player.setYaw(savedYaw);
            player.setPitch(savedPitch);
        }
        cameraPos = null;
    }

    @Override
    public void onTick(MinecraftClient client) {
        ClientPlayerEntity player = client.player;
        if (player == null || cameraPos == null) return;

        player.noClip = true;
        player.setVelocity(Vec3d.ZERO);
        if (savedPlayerPos != null) {
            player.setPosition(savedPlayerPos.x, savedPlayerPos.y, savedPlayerPos.z);
        }

        float speed = client.options.sprintKey.isPressed() ? 1.2f : 0.4f;
        float yaw = player.getYaw();
        float pitch = player.getPitch();
        double yawRad = Math.toRadians(yaw);
        double pitchRad = Math.toRadians(pitch);

        Vec3d forward = new Vec3d(
                -Math.sin(yawRad) * Math.cos(pitchRad),
                -Math.sin(pitchRad),
                Math.cos(yawRad) * Math.cos(pitchRad)
        ).normalize().multiply(speed);

        Vec3d right = new Vec3d(Math.cos(yawRad), 0, Math.sin(yawRad)).normalize().multiply(speed);

        if (client.options.forwardKey.isPressed()) cameraPos = cameraPos.add(forward);
        if (client.options.backKey.isPressed()) cameraPos = cameraPos.subtract(forward);
        if (client.options.rightKey.isPressed()) cameraPos = cameraPos.add(right);
        if (client.options.leftKey.isPressed()) cameraPos = cameraPos.subtract(right);
        if (client.options.jumpKey.isPressed()) cameraPos = cameraPos.add(0, speed, 0);
        if (client.options.sneakKey.isPressed()) cameraPos = cameraPos.add(0, -speed, 0);
    }

    public Vec3d getCameraPos() {
        return cameraPos;
    }

    public boolean isActive() {
        return isEnabled() && cameraPos != null;
    }
}
