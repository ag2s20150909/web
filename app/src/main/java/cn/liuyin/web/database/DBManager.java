package cn.liuyin.web.database;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import cn.liuyin.web.function.UIClass;

import static cn.liuyin.web.database.DatabaseHelper.TABLE_NAME3;
import static cn.liuyin.web.database.DatabaseHelper.TABLE_NAME4;
import static cn.liuyin.web.database.DatabaseHelper.TABLE_NAME5;


public class DBManager {
    private SQLiteDatabase db;
    private Context mContext;

    public DBManager(Context context) {
        this.mContext = context;
        // 因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0,
        // mFactory);
        // 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        //db =  new DatabaseHelper(mContext).getWritableDatabase();
    }


    //---------------------------------历史纪录-------------------------------------------//
    //去重
    public void deleteDuplicates() {
        db = new DatabaseHelper(mContext).getWritableDatabase();
        String sql = "delete " + DatabaseHelper.TABLE_NAME2 + " " +
                "where [_id] not in (" +
                "select max([_id]) from " + DatabaseHelper.TABLE_NAME2 + " " +
                "group by (host + url))";
        //sql=String.format(sql,DatabaseHelper.TABLE_NAME2,DatabaseHelper.TABLE_NAME2);
        db.beginTransaction();
        try {
            //db.execSQL(sql);
            db.setTransactionSuccessful(); // 设置事务成功完成
        } catch (Exception e) {
            UIClass.ViewWebSouce(e.toString());
        } finally {
            db.endTransaction(); // 结束事务
            db.close();
        }

    }

    /*历史记录*/
    public void addHistory(Web web) {
        //System.out.println("addHistory start");
        db = new DatabaseHelper(mContext).getWritableDatabase();
        db.beginTransaction(); // 开始事务
        try {
            db.execSQL(String.format("INSERT INTO %s VALUES(null,?, ?, ?, ?)", DatabaseHelper.TABLE_NAME1), new Object[]{web.getDay(), web.getTime(),
                    web.getTitle(), web.getUrl()});
            db.setTransactionSuccessful(); // 设置事务成功完成
        } catch (Exception e) {
            UIClass.ViewWebSouce(e.toString());
        } finally {
            db.endTransaction(); // 结束事务
            db.close();
        }
    }


//    public void updateAge(Web web) {
//        //Log.d(AppConstants.LOG_TAG, "DBManager --> updateAge");
//        ContentValues cv = new ContentValues();
//        cv.put("url", web.getUrl());
//        db.update(DatabaseHelper.TABLE_NAME1, cv, "name = ?",
//                new String[]{web.getTitle()});
//    }


    public void deleteHistoryByDay(int days) {

        db = new DatabaseHelper(mContext).getWritableDatabase();
        db.beginTransaction(); // 开始事务
        try {
            Calendar cal = Calendar.getInstance();
            int day = cal.get(Calendar.DAY_OF_YEAR) - days;
            db.delete(DatabaseHelper.TABLE_NAME1, "day<?", new String[]{day + ""});
            db.setTransactionSuccessful(); // 设置事务成功完成
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction(); // 结束事务
            db.close();
        }
        //删除语句：delete from 表名 where 条件子句。如：delete from person  where id=10


    }

    public void deleteHistoryById(int id) {

        db = new DatabaseHelper(mContext).getWritableDatabase();
        db.beginTransaction(); // 开始事务
        try {
            db.delete(DatabaseHelper.TABLE_NAME1, "_id=?", new String[]{id + ""});
            db.setTransactionSuccessful(); // 设置事务成功完成
        } catch (Exception e) {
            UIClass.ViewWebSouce(e.toString());
        } finally {
            db.endTransaction(); // 结束事务
            db.close();
        }

    }

    public void deleteAllHistory() {
        db = new DatabaseHelper(mContext).getWritableDatabase();
        db.beginTransaction(); // 开始事务
        try {
            db.delete(DatabaseHelper.TABLE_NAME1, "day>?", new String[]{0 + ""});
            db.setTransactionSuccessful(); // 设置事务成功完成
        } finally {
            db.endTransaction(); // 结束事务
            db.close();
        }
    }

    //    public ArrayList<ChaJian> queryChaJian(String url) {
//        ArrayList<ChaJian> chaJiens = new ArrayList<>();
//        Cursor c = db.rawQuery(new StringBuilder().append("SELECT * FROM ").append(TABLE_NAME2).append(" where host =?").toString(),
//                new String[]{url});
//        while (c.moveToNext()) {
//            ChaJian chaJian = new ChaJian(c.getInt(c.getColumnIndex("_id")),
//                    c.getString(c.getColumnIndex("name")),
//                    c.getString(c.getColumnIndex("host")),
//                    c.getString(c.getColumnIndex("url")),
//                    c.getString(c.getColumnIndex("javascript"))
//            );
//
//            chaJiens.add(chaJian);
//        }
//        c.close();
//        db.close();
//        return chaJiens;
//    }
    public List<Web> queryHistory() {
        ArrayList<Web> persons = new ArrayList<>();

        db = new DatabaseHelper(mContext).getWritableDatabase();
        db.beginTransaction(); // 开始事务
        Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME1,
                null);
        while (c.moveToNext()) {
            Web web = new Web(c.getInt(c.getColumnIndex("_id")),
                    c.getInt(c.getColumnIndex("day")),
                    c.getString(c.getColumnIndex("time")),
                    c.getString(c.getColumnIndex("title")),
                    c.getString(c.getColumnIndex("url"))
            );
            persons.add(web);
        }
        c.close();
        db.endTransaction();
        db.close();
        return persons;
    }

    //---------------------------------历史纪录-------------------------------------------//

    //----------------------------------书签----------------------------------------------//

    /*添加书签*/
    public void addShuQian(ShuQian sq) {
        //System.out.println("addHistory start");
        db = new DatabaseHelper(mContext).getWritableDatabase();
        db.beginTransaction(); // 开始事务
        try {
            db.execSQL(String.format("INSERT INTO %s VALUES(null,?, ?)", DatabaseHelper.TABLE_NAME0), new Object[]{sq.title, sq.url});
            db.setTransactionSuccessful(); // 设置事务成功完成
        } catch (Exception e) {
            UIClass.ViewWebSouce(e.toString());
        } finally {
            db.endTransaction(); // 结束事务
            db.close();
        }
    }

    public List<ShuQian> queryShuQian() {
        ArrayList<ShuQian> sqs = new ArrayList<>();

        db = new DatabaseHelper(mContext).getWritableDatabase();
        db.beginTransaction(); // 开始事务
        Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME0,
                null);
        while (c.moveToNext()) {
            ShuQian sq = new ShuQian();
            sq._id = c.getInt(c.getColumnIndex("_id"));
            sq.title = c.getString(c.getColumnIndex("title"));
            sq.url = c.getString(c.getColumnIndex("url"));

            sqs.add(sq);
        }
        c.close();
        db.endTransaction();
        db.close();
        return sqs;
    }

    public void deleteShuQianById(int id) {

        db = new DatabaseHelper(mContext).getWritableDatabase();
        db.beginTransaction(); // 开始事务
        try {
            db.delete(DatabaseHelper.TABLE_NAME0, "_id=?", new String[]{id + ""});
            db.setTransactionSuccessful(); // 设置事务成功完成
        } catch (Exception e) {
            UIClass.ViewWebSouce(e.toString());
        } finally {
            db.endTransaction(); // 结束事务
            db.close();
        }

    }


//------------------------------------书签-----------------------------------------//

    //---------------------------------资源过滤-------------------------------------------//

    public void addADHost(String adhost) {
        //System.out.println("addHistory start");
        db = new DatabaseHelper(mContext).getWritableDatabase();
        db.beginTransaction(); // 开始事务
        try {
            db.execSQL(String.format("INSERT INTO %s VALUES(null,?)", DatabaseHelper.TABLE_NAME3), new Object[]{adhost});
            db.setTransactionSuccessful(); // 设置事务成功完成
        } catch (Exception e) {
            //UIClass.ViewWebSouce(e.toString());
        } finally {
            db.endTransaction(); // 结束事务
            db.close();
        }
    }

    public void addADHost(Collection<String> aDhosts) {
        //System.out.println("addHistory start");
        db = new DatabaseHelper(mContext).getWritableDatabase();
        db.beginTransaction(); // 开始事务
        try {
            delet(TABLE_NAME3);
            for (String adhost : aDhosts) {
                db.execSQL(String.format("INSERT INTO %s VALUES(null,?)", DatabaseHelper.TABLE_NAME3), new Object[]{adhost});
            }

            db.setTransactionSuccessful(); // 设置事务成功完成
        } catch (Exception e) {
            e.printStackTrace();
            //UIClass.ViewWebSouce(e.toString());
        } finally {
            db.endTransaction(); // 结束事务
            db.close();
        }
        deletDD(TABLE_NAME3, "url");

    }

    private void delet(String tablename) {
        db.delete(tablename, null, null);
        db.execSQL("DELETE FROM sqlite_sequence WHERE name = '" + tablename + "';");
    }

    private void deletDD(String table, String key) {

        db = new DatabaseHelper(mContext).getWritableDatabase();
        db.beginTransaction(); // 开始事务
        try {
            String sql = "delete from " + table + " where " + table + ".rowid not in (select min(" + table + ".rowid) from " + table + " group by " + key + ");";
            db.execSQL(sql);
            System.out.println(sql);
            db.setTransactionSuccessful(); // 设置事务成功完成
        } catch (Exception e) {

            System.out.println("《《《《《《《");
            e.printStackTrace();
            //UIClass.ViewWebSouce(e.toString());
        } finally {
            db.endTransaction(); // 结束事务
            db.close();
        }
    }

    public ArrayList<String> getAllHosts() {
        ArrayList<String> hosts = new ArrayList<>();
        db = new DatabaseHelper(mContext).getWritableDatabase();
        db.beginTransaction(); // 开始事务
        try {
            Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME3,
                    null);
            while (c.moveToNext()) {
                hosts.add(c.getString(c.getColumnIndex("url")));
//            Web web = new Web(c.getInt(c.getColumnIndex("_id")),
//                    c.getInt(c.getColumnIndex("day")),
//                    c.getString(c.getColumnIndex("time")),
//                    c.getString(c.getColumnIndex("title")),
//                    c.getString(c.getColumnIndex("url"))
//            );
                //persons.add(web);
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }

        return hosts;
    }
    //---------------------------------资源过滤-------------------------------------------//

    //---------------------------------元素过滤-------------------------------------------//
    public void addAdDiv(ADdiv addiv) {
        db = new DatabaseHelper(mContext).getWritableDatabase();
        db.beginTransaction(); // 开始事务
        try {
            String sql = "INSERT INTO " + DatabaseHelper.TABLE_NAME4 + " VALUES(null,?,?,?)";
            db.execSQL(sql, new Object[]{"资源屏蔽", addiv.mHost, addiv.mCss});
            db.setTransactionSuccessful(); // 设置事务成功完成
        } catch (Exception e) {
            //UIClass.ViewWebSouce(e.toString());
        } finally {
            db.endTransaction(); // 结束事务
            db.close();
        }
    }

    public void addAdDiv(Collection<ADdiv> addivs) {
        db = new DatabaseHelper(mContext).getWritableDatabase();
        db.beginTransaction(); // 开始事务
        try {
            delet(TABLE_NAME4);
            String sql = "INSERT INTO " + DatabaseHelper.TABLE_NAME4 + " VALUES(null,?,?,?)";
            for (ADdiv addiv : addivs) {
                db.execSQL(sql, new Object[]{"资源屏蔽", addiv.mHost, addiv.mCss});
            }
            db.setTransactionSuccessful(); // 设置事务成功完成
        } catch (Exception e) {
            e.printStackTrace();
            //UIClass.ViewWebSouce(e.toString());
        } finally {
            db.endTransaction(); // 结束事务
            db.close();
        }
    }

    public ArrayList<String> getFiterByUrl(String url) {
        ArrayList<String> hosts = new ArrayList<>();
        db = new DatabaseHelper(mContext).getWritableDatabase();
        db.beginTransaction(); // 开始事务
        try {
            System.out.println("?????" + url);
            String sql = "SELECT * FROM " + TABLE_NAME4 + " WHERE host = '" + url + "' OR host='*';";
            System.out.println(sql);
            Cursor c = db.rawQuery(sql,
                    null);
            while (c.moveToNext()) {
                hosts.add(c.getString(c.getColumnIndex("fiter")));
                System.out.println("?????" + c.getString(c.getColumnIndex("fiter")));
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }


        return hosts;
    }

    //---------------------------------元素过滤-------------------------------------------//

    //--------------------------------hosts-----------------------------------------------//
    public void addHosts(Collection<Hosts> hosts) {
        db = new DatabaseHelper(mContext).getWritableDatabase();
        db.beginTransaction(); // 开始事务
        try {
            delet(TABLE_NAME5);
            String sql = "INSERT INTO " + TABLE_NAME5 + " VALUES(null,?,?)";
            for (Hosts host : hosts) {
                db.execSQL(sql, new Object[]{host.ip, host.domain});
            }
            db.setTransactionSuccessful(); // 设置事务成功完成
        } catch (Exception e) {
            e.printStackTrace();
            //UIClass.ViewWebSouce(e.toString());
        } finally {
            db.endTransaction(); // 结束事务
            db.close();
        }
    }

    public ArrayList<String> getHosts(String url) {
        ArrayList<String> hosts = new ArrayList<>();

        db = new DatabaseHelper(mContext).getWritableDatabase();
        db.beginTransaction(); // 开始事务
        try {
            System.out.println("?????" + url);
            String sql = "SELECT * FROM " + TABLE_NAME5 + " WHERE domain = '" + url + "';";
            System.out.println(sql);
            Cursor c = db.rawQuery(sql,
                    null);
            while (c.moveToNext()) {
                hosts.add(c.getString(c.getColumnIndex("ip")));
                //System.out.println("?????" + c.getString(c.getColumnIndex("fiter")));
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }


        return hosts;
    }
    //-------------------------------------------------------------------------------------//


}
