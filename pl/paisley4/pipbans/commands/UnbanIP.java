package pl.paisley4.pipbans.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import pl.paisley4.pipbans.MySQL;
import pl.paisley4.pipbans.objects.User;
import pl.paisley4.pipbans.pIPBans;
import pl.paisley4.pipbans.utils.FileData;
import pl.paisley4.pipbans.utils.Messages;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UnbanIP extends Command {

    public UnbanIP(){
        super("unbanip", "ipbans.unbanip", "pardon-ip", "unlock-ip", "unban-ip");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!sender.hasPermission("ipbans.unbanip")){
            sender.sendMessage(Messages.getMessage("no-permission"));
            return;
        }
        if (args.length!=1) {
            sender.sendMessage(Messages.getUsage("unbanip"));
            return;
        }
        if(pIPBans.useMysql){
            String ip = args[0];
            if(!args[0].contains(".")){
                try{
                    PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT * FROM `player_data` WHERE nick=?");
                    ps.setString(1, args[0]);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        ip = rs.getString(3);
                    } else {
                        sender.sendMessage(Messages.getMessage("player-not-exists"));
                        return;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            try {
                PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT * FROM `ip_bans` WHERE ip=?");
                ps.setString(1, ip);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    PreparedStatement d = MySQL.getConnection().prepareStatement("DELETE FROM `ip_bans` WHERE ip=?");
                    d.setString(1, ip);
                    d.executeUpdate();
                    sender.sendMessage(Messages.getMessage("player-got-unban"));
                } else {
                    sender.sendMessage(Messages.getMessage("player-has-not-ban"));
                    return;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            String ip = args[0];
            if(!args[0].contains(".")){
                if(FileData.getUser(args[0])==null){
                    sender.sendMessage(Messages.getMessage("player-not-exists"));
                    return;
                }
                ip = FileData.getUser(args[0]).getIp();
            }
            for(User user : FileData.getUsersByIP(ip)){
                if(user.hasBan()){
                    user.setBanEnds(null);
                    user.setReason(null);
                    user.setBanner(null);
                    sender.sendMessage(Messages.getMessage("player-got-unban"));
                    return;
                }else{
                    sender.sendMessage(Messages.getMessage("player-has-not-ban"));
                }
            }
        }
    }
}
