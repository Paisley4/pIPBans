package pl.paisley4.pipbans;

import net.md_5.bungee.BungeeCord;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySQL {

    private static Connection connection;
    public static String host, database, username, password;
    public static int port;

    public static boolean mysqlSetup(String host, int port, String database, String username, String password){
        MySQL.host = host;
        MySQL.port = port;
        MySQL.database = database;
        MySQL.username = username;
        MySQL.password = password;

        try{
            synchronized (new MySQL()){
                if(getConnection() != null && !getConnection().isClosed()){
                    return true;
                }

                Class.forName("com.mysql.jdbc.Driver");
                setConnection(DriverManager.getConnection("jdbc:mysql://" + host + ":"
                        + port + "/" + database, username, password));
            }
        }catch(SQLException e){
            BungeeCord.getInstance().getConsole().sendMessage("Problem with connection to database!");
            BungeeCord.getInstance().getConsole().sendMessage("Closing...");
            return false;
        }catch(ClassNotFoundException e){
            BungeeCord.getInstance().getConsole().sendMessage("I didn't found \"com.mysql.jdbc.Driver\"!");
            BungeeCord.getInstance().getConsole().sendMessage("Closing...");
            return false;
        }

        try{
            PreparedStatement ps = getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS player_data (nick varchar(255)," +
                    " uuid varchar(255)," +
                    " ip varchar(255))");
            ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }

        try{
            PreparedStatement ps = getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS ip_bans (ip varchar(255)," +
                    " todate varchar(255)," +
                    " reason varchar(255)," +
                    " banner varchar(255))");
            ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return true;
    }

    public static Connection getConnection(){
        return connection;
    }

    public static void setConnection(Connection conn){
        connection = conn;
    }

}
