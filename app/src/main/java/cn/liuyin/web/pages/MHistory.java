package cn.liuyin.web.pages;

import android.content.Context;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import cn.liuyin.web.constant.ConstantData;
import cn.liuyin.web.database.DBManager;
import cn.liuyin.web.database.Web;

/**
 * Created by asus on 2017/8/9.
 */

public class MHistory {
    Context mContext;
    //public static String homeUrl=ConstantData.getContext().getExternalFilesDir("www")+"/index.html";
    private Document document;

    public MHistory(Context context) {
        mContext = context;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            this.document = builder.newDocument();

            makeMain(null);
            PageTool.save(document, mContext.getExternalFilesDir("www") + "/history.html");
            System.out.print("kkkkkk");
        } catch (ParserConfigurationException e) {
            System.out.println(e.getMessage());
        }
    }

    private void makeMain(Element e) {
        Element html = this.document.createElement("html");
        this.document.appendChild(html);
        //HEAD 部分
        Element head = this.document.createElement("head");
        html.appendChild(head);
        Element title = this.document.createElement("title");
        title.setTextContent("历史记录");
        head.appendChild(title);
        Element meta1 = this.document.createElement("meta");
        meta1.setAttribute("name", "viewport");
        meta1.setAttribute("content", "width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=yes");
        head.appendChild(meta1);
        Element link1 = document.createElement("link");
        link1.setAttribute("rel", "stylesheet");
        link1.setAttribute("href", "file:///android_asset/css/base.css");
        head.appendChild(link1);
        Element script1 = document.createElement("script");
        script1.setAttribute("src", "file:///android_asset/js/base.js");
        head.appendChild(script1);

        Element body = document.createElement("body");
        html.appendChild(body);
        Element back = document.createElement("div");
        back.setAttribute("id", "back");
        body.appendChild(back);
        Element p1 = document.createElement("p");
        p1.setAttribute("align", "center");
        p1.setAttribute("style", "background-color:rgba(100,100,100,0.5);color:rgb(0,0,0)");
        back.appendChild(p1);
        Element span = document.createElement("span");
        span.setAttribute("id", "aa");
        p1.appendChild(span);
        Element script2 = document.createElement("script");
        script2.setAttribute("src", "file:///android_asset/js/time.js");
        back.appendChild(script2);

        Element input1 = document.createElement("input");
        input1.setAttribute("type", "search");
        input1.setAttribute("onkeyup", "search_onkeyup(this)");
        input1.setAttribute("id", "search");
        input1.setAttribute("style", "width:80%; height: 50px; padding:  2px 2px 2px 2px; float: left;");
        back.appendChild(input1);
        Element button1 = document.createElement("button");
        button1.setAttribute("type", "button");
        button1.setAttribute("onclick", "search()");
        button1.setAttribute("style", "width:20%; height: 50px; padding:  2px 2px 2px 2px; float: left;");
        button1.setTextContent("搜索");
        back.appendChild(button1);
        back.appendChild(makeList());


    }

    private Element makeList() {
        Element main = document.createElement("div");
        Element br = document.createElement("hr");
        main.setAttribute("id", "main");
        main.appendChild(br);

        //final MainActivity mainActivity =  ConstantData.getMainActivity();
        DBManager db = ConstantData.getDBManger();
        try {
            List<Web> webs = db.queryHistory();

            for (int i = webs.size() - 1; i >= 0; i--) {
                Element div = document.createElement("div");
                div.setAttribute("style", "background-color:rgba(100,100,100,0.75)");
                Web web = webs.get(i);
                Element hr = document.createElement("hr");
                Element a = document.createElement("a");
                a.setAttribute("href", web.getUrl());
                a.setAttribute("style", "float:left;width:100%;background-color:rgb(200,200,200);");
                String title = web.getTitle();
                a.setTextContent(title);

                Element p1 = document.createElement("p");
                p1.setTextContent(web.getTime());
                p1.setAttribute("style", "background-color:rgb(200,200,200);float:right;");
                p1.setAttribute("align", "right");

                div.appendChild(a);
                div.appendChild(p1);
                //div.appendChild(hr);
                main.appendChild(div);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return main;

    }

}
