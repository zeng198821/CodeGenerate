/**
 * @File DBTest.java
 * @author zeng
 * @Date 2017-04-04
 * @Time 10:57
 */

package com.yunjuanyunshu.db;

import com.yunjuanyunshu.Entity.TableEntity;
import com.yunjuanyunshu.db.dao.TableDao;

import java.util.List;

public class DBTest {


    public static void main(String[] args){
        TableDao tableDao = new TableDao();
        try {
            List<TableEntity> tmplist = tableDao.findAllTableInfo("student");
            System.out.println(tmplist.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
