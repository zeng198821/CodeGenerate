/**
 * @File ColumnDao.java
 * @author zeng
 * @Date 2017-03-27
 * @Time 13:12
 */

package com.yunjuanyunshu.db.dao;


import com.yunjuanyunshu.Entity.ColumnEntity;
import com.yunjuanyunshu.db.dbc.DatabaseConnection;
import com.yunjuanyunshu.db.entity.BaseEntity;
import com.yunjuanyunshu.util.DBJavaTypeConvertUtil;
import com.yunjuanyunshu.util.HumpLineUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ColumnDao implements IDao<BaseEntity> {

    DatabaseConnection dbc=null;
    Connection conn=null;

    public ColumnDao(){
        try{
            dbc=new DatabaseConnection();
            conn = dbc.getConnection();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public ColumnDao(String DBUSER,String DBPASSWORD,String DBURL){
        try{
            dbc=new DatabaseConnection(DBUSER,DBPASSWORD,DBURL);
            conn = dbc.getConnection();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean doCreate(BaseEntity baseEntity) throws Exception {
        return false;
    }

    @Override
    public boolean doUpdate(BaseEntity baseEntity) throws Exception {
        return false;
    }

    @Override
    public boolean doDelete(int id) throws Exception {
        return false;
    }

    @Override
    public List findAll(int currentPage, int lineSize, String keyword) throws Exception {
        return null;
    }

    @Override
    public int getAllCount(String keyword) throws Exception {
        return 0;
    }

    @Override
    public BaseEntity findEmpById(int no) throws Exception {
        return null;
    }

    @Override
    public boolean getAllName(String name) throws Exception {
        return false;
    }


    public List<ColumnEntity> findAllColumnInfo(String schema,String tableName) throws Exception {
        String sql="select * from information_schema.COLUMNS where table_schema = ? and table_name = ?  ;";
        List<ColumnEntity> all=new ArrayList<ColumnEntity>();
        PreparedStatement pstmt=null;
        try{
            pstmt=this.conn.prepareStatement(sql);
            pstmt.setString(1,schema);
            pstmt.setString(2,tableName);
            System.out.println(sql);
            ResultSet rs=pstmt.executeQuery();
            ColumnEntity tmp=null;
            while(rs.next()){
                tmp=new ColumnEntity();
                tmp.setColDBName(rs.getString("COLUMN_NAME"));
                tmp.setColDBType(rs.getString("DATA_TYPE"));
                tmp.setColDBNullAble(rs.getString("IS_NULLABLE").equals("YES"));
                tmp.setColDBDesc(rs.getString("COLUMN_COMMENT") == null ? "" : rs.getString("COLUMN_COMMENT"));
                tmp.setColDBLength(rs.getString("CHARACTER_MAXIMUM_LENGTH") == null ? 0 : rs.getLong("CHARACTER_MAXIMUM_LENGTH"));
                tmp.setColJavaName(HumpLineUtil.getJavaColumnName(tmp.getColDBName()));
                tmp.setColJavaDesc(tmp.getColDBDesc());
                tmp.setColJavaType(DBJavaTypeConvertUtil.getJavaTypeFromDBType(tmp.getColDBType()));
                all.add(tmp);
            }
        }catch(Exception e){
            throw e;
        }finally{
            try{
                pstmt.close();
                conn.close();
            }catch(Exception e){
                throw e;
            }
        }
        return all;
    }


}
