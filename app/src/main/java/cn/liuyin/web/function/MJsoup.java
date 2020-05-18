package cn.liuyin.web.function;


import android.content.Context;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Set;

public class MJsoup {

    public static String getHmlSouce(Context context, String url) {

        StringBuilder html = new StringBuilder();

        if (url.startsWith("file:///android_asset")) {
            String fileName = url.substring(url.indexOf("android_asset/") + 14);
            try {
                //得到资源中的asset数据流
                InputStream in = context.getResources().getAssets().open(fileName);
                int length = in.available();
                byte[] buffer = new byte[length];
                in.read(buffer);
                in.close();
                html = new StringBuilder(new String(buffer, StandardCharsets.UTF_8));
                Document doc = Jsoup.parse(html.toString());

                html = new StringBuilder(doc.html());
                return html.toString();
            } catch (Exception e) {
                return "MJsoup001\n" + e.toString();
            }
        } else if (url.startsWith("file:///")) {
            try {
                URL urls = new URL(url);
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        urls.openStream(), StandardCharsets.UTF_8));
                String data;
                while ((data = reader.readLine()) != null) {
                    html.append(data).append("\n");
                }
                reader.close();
                Document doc = Jsoup.parse(html.toString());
                html = new StringBuilder(doc.html());
                return html.toString();

            } catch (MalformedURLException e) {
                return "MJsoup002\n" + e.toString();
            } catch (IOException e) {
                return "MJsoup003\n" + e.toString();
            }
        } else if (url.startsWith("http://") || url.startsWith("https://")) {
            try {
                Document doc = Jsoup.connect(url).get();
                html = new StringBuilder(doc.outerHtml());
            } catch (Exception e) {
                //html=e.toString();
                return "MJsoup004\n" + e.toString();
            }
        } else {
            Document doc = Jsoup.parse(url);
            html = new StringBuilder(doc.html());
        }
        return html.toString();
    }

    public static String getAllLink(String url, String html) {
        StringBuilder txt = new StringBuilder();

        try {
            Document doc = Jsoup.parse(html);
            doc.setBaseUri(url);
            Elements links = doc.select("a[href]");
            Elements media = doc.select("[src]");
            Elements imports = doc.select("link[href]");


            txt.append(print("\n媒体文件链接: (%d)", media.size()));
            for (Element src : media) {
                if (src.tagName().equals("img")) {
                    txt.append(print(" * %s: <%s> %sx%s (%s)",
                            "【图片】" + src.tagName(), src.attr("abs:src"), src.attr("width"), src.attr("height"),
                            trim(src.attr("alt"), 20)));
                    //DownloadPart.down_file(src.attr("abs:src"),"","","");
                } else if (src.tagName().equals("video")) {
                    txt.append(print(" * %s: <%s>", "【视频】" + src.tagName(), src.attr("abs:src")));
                } else if (src.tagName().equals("audio")) {
                    txt.append(print(" * %s: <%s>", "【音频】" + src.tagName(), src.attr("abs:src")));
                } else if (src.tagName().equals("source")) {
                    txt.append(print(" * %s: <%s>", "【来源】" + src.tagName(), src.attr("abs:src")));
                } else if (src.tagName().equals("script")) {
                    txt.append(print(" * %s: <%s>", "【脚本】" + src.tagName(), src.attr("abs:src")));
                } else if (src.tagName().equals("iframe")) {
                    txt.append(print(" * %s: <%s>", "【框架】" + src.tagName(), src.attr("abs:src")));
                } else if (src.tagName().equals("div")) {
                    txt.append(print(" * %s: <%s>", "【布块】" + src.tagName(), src.attr("abs:src")));
                } else if (src.tagName().equals("track")) {
                    txt.append(print(" * %s: <%s>", "【轨道】" + src.tagName(), src.attr("abs:src")));
                } else if (src.tagName().equals("embed")) {
                    txt.append(print(" * %s: <%s>", "【媒体】" + src.tagName(), src.attr("abs:src")));
                } else {
                    txt.append(print(" * %s: <%s>", src.tagName(), src.attr("abs:src")));
                }
            }


            txt.append(print("\n导入文件链接: (%d)", imports.size()));
            for (Element link : imports) {
                txt.append(print(" * %s <%s> (%s)", link.tagName(), link.attr("abs:href"), link.attr("rel")));

            }

            txt.append(print("\n链接: (%d)", links.size()));
            for (Element link : links) {
                String tempurl = link.text();
                if (tempurl.contains("magnet:")) {
                    txt.append(print(" * 【磁力链接】a: <%s>  (%s)", link.attr("abs:href"), trim(link.text(), 35)));
                } else {
                    txt.append(print(" * a: <%s>  (%s)", link.attr("abs:href"), trim(link.text(), 35)));
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return txt.toString();
    }

    public static String findHtmlbyTag(String url, String html, String tag) {
        StringBuilder msg = new StringBuilder();

        try {
            Document doc = Jsoup.parse(html);
            doc.setBaseUri(url);
            Elements targs = doc.select(tag);
            msg.append(print("发现个数: (%d)", targs.size()));
            for (Element targ : targs) {
                String tagname = targ.tagName();
                msg.append(print("【标签名】：%s", tagname));
                String id = targ.id();
                msg.append(print("【id】：%s", id));
                Set<String> classNames = targ.classNames();
                StringBuilder classss = new StringBuilder();
                for (String classname : classNames) {
                    classss.append(classname).append(",");
                }
                msg.append("【类名】：").append(classss).append("\n");
                Attributes attributes = targ.attributes();
                msg.append(print("【属性】 %s 个", attributes.size()));
                for (Attribute attr : attributes) {
                    attr.getKey();
                    msg.append(print("  %s => %s", attr.getKey(), attr.getValue()));
                }
                msg.append(print("【自有文本】:%s", targ.ownText()));
                msg.append(print("【内部文本】:%s", targ.text()));
                msg.append(print("【内部数据】:%s", targ.data()));
                msg.append("\n\n");

            }


        } catch (Exception e) {
            e.printStackTrace();
            msg = new StringBuilder(e.toString());
        }

        return msg.toString();
    }

    private static String print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
        return String.format(msg, args) + "\n";
    }

    private static String trim(String s, int width) {
        if (s.length() > width)
            return s.substring(0, width - 1) + ".";
        else
            return s;
    }
}
