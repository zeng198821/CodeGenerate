/**
 * @File TableEntity.java
 * @author zeng
 * @Date 2017-04-03
 * @Time 21:55
 */

package com.yunjuanyunshu.Entity;

import com.yunjuanyunshu.db.entity.BaseEntity;

import java.util.List;

public class TableEntity implements BaseEntity {

    /**
     * 字段列表
     */
    List<ColumnEntity> columnEntityList;

    /**
     * 数据库中表名
     */
    String tableDBName;
    /**
     * 数据库中表注释
     */
    String tableDBDesc;
    /**
     * Java中表名
     */
    String tableJavaName;
    /**
     * Java中表注释
     */
    String tableJavaDesc;

    /**
     * 包名
     */
    String packageStr;

    /**
     * 数据库中表类型
     */
    String tableDBType;


    public List<ColumnEntity> getColumnEntityList() {
        return columnEntityList;
    }

    public void setColumnEntityList(List<ColumnEntity> columnEntityList) {
        this.columnEntityList = columnEntityList;
    }

    public String getTableDBName() {
        return tableDBName;
    }

    public void setTableDBName(String tableDBName) {
        this.tableDBName = tableDBName;
    }

    public String getTableDBDesc() {
        return tableDBDesc;
    }

    public void setTableDBDesc(String tableDBDesc) {
        this.tableDBDesc = tableDBDesc;
    }

    public String getTableJavaName() {
        return tableJavaName;
    }

    public void setTableJavaName(String tableJavaName) {
        this.tableJavaName = tableJavaName;
    }

    public String getTableJavaDesc() {
        return tableJavaDesc;
    }

    public void setTableJavaDesc(String tableJavaDesc) {
        this.tableJavaDesc = tableJavaDesc;
    }

    public String getPackageStr() {
        return packageStr;
    }

    public void setPackageStr(String packageStr) {
        this.packageStr = packageStr;
    }

    public String getTableDBType() {
        return tableDBType;
    }

    public void setTableDBType(String tableDBType) {
        this.tableDBType = tableDBType;
    }
}
