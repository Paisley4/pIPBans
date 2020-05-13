package pl.paisley4.pipbans;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class Config {

    private static File file = new File(pIPBans.getInstance().getDataFolder(), "config.yml");

    public static Configuration getConfig(){
        try{
            return ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public static void check(){
        if(!file.exists()){
            if (!pIPBans.getInstance().getDataFolder().exists())
                pIPBans.getInstance().getDataFolder().mkdir();

            File file = new File(pIPBans.getInstance().getDataFolder(), "config.yml");


            if (!file.exists()) {
                try (InputStream in = pIPBans.getInstance().getResourceAsStream("config.yml")) {
                    Files.copy(in, file.toPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
