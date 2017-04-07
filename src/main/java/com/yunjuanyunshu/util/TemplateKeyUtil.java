/**
 * @File TemplateKeyUtil.java
 * @author zeng
 * @Date 2017-04-04
 * @Time 01:30
 */

package com.yunjuanyunshu.util;

import com.yunjuanyunshu.Entity.TableEntity;
import org.apache.commons.lang.time.DateFormatUtils;

import java.util.Date;
import java.util.HashMap;

public class TemplateKeyUtil {

    public static HashMap<String,Object> getTemplateKeyMap(TableEntity tableEntity){
        HashMap<String,Object> tmpMap = new HashMap<>();
        Date now = new Date();
        //tmpMap.put("class0", new ClassEntry("",""));
        tmpMap.put("YEAR", DateFormatUtils.format(now, "yyyy"));
        tmpMap.put("TIME", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        tmpMap.put("USER", System.getProperty("user.name"));
        tmpMap.put("ClassName", tableEntity.getTableJavaName());
        tmpMap.put("PacakgeStr", tableEntity.getPackageStr());
        return tmpMap;
    }


}
