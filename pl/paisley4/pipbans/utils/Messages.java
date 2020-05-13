package pl.paisley4.pipbans.utils;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import pl.paisley4.pipbans.pIPBans;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class Messages {

    private static Map<String, String> messages = new HashMap<>();

    private static Map<String, Map<String, String>> listsOfMessages = new HashMap<>();

    public static void loadMessages(){
        messages.clear();
        File file = new File(pIPBans.getInstance().getDataFolder(), "messages.yml");
        Configuration yaml = null;
        try{
            yaml = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        }catch (IOException e){
            if (!file.exists()) {
                try (InputStream in = pIPBans.getInstance().getResourceAsStream("messages.yml")) {
                    Files.copy(in, file.toPath());
                } catch (IOException d) {
                    d.printStackTrace();
                }
            }
            try{
                yaml = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
            }catch (IOException f){}
        }
        for(String s : yaml.getKeys()){
            if(!s.equalsIgnoreCase("usage")){
                messages.put(s, Utils.c(yaml.getString(s)));
            }
        }

        listsOfMessages.clear();
        for(String s : yaml.getKeys()){
            if(s.equalsIgnoreCase("usage")){
                Configuration cs = yaml.getSection(s);
                Map<String, String> map = new HashMap<>();
                for(String f : cs.getKeys()){
                    map.put(f, Utils.c(cs.getString(f)));
                }
                listsOfMessages.put(s, map);
            }
        }
    }

    public static String getMessage(String name){
        return messages.get(name);
    }

    public static Map<String, String> getListOfMessages(String name){
        Map<String, String> map = new HashMap<>();
        for(String s : listsOfMessages.get(name).keySet()){
            map.put(s, listsOfMessages.get(name).get(s));
        }
        return map;
    }

    public static String getUsage(String command){
        return listsOfMessages.get("usage").get(command);
    }

}
