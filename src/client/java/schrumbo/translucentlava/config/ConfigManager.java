package schrumbo.translucentlava.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Used for handling the config writing and reading
 */
public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Logger LOGGER =  LoggerFactory.getLogger(ConfigManager.class.getName());

    public static Config config = new Config();

    private final MinecraftClient client = MinecraftClient.getInstance();

    private static final File CONFIG_FILE = new File(
            FabricLoader.getInstance().getConfigDir().toFile(), "translucentlava/config.json"
    );



    private static final File CONFIG_DIRECTORY = new File(FabricLoader.getInstance().getConfigDir().toFile(), "translucentlava");

    /**
     * loads config from file - creates new if needed
     */
    public static Config load(){
        checkConfigDirectory();

        if(!CONFIG_FILE.exists()){
            LOGGER.info("creating new config file");
            save();
            return new Config();
        }

        try(FileReader reader = new FileReader(CONFIG_FILE)){
            config = GSON.fromJson(reader, Config.class);
        }catch(IOException e){
            config = new Config();
            LOGGER.info("failed to read config, using default");
        }
        return config;
    }

    /**
     * writes config changes to file
     */
    public static void save(){
        try(FileWriter writer = new FileWriter(CONFIG_FILE)){
            GSON.toJson(config, writer);
        }catch (IOException e){
            LOGGER.info("failed to write changes to config" + e);
        }
    }


    /**
     * ensures that the config directory is existent
     */
    private static void checkConfigDirectory(){
        if (!CONFIG_DIRECTORY.exists()){
            CONFIG_DIRECTORY.mkdirs();
        }
    }


}
