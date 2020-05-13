package pl.paisley4.pipbans.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import pl.paisley4.pipbans.MySQL;
import pl.paisley4.pipbans.objects.User;
import pl.paisley4.pipbans.pIPBans;
import pl.paisley4.pipbans.utils.FileData;
import pl.paisley4.pipbans.utils.Messages;
import pl.paisley4.pipbans.utils.Oclock;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BanIP extends Command {

    public BanIP(){
        super("banip", "ipbans.banip", "ban-ip", "block-ip");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!sender.hasPermission("ipbans.banip")){
            sender.sendMessage(Messages.getMessage("no-permission"));
            return;
        }
        if(args.length<2){
            sender.sendMessage(Messages.getUsage("banip"));
            return;
        }
        if(pIPBans.useMysql){
            try{
                PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT * FROM `ip_bans` WHERE ip=?");
                if(!args[0].contains(".")){
                    PreparedStatement d = MySQL.getConnection().prepareStatement("SELECT * FROM `player_data` WHERE nick=?");
                    d.setString(1, args[0]);
                    if(d.executeQuery().next()){
                        ps.setString(1, d.executeQuery().getString(3));
                    }
                }else{
                    ps.setString(1, args[0]);
                }
                if(ps.executeQuery().next()){
                    sender.sendMessage(Messages.getMessage("player-has-ban"));
                    return;
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
            String ip = args[0];
            if(!args[0].contains(".")){
                try{
                    PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT * FROM `player_data` WHERE nicke=?");
                    ps.setString(1, args[0]);
                    ResultSet rs = ps.executeQuery();
                    if(rs.next()){
                        ip = rs.getString(3);
                    }else{
                        sender.sendMessage(Messages.getMessage("player-not-exists"));
                        return;
                    }
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
            String by = sender.getName();
            String reason = "";
            int i = 0;
            for(String s : args){
                if(i!=0){
                    reason+=(s+" ");
                }
                i++;
            }
            String todate = "0";
            try{
                PreparedStatement ps = MySQL.getConnection().prepareStatement("INSERT INTO `ip_bans` (ip,todate,reason,banner) VALUES (?,?,?,?)");
                ps.setString(1, ip);
                ps.setString(2, todate);
                ps.setString(3, reason);
                ps.setString(4, by);
                ps.executeUpdate();
            }catch (SQLException e){
                e.printStackTrace();
            }

            for(ProxiedPlayer player : pIPBans.getInstance().getProxy().getPlayers()) {
                try {
                    PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT * FROM `player_data` WHERE nick=?");
                    ps.setString(1, player.getName());
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        if (rs.getString(3).equalsIgnoreCase(ip)) {
                            player.disconnect(Messages.getMessage("ip-perm-kick").replace("<enter>", "\n"));
                            pIPBans.getInstance().getProxy().broadcast((Messages.getMessage("ip-perm-info").replace("<player>", player.getName()).replace("<banner>", sender.getName()).replace("<enter>", "\n")));
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
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
            String reason = "";
            int i = 0;
            for(String s : args){
                if(i!=0){
                    reason+=(s+" ");
                }
                i++;
            }
            Oclock oclock = new Oclock();
            oclock.setYear(0);
            oclock.setMonth(0);
            oclock.setDay(0);
            oclock.setHour(0);
            oclock.setMinute(0);
            oclock.setSecond(0);
            for(User user : FileData.getUsersByIP(ip)){
                if(user.getIp().equalsIgnoreCase(ip)){
                    if(user.hasBan()){
                        sender.sendMessage(Messages.getMessage("player-has-ban"));
                        return;
                    }
                    user.setReason(reason);
                    user.setBanner(sender.getName());
                    user.setBanEnds(oclock);
                    pIPBans.getInstance().getProxy().getPlayer(user.getUsername()).disconnect(Messages.getMessage("ip-perm-kick").replace("<enter>", "\n").replace("<reason>", reason).replace("<banner>", sender.getName()));
                    pIPBans.getInstance().getProxy().broadcast((Messages.getMessage("ip-perm-info").replace("<reason>", reason).replace("<player>", pIPBans.getInstance().getProxy().getPlayer(user.getUsername()).getName()).replace("<banner>", sender.getName()).replace("<enter>", "\n")));
                }
            }
        }
    }
}
