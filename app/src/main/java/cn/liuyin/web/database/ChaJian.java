package cn.liuyin.web.database;


public class ChaJian {
    int _id;
    String name = "*";
    String host = "*";
    String path = "*";
    String javascript = "*";


    public ChaJian(String name, String host, String path) {
        if (host.equals("")) {
            host = "*";
        }
        this.name = name;
        this.host = host;
        this.path = path;
        this.javascript = "*";
    }

    public ChaJian(int id, String name, String host, String path, String javascript) {
        this._id = id;
        this.name = name;
        this.host = host;
        this.path = path;
        this.javascript = javascript;
    }


    public String getName() {
        return this.name;
    }

    public String getHost() {
        return this.host;
    }

    public String getPath() {
        return this.path;
    }

    public String getJavascript() {
        return this.javascript;
    }

    public int getId() {
        return this._id;
    }


}
