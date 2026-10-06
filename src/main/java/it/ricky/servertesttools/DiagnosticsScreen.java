package it.ricky.servertesttools;

import java.util.ArrayList;
import java.util.List;

import it.ricky.servertesttools.modules.Module;
import it.ricky.servertesttools.modules.ModuleManager;
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

    private final Screen parent;
    private final List<PanelLayout> panels = new ArrayList<>();
    private int scrollOffset;
    private int maxScroll;
    private int columns;

    public DiagnosticsScreen(Screen parent) {
        super(Text.literal("Server Test Tools"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        rebuildLayout();
    }

    private void rebuildLayout() {
        panels.clear();
        Module.Category[] categories = Module.Category.values();
        int contentWidth = Math.max(MIN_PANEL_WIDTH, this.width - 24);
        columns = Math.min(categories.length, Math.max(1, (contentWidth + GAP) / (MIN_PANEL_WIDTH + GAP)));
        int panelWidth = (contentWidth - GAP * (columns - 1)) / columns;
        int startX = (this.width - (panelWidth * columns + GAP * (columns - 1))) / 2;
        int fullY = VIEW_TOP;

        for (int first = 0; first < categories.length; first += columns) {
            int rowCount = Math.min(columns, categories.length - first);
            int maxEntries = 1;
            for (int index = first; index < first + rowCount; index++) {
                maxEntries = Math.max(maxEntries, ModuleManager.getByCategory(categories[index]).size());
            }
            int panelHeight = 31 + maxEntries * ROW_HEIGHT;
            for (int column = 0; column < rowCount; column++) {
                Module.Category category = categories[first + column];
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
            context.drawTextWithShadow(this.textRenderer, Text.literal("Toggle modules on/off"),
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
            context.drawTextWithShadow(this.textRenderer, Text.literal(panel.category().getDisplayName()), x + 9, y + 8, 0xFFE1EAE5);
            context.fill(x + 9, y + 21, x + panel.width() - 9, y + 22, 0x554F6A60);

            List<Module> entries = ModuleManager.getByCategory(panel.category());
            for (int index = 0; index < entries.size(); index++) {
                Module entry = entries.get(index);
                int rowY = y + 25 + index * ROW_HEIGHT;
                int color = entry.isEnabled() ? 0xFF9FB7AD : 0xFF7C8582;
                context.drawTextWithShadow(this.textRenderer, Text.literal(entry.getName()), x + 9, rowY + 3, color);
                drawSwitch(context, x + panel.width() - 24, rowY + 3, entry.isEnabled());
            }
        }
        context.disableScissor();

        int closeX = this.width / 2 - 38;
        int closeY = this.height - 26;
        context.fill(closeX, closeY, closeX + 76, closeY + 18, 0xFF303B42);
        context.drawCenteredTextWithShadow(this.textRenderer, Text.literal("Close"), this.width / 2, closeY + 5, 0xFFFFFFFF);
        if (maxScroll > 0) {
            context.drawTextWithShadow(this.textRenderer, Text.literal("Scroll to browse"), 12, this.height - 22, 0xFFB9C5C0);
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
                if (mouseX < panel.x() || mouseX >= panel.x() + panel.width()) continue;
                int rowOffset = (int) mouseY - panel.y() - 25;
                if (rowOffset < 0) continue;
                int index = rowOffset / ROW_HEIGHT;
                List<Module> entries = ModuleManager.getByCategory(panel.category());
                if (index >= 0 && index < entries.size()) {
                    entries.get(index).toggle();
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

    private record PanelLayout(Module.Category category, int x, int y, int width, int height) {}
}
