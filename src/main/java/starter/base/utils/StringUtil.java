package starter.base.utils;

import java.io.*;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author zhyf
 */
public class StringUtil {

    /**
     * 判断是否不为空，不为空则返回true 为空的条件：null、""、"null"
     */
    public static boolean isNotBlank(Object obj) {
        return !isBlank(obj);
    }

    /**
     * 判断是否为空，为空则返回true 为空的条件：null、""、"null"
     */
    public static boolean isBlank(Object obj) {
        if (obj == null) {
            return true;
        }
        String str = obj.toString().trim();
        return "".equals(str) || "null".equalsIgnoreCase(str);
    }

    /**
     * 是否包含空字符串
     *
     * @return 是否包含空字符串
     */
    public static boolean hasBlank(Object obj) {
        return obj == null || obj.toString().contains(" ");
    }

    /**
     * 字符串是否为非空白 空白的定义如下： null、""
     *
     * @param str 被检测的字符串
     *
     * @return 是否为非空
     */
    public static boolean isNotEmpty(CharSequence str) {
        return !isEmpty(str);
    }

    /**
     * 字符串是否为空，空的定义如下 null、""
     *
     * @param str 被检测的字符串
     *
     * @return 是否为空
     */
    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    public static String getExceptionAllInformation(Exception ex) {
        StringBuilder sOut = new StringBuilder();
        StackTraceElement[] trace = ex.getStackTrace();
        for (StackTraceElement s : trace) {
            sOut.append("\tat ").append(s).append("\r\n");
        }
        return sOut.toString();
    }

    public static String getExceptionAllInformation(Throwable ex) {
        StringBuilder sOut = new StringBuilder();
        Throwable d = ex.getCause();
        sOut.append(d.getMessage());
        sOut.append("\r\n");
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw, true));
        sOut.append(sw.toString());
        return sOut.toString();
    }

    /**
     * 获取换行符
     */
    public static String getNewLine() {
        return System.getProperty("line.separator");
    }

    /**
     * 获取uuid字符串
     */
    public static String uuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * 去除前后空格，若obj为null返回""空字串
     */
    public static String trim(Object obj) {
        if (obj == null) {
            return "";
        }
        return obj.toString().trim();
    }

    /**
     * 切分字符串
     *
     * @param str       被切分的字符串
     * @param separator 分隔符字符
     *
     * @return 切分后的集合
     */
    public static String[] splitToArray(String str, char separator) {
        List<String> result = split(str, separator);
        return result.toArray(new String[0]);
    }

    /**
     * 切分字符串<br> a#b#c -> [a,b,c] <br> a##b#c -> [a,"",b,c]
     *
     * @param str       被切分的字符串
     * @param separator 分隔符字符
     *
     * @return 切分后的集合
     */
    public static List<String> split(String str, char separator) {
        return split(str, separator, 0);
    }

    /**
     * 切分字符串
     *
     * @param str       被切分的字符串
     * @param separator 分隔符字符
     * @param limit     限制分片数
     *
     * @return 切分后的集合
     */
    public static List<String> split(String str, char separator, int limit) {
        if (str == null) {
            return null;
        }
        List<String> list = new ArrayList<>(limit == 0 ? 16 : limit);
        if (limit == 1) {
            list.add(str);
            return list;
        }

        // 未结束切分的标志
        boolean isNotEnd = true;
        int strLen = str.length();
        StringBuilder sb = new StringBuilder(strLen);
        for (int i = 0; i < strLen; i++) {
            char c = str.charAt(i);
            if (isNotEnd && c == separator) {
                list.add(sb.toString());
                // 清空StringBuilder
                sb.delete(0, sb.length());

                // 当达到切分上限-1的量时，将所剩字符全部作为最后一个串
                if (limit != 0 && list.size() == limit - 1) {
                    isNotEnd = false;
                }
            } else {
                sb.append(c);
            }
        }

        // 加入尾串
        list.add(sb.toString());
        return list;
    }

    /**
     * 切分字符串
     *
     * @param str       被切分的字符串
     * @param separator 分隔符字符
     * @param limit     限制分片数
     *
     * @return 切分后的集合
     */
    public static String[] splitToArray(String str, char separator, int limit) {
        List<String> result = split(str, separator, limit);
        return result.toArray(new String[0]);
    }

    /**
     * 切分字符串<br>
     *
     * @param str       被切分的字符串
     * @param delimiter 分隔符
     *
     * @return 字符串
     */
    public static String[] split(String str, String delimiter) {
        if (str == null) {
            return null;
        }
        if (str.trim().length() == 0) {
            return new String[]{str};
        }

        // del length
        int dellen = delimiter.length();
        // one more for the last
        int maxparts = (str.length() / dellen) + 2;
        int[] positions = new int[maxparts];

        int i, j = 0;
        int count = 0;
        positions[0] = -dellen;
        while ((i = str.indexOf(delimiter, j)) != -1) {
            count++;
            positions[count] = i;
            j = i + dellen;
        }
        count++;
        positions[count] = str.length();

        String[] result = new String[count];

        for (i = 0; i < count; i++) {
            result[i] = str.substring(positions[i] + dellen, positions[i + 1]);
        }
        return result;
    }

    /**
     * 拆分字符串 根据给定长度，将给定字符串截取为多个部分
     *
     * @param str 字符串
     * @param len 每一个小节的长度
     *
     * @return 截取后的字符串数组
     */
    public static String[] split(String str, int len) {
        int partCount = str.length() / len;
        int lastPartCount = str.length() % len;
        int fixPart = 0;
        if (lastPartCount != 0) {
            fixPart = 1;
        }
        final String[] strs = new String[partCount + fixPart];
        for (int i = 0; i < partCount + fixPart; i++) {
            if (i == partCount + fixPart - 1 && lastPartCount != 0) {
                strs[i] = str.substring(i * len, i * len + lastPartCount);
            } else {
                strs[i] = str.substring(i * len, i * len + len);
            }
        }
        return strs;
    }

    /**
     * 统计指定内容中包含指定字符串的数量<br> 参数为 {@code null} 或者 "" 返回 {@code 0}.
     * <p>
     * <pre>
     * StrUtil.count(null, *)       = 0
     * StrUtil.count("", *)         = 0
     * StrUtil.count("abba", null)  = 0
     * StrUtil.count("abba", "")    = 0
     * StrUtil.count("abba", "a")   = 2
     * StrUtil.count("abba", "ab")  = 1
     * StrUtil.count("abba", "xxx") = 0
     * </pre>
     *
     * @param content      被查找的字符串
     * @param strForSearch 需要查找的字符串
     *
     * @return 查找到的个数
     */
    public static int count(final String content, final String strForSearch) {
        if (hasEmpty(content, strForSearch) || strForSearch.length() > content.length()) {
            return 0;
        }

        int count = 0;
        int idx = 0;
        while ((idx = content.indexOf(strForSearch, idx)) > -1) {
            count++;
            idx += strForSearch.length();
        }
        return count;
    }

    /**
     * 是否包含空字符串
     *
     * @param strs 字符串列表
     *
     * @return 是否包含空字符串
     */
    public static boolean hasEmpty(CharSequence... strs) {
        if (ArrayUtil.isEmpty(strs)) {
            return true;
        }

        for (CharSequence str : strs) {
            if (isEmpty(str)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 统计指定内容中包含指定字符的数量
     *
     * @param content       内容
     * @param charForSearch 被统计的字符
     *
     * @return 包含数量
     */
    public static int count(final String content, char charForSearch) {
        int count = 0;
        if (isEmpty(content)) {
            return 0;
        }
        int contentLength = content.length();
        for (int i = 0; i < contentLength; i++) {
            if (charForSearch == content.charAt(i)) {
                count++;
            }
        }
        return count;
    }

    /**
     * 补充字符串以满足最小长度 StrUtil.padPre("1", 3, '0');//"001"
     *
     * @param str       字符串
     * @param minLength 最小长度
     * @param padChar   补充的字符
     *
     * @return 补充后的字符串
     */
    public static String padPre(String str, int minLength, char padChar) {
        if (str.length() >= minLength) {
            return str;
        }
        StringBuilder sb = new StringBuilder(minLength);
        for (int i = str.length(); i < minLength; i++) {
            sb.append(padChar);
        }
        sb.append(str);
        return sb.toString();
    }

    /**
     * 补充字符串以满足最小长度 StrUtil.padEnd("1", 3, '0');//"100"
     *
     * @param str       字符串
     * @param minLength 最小长度
     * @param padChar   补充的字符
     *
     * @return 补充后的字符串
     */
    public static String padEnd(String str, int minLength, char padChar) {
        if (str.length() >= minLength) {
            return str;
        }
        StringBuilder sb = new StringBuilder(minLength);
        sb.append(str);
        for (int i = str.length(); i < minLength; i++) {
            sb.append(padChar);
        }
        return sb.toString();
    }

    /**
     * 使用gzip对字符串压缩
     *
     * @param str 待压缩的字符串
     *
     * @return 返回压缩后的字符串
     */
    public static String compress(String str) throws IOException {
        if (null == str || str.length() <= 0) {
            return str;
        }
        // 创建一个新的 byte 数组输出流
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // 使用默认缓冲区大小创建新的输出流
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        // 将 b.length 个字节写入此输出流
        gzip.write(str.getBytes());
        gzip.close();
        // 使用指定的 charsetName，通过解码字节将缓冲区内容转换为字符串
        return out.toString("ISO-8859-1");
    }

    /**
     * 使用gzip对字符串解压
     *
     * @param str 对字符串解压
     *
     * @return 返回解压缩后的字符串
     */
    public static String unCompress(String str) throws IOException {
        if (null == str || str.length() <= 0) {
            return str;
        }
        // 创建一个新的 byte 数组输出流
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // 创建一个 ByteArrayInputStream，使用 buf 作为其缓冲区数组
        ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes(StandardCharsets.ISO_8859_1));
        // 使用默认缓冲区大小创建新的输入流
        GZIPInputStream gzip = new GZIPInputStream(in);
        byte[] buffer = new byte[256];
        int n;
        while ((n = gzip.read(buffer)) >= 0) {
            // 将未压缩数据读入字节数组
            // 将指定 byte 数组中从偏移量 off 开始的 len 个字节写入此 byte数组输出流
            out.write(buffer, 0, n);
        }
        // 使用指定的 charsetName，通过解码字节将缓冲区内容转换为字符串
        return out.toString("GBK");
    }

    /**
     * 获取字符串中两个指定字符串中间的内容
     * <p>
     * abc3443abcfgjhgabcgfjabc 获取 abc abc 中间的内容 结果：[3443,gfj]
     */
    public static List<String> getSubStrBetween(String soap, String frontStr, String backStr) {
        if (StringUtil.isEmpty(soap)) {
            return CollectionUtil.newArrayList();
        }

        List<String> list = new ArrayList<>();
        Pattern pattern = Pattern.compile(frontStr + "(.*?)" + backStr);
        Matcher m = pattern.matcher(soap);
        while (m.find()) {
            list.add(m.group(1));
        }
        return list;
    }

    public static String getGetterName(Method mtd) {
        String name = mtd.getName();
        if (name.startsWith("get")) {
            return Character.toLowerCase(name.charAt(3)) + name.substring(4);
        }
        if (name.startsWith("is")) {
            return Character.toLowerCase(name.charAt(2)) + name.substring(3);
        }
        return null;
    }

    public static String toGetterName(String name) {
        return "get" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }

    /**
     * 过滤emoji
     */
    public static String filterEmoji(String source) {
        if (source != null) {
            Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]", Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
            Matcher emojiMatcher = emoji.matcher(source);
            if (emojiMatcher.find()) {
                source = emojiMatcher.replaceAll("*");
                return source;
            }
            return source;
        }
        return source;
    }

}
