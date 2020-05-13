package pl.paisley4.pipbans.objects;

import pl.paisley4.pipbans.utils.Oclock;

public class User {

    private String username;
    private String ip;
    private Oclock banEnds;
    private String banner;
    private String reason;

    public User(){}

    public User(String username, String ip, Oclock banEnds, String banner, String reason){
        this.username = username;
        this.ip = ip;
        this.banEnds = banEnds;
        this.banner = banner;
        this.reason = reason;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Oclock getBanEnds() {
        return banEnds;
    }

    public void setBanEnds(Oclock banEnds) {
        this.banEnds = banEnds;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public boolean hasBan(){
        return banEnds!=null;
    }

}
