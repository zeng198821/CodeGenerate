/**
 * @File DBJavaTypeConvertUtil.java
 * @author zeng
 * @Date 2017-04-04
 * @Time 11:22
 */

package com.yunjuanyunshu.util;

import java.util.HashMap;

public class DBJavaTypeConvertUtil {

    private static HashMap<String,String> dbTypeMap=new HashMap<>();

    static {

        dbTypeMap.put("int","int");
        dbTypeMap.put("bigint","long");
        dbTypeMap.put("float","float");
        dbTypeMap.put("double","double");
        dbTypeMap.put("decimal","double");
        dbTypeMap.put("datetime","Date");
        dbTypeMap.put("date","Date");
        dbTypeMap.put("timestamp","Date");
        dbTypeMap.put("text","String");
        dbTypeMap.put("char","String");
        dbTypeMap.put("varchar","String");
        dbTypeMap.put("bool","Boolean");

    }


    /**
     *
     * @param dbType
     * @return
     */
    public static String getJavaTypeFromDBType(String dbType){
        String tmpJavaType = "";
        if(dbType != null || !dbType.isEmpty()){
            dbTypeMap.containsKey(dbType);
            dbType = dbType.toLowerCase();
            tmpJavaType = dbTypeMap.get(dbType);
        }

        return  tmpJavaType;
    }


}
