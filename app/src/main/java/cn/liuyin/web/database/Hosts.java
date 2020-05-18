package cn.liuyin.web.database;

/**
 * Created by asus on 2017/9/14.
 */

public class Hosts {
    public String domain;
    public String ip;

    public Hosts(String ip, String domain) {
        this.domain = domain;
        this.ip = ip;
    }
}
