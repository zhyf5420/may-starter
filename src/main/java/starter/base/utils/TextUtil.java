package starter.base.utils;

import cn.hutool.core.io.FileUtil;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文本处理工具
 *
 * Created on 2019/5/17.
 *
 * @author zhyf
 */
public class TextUtil {

    public static List<String> uselessList;

    static {
        File useless = new File(TextUtil.class.getResource("/useless.txt").getFile());
        uselessList = FileUtil.readUtf8Lines(useless);
    }

    public static String filter2(String str) {
        return str.replaceAll("\n", "").trim();
    }

    /**
     * 用于过滤文本
     */
    public static String filter(String str) {
        //去掉前后空格
        str = str.trim().replaceAll(" ", "");

        //过滤全是数字
        if (RegexUtil.checkDigit(str)) {
            return "";
        }

        //过滤url
        String urlPatternStr = "(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]";
        str = str.replaceAll(urlPatternStr, "");

        //对无用词进行过滤 利用useless.txt文件
        for (String useless : uselessList) {
            str = str.replace(useless, "");
        }

        //过滤文本开始和结束的标点
        String pattern = "^([*,，。.?？!！、])+(.*)";
        Matcher m = Pattern.compile(pattern).matcher(str);
        if (m.matches()) {
            str = m.group(2);
        }

        pattern = "(.*?)([*,，。.?？!！、])+$";
        m = Pattern.compile(pattern).matcher(str);
        if (m.matches()) {
            str = m.group(1);
        }

        //去掉html标签
        str = removeHtmlTag(str);

        //去掉前后空格
        str = str.trim();

        //过滤全是数字
        if (RegexUtil.checkDigit(str)) {
            return "";
        }

        return str;
    }

    /**
     * 去掉html标签
     */
    public static String removeHtmlTag(String inputString) {
        if (StringUtil.isEmpty(inputString)) {
            return inputString;
        }

        String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?/[\\s]*?script[\\s]*?>";
        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);

        String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?/[\\s]*?style[\\s]*?>";
        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);

        String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);

        // 过滤script标签
        inputString = p_script.matcher(inputString).replaceAll("");

        // 过滤style标签
        inputString = p_style.matcher(inputString).replaceAll("");

        // 过滤html标签
        inputString = p_html.matcher(inputString).replaceAll("");

        return inputString;// 返回文本字符串
    }

}
