package schrumbo.translucentlava.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import schrumbo.translucentlava.config.ConfigManager;
import schrumbo.translucentlava.features.TransLava;
import schrumbo.translucentlava.utils.ChatUtils;

import static schrumbo.translucentlava.TranslucentLavaClient.config;


public class Commands {
    public static void register(){
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            toggleCommand(dispatcher);
        });
    }

    public static void toggleCommand(CommandDispatcher<FabricClientCommandSource> dispatcher){
        dispatcher.register(ClientCommandManager.literal("lava")
                .executes(commandContext -> {
                    config.setEnabled(!config.getEnabled());
                    TransLava.update(config.getEnabled());
                    if (config.getEnabled()){
                        ChatUtils.modMessage("Enabled translucent lava");
                    }else{
                        ChatUtils.modMessage("Disabled translucent lava");
                    }
                    ConfigManager.save();
                    return 1;
                })
        );
    }
}

