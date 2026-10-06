package it.ricky.servertesttools.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public abstract class Module {
    private final String name;
    private final Category category;
    private boolean enabled;

    public Module(String name, Category category) {
        this.name = name;
        this.category = category;
        this.enabled = false;
    }

    public Module(String name, Category category, boolean defaultEnabled) {
        this.name = name;
        this.category = category;
        this.enabled = defaultEnabled;
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        if (this.enabled == enabled) return;
        this.enabled = enabled;
        if (enabled) {
            onEnable();
        } else {
            onDisable();
        }
    }

    public void toggle() {
        setEnabled(!enabled);
    }

    protected void onEnable() {}
    protected void onDisable() {}

    /** Called every client tick while enabled */
    public void onTick(MinecraftClient client) {}

    /** Called every frame for HUD / ESP rendering */
    public void onRender(MinecraftClient client, DrawContext context) {}

    public enum Category {
        COMBAT("Combat"),
        MISC("Misc"),
        DONUT("Donut"),
        BASEFINDING("BaseFinding"),
        RENDER("Render"),
        CLIENT("Client");

        private final String displayName;

        Category(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
