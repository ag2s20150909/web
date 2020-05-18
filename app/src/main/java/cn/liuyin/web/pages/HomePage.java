package cn.liuyin.web.pages;


import android.content.Context;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import cn.liuyin.web.constant.ConstantData;


public class HomePage {
    private Document document;

    public HomePage(Context context) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            this.document = builder.newDocument();
            makeMain();
            PageTool.save(document, context.getExternalFilesDir("www") + "/index.html");
            System.out.print("kkkkkk");
        } catch (ParserConfigurationException e) {
            System.out.println(e.getMessage());
        }
    }

    private void makeMain() {
        Element html = this.document.createElement("html");
        this.document.appendChild(html);
        //HEAD 部分
        Element head = this.document.createElement("head");
        html.appendChild(head);
        Element title = this.document.createElement("title");
        title.setTextContent("网址导航");
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
        main.setAttribute("id", "main");
        BufferedReader br;
        try {
            File file = new File(ConstantData.DIYPath + "/data.txt");
            br = new BufferedReader(new FileReader(file));
            String line;
            Element table = null, tr = null;
            int num = 0;
            while ((line = br.readLine()) != null) {
                //去除每行可能存在的空格。
                line = line.trim();
                //判断改行是否符合一级标准（非空，#开头）
                if (!line.isEmpty() && line.startsWith("#")) {
                    //标题开始标记
                    if (line.startsWith("#HT:")) {
                        table = document.createElement("table");
                        Element caption = document.createElement("caption");
                        caption.setAttribute("align", "center");
                        caption.setTextContent(line.substring(line.lastIndexOf("#HT:") + 4));
                        table.appendChild(caption);
                        //将num归零
                        num = 0;

                    } else if (line.startsWith("#name") && line.contains("#url")) {

                        num++;
                        String name, url;
                        name = line.substring(line.indexOf("#name:") + 6, line.lastIndexOf("#url:"));
                        url = line.substring(line.indexOf("#url:") + 5);
                        if (num % 4 == 1) {
                            tr = document.createElement("tr");
                        }
                        if (num % 4 == 0) {
                            assert table != null;
                            table.appendChild(tr);
                        }

                        //System.out.println("hhh" + num);
                        Element td = document.createElement("td");
                        Element p = document.createElement("p");
                        Element a = document.createElement("a");
                        a.setAttribute("href", url);
                        a.setAttribute("target", "_blank");
                        a.setTextContent(name);
                        p.appendChild(a);
                        assert tr != null;
                        tr.appendChild(td);
                        td.appendChild(p);


                    } else if (line.startsWith("#end")) {
                        //对最后一行未满4个的处理
                        if (num % 4 != 0) {
                            assert table != null;
                            table.appendChild(tr);
                        }
                        main.appendChild(table);
                    }
                }
            }
            br.close();


        } catch (Exception e) {
            e.printStackTrace();

        }
        return main;

    }


}
