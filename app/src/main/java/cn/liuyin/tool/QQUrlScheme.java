package cn.liuyin.tool;

public class QQUrlScheme {
    public static String ViewDetial(String QQHao) {
        return "mqqapi://card/show_pslcard?src_type=internal&version=1" +
                "&uin=" + QQHao +
                "&card_type=person&source=sharecard&web_src=oicqzone.com";
    }

    public static String ChatToFrient(String QQHao) {
        return "mqqwpa://im/chat?chat_type=wpa" +
                "&uin=" + QQHao +
                "&version=1&src_type=web&web_src=oicqzone.com";
    }

    public static String ShareToFriends(String url, String img, String title, String des) {
        return "mqqapi://share/to_fri?file_type=news" +
                "&src_type=web&version=1&share_id=1105471055" +
                "&url=" + MBase64.MEncode(url) +
                "&previewimageUrl=" + MBase64.MEncode(img) +
                "&image_url=" + MBase64.MEncode(img) +
                "&title=" + MBase64.MEncode(title) +
                "&description=" + MBase64.MEncode(des) +
                "&callback_type=scheme&thirdAppDsplayName=UVE&app_name=UVE" +
                "&cflag=0&shareType=0";
    }

    public static String JiaQun(String QunHao) {
        return "mqqapi://card/show_pslcard?src_type=internal&version=1&uin=" + QunHao + "&card_type=group&source=qrcode";
    }
}
