package pl.paisley4.pipbans.utils;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import pl.paisley4.pipbans.objects.User;
import pl.paisley4.pipbans.pIPBans;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileData {

    public static List<User> users = new ArrayList<>();

    public static void loadUsers(){
        users.clear();
        File usersFolder = new File(pIPBans.getInstance().getDataFolder(), "users");
        if(!usersFolder.exists()){
            usersFolder.mkdir();
            return;
        }
        if(usersFolder.listFiles()==null){
            return;
        }
        for(File file : usersFolder.listFiles()){
            if(!file.getName().endsWith(".yml")){
                break;
            }
            try{
                Configuration userconf = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
                Oclock oclock = new Oclock();
                if(userconf.get("endsBan")==null){
                    oclock = null;
                }else{
                    String[] form = userconf.getString("endsBan").split(":");
                    oclock.setHour(Integer.parseInt(form[0]));
                    oclock.setMinute(Integer.parseInt(form[1]));
                    oclock.setSecond(Integer.parseInt(form[2]));
                    oclock.setDay(Integer.parseInt(form[3]));
                    oclock.setMonth(Integer.parseInt(form[4]));
                    oclock.setYear(Integer.parseInt(form[5]));
                }
                User user = new User(file.getName().replace(".yml", ""), userconf.getString("address"), oclock, userconf.getString("banner"), userconf.getString("reason"));
                users.add(user);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public static void saveUsers(){
        File usersFolder = new File(pIPBans.getInstance().getDataFolder(), "users");
        for(File file : usersFolder.listFiles()){
            file.delete();
        }
        for(User user : users){
            File file = new File(usersFolder, user.getUsername()+".yml");
            try{
                file.createNewFile();
                Configuration userconf = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
                userconf.set("address", user.getIp());
                if(user.hasBan()){
                    userconf.set("endsBan", user.getBanEnds().toGetterString());
                    userconf.set("banner", user.getBanner());
                    userconf.set("reason", user.getReason());
                }else{
                    userconf.set("endsBan", null);
                    userconf.set("banner", null);
                    userconf.set("reason", null);
                }
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(userconf, file);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public static User getUser(String username){
        for(User user : users){
            if(user.getUsername().equalsIgnoreCase(username)){
                return user;
            }
        }
        return null;
    }

    public static List<User> getUsersByIP(String ip){
        List<User> us = new ArrayList<>();
        for(User user : users){
            if(user.getIp().equalsIgnoreCase(ip)){
                us.add(user);
            }
        }
        return us;
    }

    public static void add(User user){
        users.add(user);
    }

}
