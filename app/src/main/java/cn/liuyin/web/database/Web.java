package cn.liuyin.web.database;

public class Web {
    public int _id;
    public int day;
    public String time;
    public String title;
    public String url;

    public Web() {

    }

    public Web(int day, String time, String title, String url) {
        this.day = day;
        this.time = time;
        this.title = title;
        this.url = url;
    }

    public Web(int id, int day, String time, String title, String url) {
        this._id = id;
        this.day = day;
        this.time = time;
        this.title = title;
        this.url = url;
    }

    public String getTitle() {
        return this.title;
    }

    public String getTime() {
        return this.time;
    }

    public String getUrl() {
        return this.url;
    }

    public int getDay() {
        return this.day;
    }
}
