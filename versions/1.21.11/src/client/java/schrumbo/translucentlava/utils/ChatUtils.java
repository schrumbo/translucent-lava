package schrumbo.translucentlava.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class ChatUtils {
    private static final MinecraftClient client = MinecraftClient.getInstance();

    public static void modMessage(String message){
        if(client.player == null)return;
        client.player.sendMessage(Text.literal("§f" + message), false);
    }


}
