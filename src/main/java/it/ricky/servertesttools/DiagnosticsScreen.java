package it.ricky.servertesttools;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public final class DiagnosticsScreen extends Screen {
        private static final int GAP = 6;
        private static final int MIN_PANEL_WIDTH = 140;
        private static final int VIEW_TOP = 42;
        private static final int VIEW_BOTTOM_MARGIN = 34;
        private static final int ROW_HEIGHT = 16;
    private static final List<Category> CATEGORIES = List.of(
            cosmetic("Combat", "Aim Assist", "Anchor Macro", "Auto Double Hand", "Auto Crystal",
                    "Auto Hit Crystal", "Auto Inv Totem", "Auto Jump Reset", "Auto Totem", "Crystal Optimizer",
                    "Double Anchor", "Elytra Swap", "HitBox", "Hover Totem", "Totem Offhand", "Mace Swap",
                    "Spear Swap", "No Hit Delay", "Shield Breaker", "Static HitBoxes", "Trigger Bot", "Mace Bomber"),
            cosmetic("Misc", "Auto Clicker", "Auto Eat", "Auto Firework", "Auto Log", "Auto Loot", "Auto Mine",
                    "Auto Tool", "Auto Tpa", "Cord Snapper", "Elytra Glide", "Fast Place", "Freecam", "Key Pearl",
                    "Key Wind Charge", "Name Protect", "Sprint", "Skin Protect", "Auto Reconnect"),
            cosmetic("Donut", "Anti Trap", "Auction Sniper", "Auto Sell", "Auto Spawner Sell", "Item Dropper",
                    "Netherite Finder", "Rtp Base Finder", "Auto Shulker Buy", "Tunnel Base Finder", "Fake Stats",
                    "Spawner Protect", "Chunk Finder", "Prime Chunk Finder"),
            cosmetic("BaseFinding", "Seed Chunk Finder", "Hole ESP", "Light Finder", "Sus Chunk Finder",
                    "Suspicious ESP"),
                renderCategory(),
            cosmetic("Client", "Krypton+", "Chat Macro", "Radio", "Friends", "Discord Presence")
    );

    private final Screen parent;
    private final List<PanelLayout> panels = new ArrayList<>();
    private int scrollOffset;
    private int maxScroll;
    private int columns;

    public DiagnosticsScreen(Screen parent) {
        super(Text.literal("Server Test Tools"));
        this.parent = parent;
    }

    private static Category cosmetic(String title, String... labels) {
        List<ModuleEntry> entries = new ArrayList<>();
        for (int index = 0; index < labels.length; index++) {
            entries.add(ModuleEntry.decorative(labels[index], index % 4 == 1 || index % 7 == 3));
        }
        return new Category(title, false, entries);
    }

        private static Category renderCategory() {
        List<ModuleEntry> entries = new ArrayList<>(cosmetic("Render", "Ore Sim", "Fullbright", "SwingSpeed",
            "Jump Circles", "HUD", "Player ESP", "Storage ESP", "Block ESP", "Target HUD", "RealHitBox",
            "Free Look").entries());
        entries.add(ModuleEntry.live("Coordinates HUD", () -> ServerTestToolsClient.showCoordinates,
            value -> ServerTestToolsClient.showCoordinates = value));
        entries.add(ModuleEntry.live("FPS HUD", () -> ServerTestToolsClient.showFps,
            value -> ServerTestToolsClient.showFps = value));
        entries.add(ModuleEntry.live("Current chunk", () -> ServerTestToolsClient.showLocalChunk,
            value -> ServerTestToolsClient.showLocalChunk = value));
        return new Category("Render", false, entries);
        }

    @Override
    protected void init() {
        rebuildLayout();
    }

    private void rebuildLayout() {
        panels.clear();
        int contentWidth = Math.max(MIN_PANEL_WIDTH, this.width - 24);
        columns = Math.min(CATEGORIES.size(), Math.max(1, (contentWidth + GAP) / (MIN_PANEL_WIDTH + GAP)));
        int panelWidth = (contentWidth - GAP * (columns - 1)) / columns;
        int startX = (this.width - (panelWidth * columns + GAP * (columns - 1))) / 2;
        int fullY = VIEW_TOP;

        for (int first = 0; first < CATEGORIES.size(); first += columns) {
            int rowCount = Math.min(columns, CATEGORIES.size() - first);
            int maxEntries = 1;
            for (int index = first; index < first + rowCount; index++) {
                maxEntries = Math.max(maxEntries, CATEGORIES.get(index).entries().size());
            }
            int panelHeight = 31 + maxEntries * ROW_HEIGHT;
            for (int column = 0; column < rowCount; column++) {
                Category category = CATEGORIES.get(first + column);
                int x = startX + column * (panelWidth + GAP);
                panels.add(new PanelLayout(category, x, fullY - scrollOffset, panelWidth, panelHeight));
            }
            fullY += panelHeight + GAP;
        }

        int viewportHeight = Math.max(1, this.height - VIEW_TOP - VIEW_BOTTOM_MARGIN);
        maxScroll = Math.max(0, fullY - GAP - VIEW_TOP - viewportHeight);
        scrollOffset = Math.min(scrollOffset, maxScroll);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        context.fill(8, 7, this.width - 8, 35, 0xDD10151B);
        context.drawTextWithShadow(this.textRenderer, this.title, 15, 10, 0xFFFFFFFF);
        if (this.width > 620) {
            context.drawTextWithShadow(this.textRenderer, Text.literal("Most switches are visual only"),
                15, 22, 0xFFB9C5C0);
        }
        int searchX = this.width - 132;
        context.fill(searchX, 12, this.width - 16, 31, 0xFF252D34);
        context.drawTextWithShadow(this.textRenderer, Text.literal("Search..."), searchX + 7, 18, 0xFF8A9690);

        context.enableScissor(8, VIEW_TOP, this.width - 8, this.height - VIEW_BOTTOM_MARGIN);
        for (PanelLayout panel : panels) {
            int x = panel.x();
            int y = panel.y();
            context.fill(x, y, x + panel.width(), y + panel.height(), 0xD910141A);
            context.fill(x, y, x + 3, y + panel.height(), 0xFF4B9C79);
                context.drawTextWithShadow(this.textRenderer, Text.literal(panel.category().title()), x + 9, y + 8,
                    0xFFE1EAE5);
                context.fill(x + 9, y + 21, x + panel.width() - 9, y + 22, 0x554F6A60);

            List<ModuleEntry> entries = panel.category().entries();
            for (int index = 0; index < entries.size(); index++) {
                ModuleEntry entry = entries.get(index);
                int rowY = y + 25 + index * ROW_HEIGHT;
                int color = entry.isEnabled() ? 0xFF9FB7AD : 0xFF7C8582;
                context.drawTextWithShadow(this.textRenderer, Text.literal(entry.label()), x + 9, rowY + 3, color);
                drawSwitch(context, x + panel.width() - 24, rowY + 3, entry.isEnabled());
            }
        }
        context.disableScissor();

        int closeX = this.width / 2 - 38;
        int closeY = this.height - 26;
        context.fill(closeX, closeY, closeX + 76, closeY + 18, 0xFF303B42);
        context.drawCenteredTextWithShadow(this.textRenderer, Text.literal("Close"), this.width / 2, closeY + 5,
                0xFFFFFFFF);
        if (maxScroll > 0) {
            context.drawTextWithShadow(this.textRenderer, Text.literal("Scroll to browse"), 12,
                this.height - 22, 0xFFB9C5C0);
        }
    }

    private void drawSwitch(DrawContext context, int x, int y, boolean enabled) {
        int color = enabled ? 0xFF426B8C : 0xFF3B4145;
        context.fill(x, y, x + 16, y + 8, color);
        int knobX = enabled ? x + 10 : x + 2;
        context.fill(knobX, y + 1, knobX + 5, y + 7, enabled ? 0xFFDCEAF2 : 0xFF9AA19E);
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubleClick) {
        double mouseX = click.x();
        double mouseY = click.y();
        int closeX = this.width / 2 - 38;
        int closeY = this.height - 26;
        if (click.button() == 0 && mouseX >= closeX && mouseX <= closeX + 76
            && mouseY >= closeY && mouseY <= closeY + 18) {
            close();
            return true;
        }

        if (click.button() == 0 && mouseY >= VIEW_TOP && mouseY < this.height - VIEW_BOTTOM_MARGIN) {
            for (PanelLayout panel : panels) {
                if (mouseX < panel.x() || mouseX >= panel.x() + panel.width()) {
                    continue;
                }
                int rowOffset = (int) mouseY - panel.y() - 25;
                if (rowOffset < 0) {
                    continue;
                }
                int index = rowOffset / ROW_HEIGHT;
                if (index >= 0 && index < panel.category().entries().size()) {
                    ModuleEntry entry = panel.category().entries().get(index);
                    entry.toggle();
                    return true;
                }
            }
        }
        return super.mouseClicked(click, doubleClick);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (maxScroll > 0 && mouseY >= VIEW_TOP && mouseY < this.height - VIEW_BOTTOM_MARGIN) {
            scrollOffset = Math.max(0, Math.min(maxScroll, scrollOffset - (int) (verticalAmount * 24)));
            rebuildLayout();
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public void close() {
        if (this.client != null) {
            this.client.setScreen(this.parent);
        }
    }

    private static final class ModuleEntry {
        private final String label;
        private final Supplier<Boolean> state;
        private final Consumer<Boolean> setter;
        private boolean decorativeState;

        private ModuleEntry(String label, Supplier<Boolean> state, Consumer<Boolean> setter, boolean decorativeState) {
            this.label = label;
            this.state = state;
            this.setter = setter;
            this.decorativeState = decorativeState;
        }

        private static ModuleEntry live(String label, Supplier<Boolean> state, Consumer<Boolean> setter) {
            return new ModuleEntry(label, state, setter, false);
        }

        private static ModuleEntry decorative(String label, boolean visualState) {
            return new ModuleEntry(label, null, null, visualState);
        }

        private String label() {
            return label;
        }

        private boolean isEnabled() {
            return state == null ? decorativeState : state.get();
        }

        private void toggle() {
            if (state == null) {
                decorativeState = !decorativeState;
            } else {
                setter.accept(!state.get());
            }
        }
    }

    private record Category(String title, boolean interactive, List<ModuleEntry> entries) {
    }

    private record PanelLayout(Category category, int x, int y, int width, int height) {
    }
}
