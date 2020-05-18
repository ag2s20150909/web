package cn.liuyin.webview;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TTSService extends Service {
    private TextToSpeech mtts;

    @Override
    public void onCreate() {
        mtts = new TextToSpeech(this, new MyOnInitialListener());
        super.onCreate();
    }

    // 服务断开连接时，调用该方法
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    public void StopSpeak() {
        mtts.stop();
    }

    public void Speak(String string) {
        mtts.stop();
        String[] data = splitSentence(string);
        for (String str : data) {
            mtts.speak(str, TextToSpeech.QUEUE_ADD, null);
            System.out.println(str);
        }

    }

    private String[] splitSentence(String cmt) {
        cmt = cmt.replaceAll("(?m)^\\s*$" + System.lineSeparator(), "");
        cmt = cmt.replaceAll("\\p{Blank}{1,}", "");
        String regEx = "([.!?。？！])";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(cmt);
        String[] words = p.split(cmt);
        if (words.length > 0) {
            int count = 0;
            while (count < words.length) {
                if (m.find()) {
                    words[count] += m.group();
                }
                count++;
            }
        }
        //p
        return words;
    }

    /**
     * 返回一个Binder对象
     */
    @Override
    public IBinder onBind(Intent intent) {
        return new TTSServiceBinder();
    }

    class MyOnInitialListener implements TextToSpeech.OnInitListener {

        @Override
        public void onInit(int status) {
            if (status == TextToSpeech.SUCCESS) {
                int result = mtts.setLanguage(Locale.CHINA);
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Toast.makeText(TTSService.this, "数据丢失或者不支持。", Toast.LENGTH_SHORT).show();
                } else {
                    //tts.speak("初始化成功。", TextToSpeech.QUEUE_ADD, null);
                    Toast.makeText(TTSService.this, "TTS初始化成功。", Toast.LENGTH_SHORT).show();
                }
            }
            //tts.setLsanguage(Locale.CHINESE);


        }

    }

    public class TTSServiceBinder extends Binder {

        public TTSService getService() {
            return TTSService.this;
        }
    }

}
