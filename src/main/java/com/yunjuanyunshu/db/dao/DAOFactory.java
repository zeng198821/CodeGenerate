/**
 * @File DAOFactory.java
 * @author zeng
 * @Date 2017-03-27
 * @Time 13:17
 */

package com.yunjuanyunshu.db.dao;

public class DAOFactory {

    public static TableDao getTableDaoInstance(){
        return new TableDao();
    }

    public static ColumnDao ColumnDao(){
        return new ColumnDao();
    }
}
