package pl.paisley4.pipbans.listeners;

import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
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

public class Join implements Listener {

    @EventHandler
    public void onConnect(PostLoginEvent event){
        String frmn = event.getPlayer().getAddress().toString().replace("/", ".").replaceFirst(".", "");
        String ip = (frmn.split(":"))[0];
        if(pIPBans.useMysql){
            try{
                PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT * FROM `ip_bans` WHERE ip=?");
                ps.setString(1, ip);
                ResultSet rs = ps.executeQuery();
                if(rs.next()){
                    if(rs.getString(2).equalsIgnoreCase("0")){
                        event.getPlayer().disconnect(Messages.getMessage("player-ban-perm").replace("<by>", rs.getString(4)).replace("<reason>", rs.getString(3)).replace("<enter>", "\n"));
                        return;
                    }else{
                        Oclock oclock = new Oclock();
                        String[] form = rs.getString(2).split(":");
                        oclock.setHour(Integer.parseInt(form[0]));
                        oclock.setMinute(Integer.parseInt(form[1]));
                        oclock.setSecond(Integer.parseInt(form[2]));
                        oclock.setDay(Integer.parseInt(form[3]));
                        oclock.setMonth(Integer.parseInt(form[4]));
                        oclock.setYear(Integer.parseInt(form[5]));
                        if(new Oclock(new Date()).isAfter(oclock)){
                            PreparedStatement pd = MySQL.getConnection().prepareStatement("DELETE FROM `ip_bans` WHERE ip=?");
                            pd.setString(1, ip);
                            pd.executeUpdate();
                        }else{
                            event.getPlayer().disconnect(Messages.getMessage("player-ban-temp").replace("<by>", rs.getString(4)).replace("<reason>", rs.getString(3)).replace("<ends>", oclock.toString()).replace("<enter>", "\n"));
                        }
                    }
                }
            }catch(SQLException e){
                e.printStackTrace();
            }

            boolean exists = false;
            try{
                PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT * FROM `player_data` WHERE ip=?");
                ps.setString(1, ip);
                exists = ps.executeQuery().next();
            }catch (SQLException e){
                e.printStackTrace();
            }
            if(exists){
                try{
                    PreparedStatement ps = MySQL.getConnection().prepareStatement("UPDATE `player_data` SET ip=? WHERE nick=?");
                    ps.setString(1, ip);
                    ps.setString(2, event.getPlayer().getName());
                    ps.executeUpdate();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }else{
                try{
                    PreparedStatement ps = MySQL.getConnection().prepareStatement("INSERT INTO `player_data` (nick,uuid,ip) VALUES (?,?,?)");
                    ps.setString(1, event.getPlayer().getName());
                    ps.setString(2, event.getPlayer().getUUID());
                    ps.setString(3, ip);
                    ps.executeUpdate();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
        }else{
            if(FileData.getUser(event.getPlayer().getName())!=null){
                User us = FileData.getUser(event.getPlayer().getName());
                if(us.hasBan()){
                    if(us.getBanEnds().getYear()==0){
                        event.getPlayer().disconnect(Messages.getMessage("player-ban-perm").replace("<banner>", us.getBanner()).replace("<reason>", us.getReason()).replace("<enter>", "\n"));
                        return;
                    }
                    if(us.getBanEnds().isAfter(new Oclock(new Date()))){
                        event.getPlayer().disconnect(Messages.getMessage("player-ban-temp").replace("<banner>", us.getBanner()).replace("<reason>", us.getReason()).replace("<ends>", us.getBanEnds().toString()).replace("<enter>", "\n"));
                        return;
                    }
                    us.setBanEnds(null);
                    us.setReason(null);
                    us.setBanner(null);
                }
            }

            boolean tt = false;
            String reason = null;
            String banner = null;
            Oclock oclock = null;
            for(User ut : FileData.getUsersByIP(ip)){
                if(ut.hasBan()){
                    tt = true;
                    reason = ut.getReason();
                    banner = ut.getBanner();
                    oclock = ut.getBanEnds();
                }
            }

            if(tt){
                User user = new User(event.getPlayer().getName(), ip, oclock, banner, reason);
                FileData.add(user);
                if(oclock.getYear()==0){
                    event.getPlayer().disconnect(Messages.getMessage("player-ban-perm").replace("<by>", banner).replace("<reason>", reason).replace("<enter>", "\n"));
                    return;
                }
                event.getPlayer().disconnect(Messages.getMessage("player-ban-temp").replace("<by>", banner).replace("<reason>", reason).replace("<ends>", oclock.toString()).replace("<enter>", "\n"));
                return;
            }

            boolean exists = false;
            User us = FileData.getUser(event.getPlayer().getName());
            if(us==null){
                User user = new User(event.getPlayer().getName(), ip, null, null, null);
                FileData.add(user);
                us = user;
            }
        }
    }

}
