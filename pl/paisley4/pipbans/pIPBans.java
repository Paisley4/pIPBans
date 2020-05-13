package pl.paisley4.pipbans;

import net.md_5.bungee.api.plugin.Plugin;
import pl.paisley4.pipbans.utils.FileData;
import pl.paisley4.pipbans.utils.Messages;

public class pIPBans extends Plugin {

    private static pIPBans instance;

    public static boolean useMysql;

    public void onEnable(){
        instance = this;
        Config.check();
        useMysql = Config.getConfig().getString("DATA_SAVE_TYPE").equalsIgnoreCase("MYSQL");
        if(Config.getConfig().getString("DATA_SAVE_TYPE").equalsIgnoreCase("MYSQL")){
            if(!MySQL.mysqlSetup(Config.getConfig().getString("mysql.host"),
                    Config.getConfig().getInt("mysql.port"),
                    Config.getConfig().getString("mysql.database"),
                    Config.getConfig().getString("mysql.username"),
                    Config.getConfig().getString("mysql.password"))){
                return;
            }
        }else{
            FileData.loadUsers();
        }
        Messages.loadMessages();
        getInstance().getProxy().getPluginManager().registerListener(getInstance(), new pl.paisley4.pipbans.listeners.Join());
        getInstance().getProxy().getPluginManager().registerCommand(getInstance(), new pl.paisley4.pipbans.commands.UnbanIP());
        getInstance().getProxy().getPluginManager().registerCommand(getInstance(), new pl.paisley4.pipbans.commands.TempbanIP());
        getInstance().getProxy().getPluginManager().registerCommand(getInstance(), new pl.paisley4.pipbans.commands.BanIP());
    }

    public void onDisable(){
        FileData.saveUsers();
    }

    public static pIPBans getInstance(){
        return instance;
    }

}
