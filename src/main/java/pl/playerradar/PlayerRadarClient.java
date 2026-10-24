package pl.playerradar;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class PlayerRadarClient implements ClientModInitializer {
    private static KeyBinding openRadar;

    @Override
    public void onInitializeClient() {
        openRadar = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.player_radar.open",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_8,
                KeyBinding.Category.MISC
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openRadar.wasPressed()) {
                if (client.currentScreen instanceof RadarScreen) {
                    client.setScreen(null);
                } else if (client.player != null && client.world != null) {
                    client.setScreen(new RadarScreen());
                }
            }
        });
    }
}
