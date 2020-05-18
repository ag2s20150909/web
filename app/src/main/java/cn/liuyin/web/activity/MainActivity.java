package cn.liuyin.web.activity;


import android.app.Activity;
import android.app.DownloadManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.Toast;

import cn.liuyin.web.R;
import cn.liuyin.web.constant.ConstantData;
import cn.liuyin.web.database.DBManager;
import cn.liuyin.web.pages.HomePage;
import cn.liuyin.webview.DownloadCompleteReceiver;
import cn.liuyin.webview.TTSService;


public class MainActivity extends Activity {
    private final String TAG = getClass().getSimpleName();
    private final DownloadCompleteReceiver dcReceiver = new DownloadCompleteReceiver();
    public SharedPreferences pref;
    public SharedPreferences.Editor editor;
    public long exitTime;
    public WebViewFragment mainFragement;
    public TTSService.TTSServiceBinder ttsBinder = null;
    private AudioManager audiomanage;
    private Context context = MainActivity.this;
    private DBManager db;
    private ServiceConnection ttsconn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            ttsBinder = (TTSService.TTSServiceBinder) binder;

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    private ServiceConnection coreconn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {


        }

        @Override
        public void onServiceDisconnected(ComponentName name) {


        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //初始化数据储存
        pref = getSharedPreferences("MyWebData", Context.MODE_PRIVATE);
        editor = pref.edit();
        editor.apply();
        ConstantData.setContext(context);
        ConstantData.setMainActivity(this);
        setContentView(R.layout.main);
        System.out.println(TAG + "onCreate");
        initData();
        new HomePage(this);
        if (savedInstanceState == null) {
            mainFragement = new WebViewFragment();
            getFragmentManager().beginTransaction()
                    .add(R.id.container, mainFragement)
                    .commit();
        }

        super.onCreate(savedInstanceState);


        // 使用api11 新加 api的方法
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//		//透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        //initView();
        RegisterReceiver();

        Intent i = new Intent(this, TTSService.class);
        bindService(i, ttsconn, BIND_AUTO_CREATE);


    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onStart() {
        db.deleteDuplicates();
        db.deleteHistoryByDay(1);
        super.onStart();

    }


    @Override

    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);
        String message = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE ? "屏幕设置为：横屏" : "屏幕设置为：竖屏";
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        Intent i = new Intent(this, TTSService.class);
        //unbindService(i,conn);
        unbindService(ttsconn);
        unbindService(coreconn);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            Intent intent = new Intent("WEBVIEW_MAIN");
            intent.putExtra("KEY", "DO_BACK_PRESSED");
            sendBroadcast(intent);
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            audiomanage.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                    AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            audiomanage.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                    AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
            return true;
        } else {
            super.onKeyDown(keyCode, event);
        }


        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mainFragement.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        if (mainFragement != null) {
            mainFragement.onSaveInstanceState(savedInstanceState);
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);
        //url = savedInstanceState.getString("url");
    }


    private void initData() {
        audiomanage = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        context = MainActivity.this;

        db = ConstantData.getDBManger();

    }


    private void RegisterReceiver() {
        registerReceiver(dcReceiver,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }


//

}
