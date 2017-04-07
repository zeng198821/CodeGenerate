/**
 * @File HumpLineUtil.java
 * @author zeng
 * @Date 2017-04-04
 * @Time 11:13
 */

package com.yunjuanyunshu.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HumpLineUtil {

    /**
     * 下划线正则表达式
     */
    private static Pattern linePattern = Pattern.compile("_(\\w)");

    /**
     * 驼峰表达式
     */
    private static Pattern humpPattern = Pattern.compile("[A-Z]");
    /**
     * 下划线转换成驼峰
     * @param str 下划线字符串
     * @return 驼峰字符串
     */
    public static String lineToHump(String str){
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while(matcher.find()){
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 转换表名至类名
     * @param str 表名
     * @return 类名
     */
    public static String getJavaTableName(String str){
        String tmp = lineToHump(str);
        tmp = tmp.substring(0,1).toUpperCase() + tmp.substring(1);
        return  tmp;
    }


    /**
     * 转换表名至类名
     * @param str 表名
     * @return 类名
     */
    public static String getJavaColumnName(String str){
        String tmp = lineToHump(str);
        tmp = tmp.substring(0,1).toLowerCase() + tmp.substring(1);
        return  tmp;
    }

    /**
     * 驼峰转下划线
     * @param str 驼峰字符串
     * @return 转换下划线字符串
     */
    public static String humpToLine(String str){
        Matcher matcher = humpPattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while(matcher.find()){
            matcher.appendReplacement(sb, "_"+matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
