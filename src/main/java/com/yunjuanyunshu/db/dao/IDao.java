package com.yunjuanyunshu.db.dao;

import java.util.List;

/**
 * Created by zeng on 17-3-27.
 */
public interface IDao<T> {

    public boolean doCreate(T t)throws Exception;
    public boolean doUpdate(T t)throws Exception;
    public boolean doDelete(int id)throws Exception;
    public List<T> findAll(int currentPage, int lineSize, String keyword)throws Exception;
    public int getAllCount(String keyword)throws Exception;
    public T findEmpById(int no)throws Exception;
    public boolean getAllName(String name)throws Exception;

}
