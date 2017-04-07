/**
 * @File TableDao.java
 * @author zeng
 * @Date 2017-03-27
 * @Time 13:12
 */

package com.yunjuanyunshu.db.dao;

import com.yunjuanyunshu.Entity.TableEntity;
import com.yunjuanyunshu.db.dbc.DatabaseConnection;
import com.yunjuanyunshu.util.HumpLineUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TableDao implements IDao<TableEntity>{

    DatabaseConnection dbc=null;
    Connection conn=null;

    public TableDao(){
        try{
            dbc=new DatabaseConnection();
            conn = dbc.getConnection();
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    public TableDao(String DBUSER,String DBPASSWORD,String DBURL){
        try{
            dbc=new DatabaseConnection(DBUSER,DBPASSWORD,DBURL);
            conn = dbc.getConnection();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean doCreate(TableEntity tableEntity) throws Exception {
        return false;
    }

    @Override
    public boolean doUpdate(TableEntity tableEntity) throws Exception {
        return false;
    }

    @Override
    public boolean doDelete(int id) throws Exception {
        return false;
    }

    @Override
    public List<TableEntity> findAll(int currentPage, int lineSize, String keyword) throws Exception {
        return null;
    }

    /**
     * 获取数据库中表和视图结构
     * @param schema 数据库名称
     * @return 数据库中表和视图结构
     * @throws Exception
     */
    public List<TableEntity> findAllTableInfo(String schema) throws Exception {
        String sql="SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = ?";
        List<TableEntity> all=new ArrayList<TableEntity>();
        PreparedStatement pstmt=null;
        try{

            dbc=new DatabaseConnection();
            conn = dbc.getConnection();
            pstmt=this.conn.prepareStatement(sql);
            pstmt.setString(1,schema);
            System.out.println(sql);
            ResultSet rs=pstmt.executeQuery();
            TableEntity tmp=null;
            while(rs.next()){
                tmp=new TableEntity();
                tmp.setTableDBName(rs.getString("TABLE_NAME"));
                tmp.setTableDBDesc(rs.getString("TABLE_COMMENT"));
                tmp.setTableDBType(rs.getString("TABLE_TYPE"));
                tmp.setTableJavaName(HumpLineUtil.getJavaTableName(tmp.getTableDBName()));
                tmp.setTableJavaDesc(tmp.getTableDBDesc());

                ColumnDao columnDao =new ColumnDao();
                tmp.setColumnEntityList(columnDao.findAllColumnInfo(schema,tmp.getTableDBName()));
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


    /**
     * 获取数据库中表和视图结构
     * @param schema 数据库名称
     * @return 数据库中表和视图结构
     * @throws Exception
     */
    public HashMap<String,TableEntity> findHashMapAllTableInfo(String schema) throws Exception {
        List<TableEntity> tmplist = findAllTableInfo(schema);
        HashMap<String,TableEntity> tmpMap = new HashMap<>();
        for(TableEntity tmp : tmplist){
            tmpMap.put(tmp.getTableDBName(),tmp);
        }
        return  tmpMap;
    }
    /**
     * 获取数据库中表和视图名称
     * @param schema 数据库名称
     * @return 数据库中表和视图名称
     * @throws Exception
     */
    public List<String> findAllTableNameList(String schema) throws Exception {
        List<TableEntity> tmplist = findAllTableInfo(schema);
        List<String> tmpTableNameList = new ArrayList<>();
        for(TableEntity tmp : tmplist){
            tmpTableNameList.add(tmp.getTableDBName());
        }
        return  tmpTableNameList;
    }
    /**
     * 获取数据库中表和视图名称
     * @param schema 数据库名称
     * @return 数据库中表和视图名称
     * @throws Exception
     */
    public String[] findAllTableName(String schema) throws Exception {
        List<TableEntity> tmplist = findAllTableInfo(schema);
        String[] tmpTableNameList = new String[tmplist.size()];
        int i=0;
        for(TableEntity tmp : tmplist){
            tmpTableNameList[i] = tmp.getTableDBName();
            i++;
        }
        return  tmpTableNameList;
    }

    @Override
    public int getAllCount(String keyword) throws Exception {
        return 0;
    }

    @Override
    public TableEntity findEmpById(int no) throws Exception {
        return null;
    }

    @Override
    public boolean getAllName(String name) throws Exception {
        return false;
    }
}
