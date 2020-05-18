package cn.liuyin.web.listener;


import android.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

import cn.liuyin.web.R;
import cn.liuyin.web.constant.ConstantData;


public class MOnLongClickListener implements View.OnLongClickListener {

    @Override
    public boolean onLongClick(View p1) {
        // TODO: Implement this method
        switch (p1.getId()) {
            case R.id.search_icon:
                changeSearch(ConstantData.getMainActivity().pref.getString("SearchEngine", "http://m.baidu.com/s?word=%s"));
                break;
        }
        return false;
    }

    private void changeSearch(String se) {
        AlertDialog alert;
        AlertDialog.Builder builder = new AlertDialog.Builder(ConstantData.getContext());
        final String[] name = new String[]{"百度", "谷歌", "必应", "神马", "好搜", "搜狗", "中国搜索"};
        final String[] ses = new String[]{
                "http://m.baidu.com/s?word=%s",//百度0
                "https://www.google.com/search?q=%s",//谷歌1
                "http://cn.bing.com/search?q=%s",//必应2
                "https://m.sm.cn/s?q=%s",//神马3
                "https://m.so.com/s?q=%s",//好搜4
                "http://m.sogou.com/web/searchList.jsp?keyword=%s",//搜狗5
                "http://m.chinaso.com/page/search.htm?keys=%s"//中国搜索6
        };
        int a = 0;
        for (int i = 0; i < ses.length; i++) {
            if (se.contains(ses[i])) {
                a = i;
            }
        }
        alert = builder.setIcon(R.drawable.ic_baidu)
                .setTitle("选择搜索引擎")
                .setSingleChoiceItems(name, a, (dialog, which) -> {
                    Toast.makeText(ConstantData.getContext(), "你选择了" + name[which], Toast.LENGTH_SHORT).show();
                    ConstantData.getMainActivity().editor.putString("SearchEngine", ses[which]).apply();

                }).create();
        alert.show();
    }

}
