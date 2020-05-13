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
import java.util.Date;

public class TempbanIP extends Command {

    public TempbanIP(){
        super("tempbanip", "ipbans.tempbanip", "tempban-ip", "tempblock-ip");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!sender.hasPermission("ipbans.tempbanip")){
            sender.sendMessage(Messages.getMessage("no-permission"));
            return;
        }
        if(args.length<4){
            sender.sendMessage(Messages.getUsage("tempbanip"));
            return;
        }
        if(pIPBans.useMysql){
            try{
                PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT * FROM `ip_bans` WHERE ip=?");
                if(!args[0].contains(".")){
                    PreparedStatement d = MySQL.getConnection().prepareStatement("SELECT * FROM `player_data` WHERE nick=?");
                    d.setString(1, args[0]);
                    ResultSet rs = d.executeQuery();
                    if(rs.next()){
                        ps.setString(1, rs.getString(3));
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
                    PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT * FROM `player_data` WHERE nick=?");
                    ps.setString(1, args[0]);
                    ResultSet rs = ps.executeQuery();
                    if(rs.next()){
                        ip = rs.getString(3);
                    }
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
            String by = sender.getName();
            String reason = "";
            int i = 0;
            for(String s : args){
                if(i!=0&&i!=1&&i!=2){
                    reason+=(s+" ");
                }
                i++;
            }
            Oclock oclock = new Oclock(new Date());
            {
                if(args[2].equalsIgnoreCase("month")||args[2].equalsIgnoreCase("months")||args[2].equalsIgnoreCase("mon")){
                    oclock.addMonths(Integer.parseInt(args[1]));
                }
                if(args[2].equalsIgnoreCase("day")||args[2].equalsIgnoreCase("days")||args[2].equalsIgnoreCase("d")){
                    oclock.addDays(Integer.parseInt(args[1]));
                }
                if(args[2].equalsIgnoreCase("hour")||args[2].equalsIgnoreCase("hours")||args[2].equalsIgnoreCase("h")){
                    oclock.addHours(Integer.parseInt(args[1]));
                }
                if(args[2].equalsIgnoreCase("minute")||args[2].equalsIgnoreCase("minutes")||args[2].equalsIgnoreCase("m")){
                    oclock.addMinutes(Integer.parseInt(args[1]));
                }
                if(args[2].equalsIgnoreCase("second")||args[2].equalsIgnoreCase("seconds")||args[2].equalsIgnoreCase("s")){
                    oclock.addSeconds(Integer.parseInt(args[1]));
                }
            }
            String todate = oclock.toGetterString();
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

            for(ProxiedPlayer player : pIPBans.getInstance().getProxy().getPlayers()){
                try{
                    PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT * FROM `player_data` WHERE nick=?");
                    ps.setString(1, player.getName());
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()){
                        if(rs.getString(3).equalsIgnoreCase(ip)){
                            player.disconnect(Messages.getMessage("ip-temp-kick").replace("<ends>", oclock.toString()).replace("<enter>", "\n"));
                            pIPBans.getInstance().getProxy().broadcast(((Messages.getMessage("ip-temp-info").replace("<player>", player.getName()).replace("<banner>", sender.getName()).replace("<ends>", oclock.toString()).replace("<enter>", "\n"))));
                        }
                    }
                }catch (SQLException e){
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
                if(i!=0&&i!=1&&i!=2){
                    reason+=(s+" ");
                }
                i++;
            }
            Oclock oclock = new Oclock(new Date());
            if(args[2].equalsIgnoreCase("month")||args[2].equalsIgnoreCase("months")||args[2].equalsIgnoreCase("mon")){
                oclock.addMonths(Integer.parseInt(args[1]));
            }
            if(args[2].equalsIgnoreCase("day")||args[2].equalsIgnoreCase("days")||args[2].equalsIgnoreCase("d")){
                oclock.addDays(Integer.parseInt(args[1]));
            }
            if(args[2].equalsIgnoreCase("hour")||args[2].equalsIgnoreCase("hours")||args[2].equalsIgnoreCase("h")){
                oclock.addHours(Integer.parseInt(args[1]));
            }
            if(args[2].equalsIgnoreCase("minute")||args[2].equalsIgnoreCase("minutes")||args[2].equalsIgnoreCase("m")){
                oclock.addMinutes(Integer.parseInt(args[1]));
            }
            if(args[2].equalsIgnoreCase("second")||args[2].equalsIgnoreCase("seconds")||args[2].equalsIgnoreCase("s")){
                oclock.addSeconds(Integer.parseInt(args[1]));
            }
            for(User user : FileData.getUsersByIP(ip)){
                if(user.getIp().equalsIgnoreCase(ip)){
                    if(user.hasBan()){
                        sender.sendMessage(Messages.getMessage("player-has-ban"));
                        return;
                    }
                    user.setReason(reason);
                    user.setBanner(sender.getName());
                    user.setBanEnds(oclock);
                    pIPBans.getInstance().getProxy().getPlayer(user.getUsername()).disconnect(Messages.getMessage("ip-temp-kick").replace("<enter>", "\n").replace("<reason>", reason).replace("<banner>", sender.getName()).replace("<ends>", oclock.toString()));
                    pIPBans.getInstance().getProxy().broadcast((Messages.getMessage("ip-temp-info").replace("<ends>", oclock.toString()).replace("<reason>", reason).replace("<player>", pIPBans.getInstance().getProxy().getPlayer(user.getUsername()).getName()).replace("<banner>", sender.getName()).replace("<enter>", "\n")));
                }
            }
        }
    }
}
