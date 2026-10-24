package pl.playerradar;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class RadarScreen extends Screen {
    private static final double RANGE = 40.0;
    private String search = "";
    private int scroll = 0;
    private int panelX, panelY, panelW = 300, panelH = 270;
    private boolean searchFocused = false;

    public RadarScreen() {
        super(Text.literal("Player Radar"));
    }

    private record NearbyPlayer(String name, double distance) {}

    private List<NearbyPlayer> getNearbyPlayers() {
        List<NearbyPlayer> players = new ArrayList<>();
        if (client == null || client.player == null || client.world == null) return players;

        for (AbstractClientPlayerEntity other : client.world.getPlayers()) {
            if (other == client.player) continue;
            double distance = client.player.distanceTo(other);
            if (distance <= RANGE) {
                String name = other.getName().getString();
                if (search.isBlank() || name.toLowerCase(Locale.ROOT).contains(search.toLowerCase(Locale.ROOT))) {
                    players.add(new NearbyPlayer(name, distance));
                }
            }
        }
        players.sort(Comparator.comparingDouble(NearbyPlayer::distance));
        return players;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);

        panelX = (width - panelW) / 2;
        panelY = (height - panelH) / 2;
        int x = panelX, y = panelY;

        // Shadow and panel
        context.fill(x - 3, y - 3, x + panelW + 3, y + panelH + 3, 0x55000000);
        context.fill(x, y, x + panelW, y + panelH, 0xF0101220);
        context.fill(x, y, x + panelW, y + 3, 0xFF9B6DFF);
        context.fill(x, y + 3, x + panelW, y + 44, 0xFF191B31);

        context.drawTextWithShadow(textRenderer, Text.literal("◈ PLAYER RADAR").formatted(Formatting.WHITE, Formatting.BOLD), x + 16, y + 14, 0xFFFFFFFF);
        context.drawTextWithShadow(textRenderer, Text.literal("1.21.4 • NEARBY").formatted(Formatting.GRAY), x + 16, y + 29, 0xFFB9B8D4);

        context.fill(x + panelW - 34, y + 10, x + panelW - 12, y + 32, 0xFF302443);
        context.drawTextWithShadow(textRenderer, Text.literal("×").formatted(Formatting.WHITE), x + panelW - 27, y + 15, 0xFFFFFFFF);

        List<NearbyPlayer> players = getNearbyPlayers();
        context.drawTextWithShadow(textRenderer, Text.literal("NEARBY PLAYERS").formatted(Formatting.GRAY), x + 16, y + 56, 0xFFB9B8D4);
        String count = String.valueOf(players.size());
        context.fill(x + panelW - 42, y + 51, x + panelW - 16, y + 69, 0xFF26364A);
        context.drawTextWithShadow(textRenderer, Text.literal(count).formatted(Formatting.AQUA, Formatting.BOLD), x + panelW - 34, y + 56, 0xFFFFFFFF);

        // Search box
        context.fill(x + 14, y + 78, x + panelW - 14, y + 101, 0xFF20243A);
        context.drawTextWithShadow(textRenderer,
                Text.literal("⌕ " + (search.isEmpty() ? "Search players..." : search)).formatted(search.isEmpty() ? Formatting.DARK_GRAY : Formatting.WHITE),
                x + 23, y + 86, 0xFFFFFFFF);

        int listTop = y + 111;
        int listBottom = y + panelH - 35;
        context.fill(x + 12, listTop, x + panelW - 12, listBottom, 0xAA141827);

        context.enableScissor(x + 12, listTop, x + panelW - 12, listBottom);
        int rowY = listTop + 5 - scroll;
        if (players.isEmpty()) {
            String empty = client == null || client.player == null ? "Join a world to scan players" : "No players found within 40 blocks";
            context.drawTextWithShadow(textRenderer, Text.literal(empty).formatted(Formatting.GRAY), x + 23, rowY + 7, 0xFFFFFFFF);
        } else {
            for (NearbyPlayer player : players) {
                if (rowY + 27 >= listTop && rowY <= listBottom) {
                    context.fill(x + 17, rowY, x + panelW - 17, rowY + 27, 0xFF20263A);
                    context.fill(x + 17, rowY, x + 20, rowY + 27, 0xFF66D9C1);
                    context.drawTextWithShadow(textRenderer, Text.literal(player.name()).formatted(Formatting.WHITE), x + 28, rowY + 5, 0xFFFFFFFF);
                    context.drawTextWithShadow(textRenderer, Text.literal(String.format(Locale.ROOT, "%.1f bl", player.distance())).formatted(Formatting.AQUA), x + panelW - 77, rowY + 5, 0xFFFFFFFF);
                    context.drawTextWithShadow(textRenderer, Text.literal("PLAYER").formatted(Formatting.DARK_GRAY), x + 28, rowY + 16, 0xFF9999B5);
                }
                rowY += 31;
            }
        }
        context.disableScissor();

        context.fill(x, y + panelH - 27, x + panelW, y + panelH, 0xFF191B31);
        context.drawTextWithShadow(textRenderer, Text.literal("RANGE 40 BLOCKS").formatted(Formatting.GRAY), x + 15, y + panelH - 17, 0xFFB9B8D4);
        context.drawTextWithShadow(textRenderer, Text.literal("● LIVE").formatted(Formatting.GREEN), x + panelW - 58, y + panelH - 17, 0xFFFFFFFF);

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            if (mouseX >= panelX + panelW - 38 && mouseX <= panelX + panelW - 8
                    && mouseY >= panelY + 7 && mouseY <= panelY + 36) {
                close();
                return true;
            }
            searchFocused = mouseX >= panelX + 14 && mouseX <= panelX + panelW - 14
                    && mouseY >= panelY + 78 && mouseY <= panelY + 101;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        if (searchFocused && !Character.isISOControl(chr) && search.length() < 32) {
            search += chr;
            scroll = 0;
            return true;
        }
        return super.charTyped(chr, modifiers);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (searchFocused) {
            if (keyCode == 256) {
                searchFocused = false;
                return true;
            }
            if (keyCode == 259 && !search.isEmpty()) {
                search = search.substring(0, search.length() - 1);
                scroll = 0;
                return true;
            }
        }
        if (keyCode == 256) {
            close();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        scroll = Math.max(0, scroll - (int) (verticalAmount * 31));
        return true;
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
