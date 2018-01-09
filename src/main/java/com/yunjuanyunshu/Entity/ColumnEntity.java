/**
 * @File ColumnEntity.java
 * @author zeng
 * @Date 2017-04-03
 * @Time 21:55
 */

package com.yunjuanyunshu.Entity;

import com.yunjuanyunshu.db.entity.BaseEntity;

public class ColumnEntity implements BaseEntity {

    /**
     * 字段数据库名称
     */
    String colDBName;
    /**
     * 字段数据库类型
     */
    String colDBType;
    /**
     * 字段数据库注释
     */
    String colDBDesc;
    /**
     * 字段数据库长度
     */
    Long colDBLength;

    /**
     * 数据库该字段是否允许为空
     */
    Boolean colDBNullAble;

    /**
     * 字段Java名称
     */
    String colJavaName;
    /**
     * 字段Java类型
     */
    String colJavaType;
    /**
     * 字段Java注释
     */
    String colJavaDesc;
    /**
     * 最大范围值
     */
    Double colRangMax;
    /**
     * 最小范围值
     */
    Double colRangMin;

    /**
     * 是否列表展示
     */
    Boolean listView;
    /**
     * 是否表单展示
     */
    Boolean formView;
    /**
     * 是否表单编辑
     */
    Boolean editAble;

    /**
     * Java类该字段是否允许为空
     */
    Boolean colJavaNullAble;

    /**
     *
     * @return
     */
    public String getColDBName() {
        return colDBName;
    }

    /**
     *
     * @param colDBName
     */
    public void setColDBName(String colDBName) {
        this.colDBName = colDBName;
    }

    public String getColDBType() {
        return colDBType;
    }

    public void setColDBType(String colDBType) {
        this.colDBType = colDBType;
    }

    public String getColDBDesc() {
        return colDBDesc;
    }

    public void setColDBDesc(String colDBDesc) {
        this.colDBDesc = colDBDesc;
    }

    public Long getColDBLength() {
        return colDBLength;
    }

    public void setColDBLength(Long colDBLength) {
        this.colDBLength = colDBLength;
    }

    public String getColJavaName() {
        return colJavaName;
    }

    public void setColJavaName(String colJavaName) {
        this.colJavaName = colJavaName;
    }

    public String getColJavaType() {
        return colJavaType;
    }

    public void setColJavaType(String colJavaType) {
        this.colJavaType = colJavaType;
    }

    public String getColJavaDesc() {
        return colJavaDesc;
    }

    public void setColJavaDesc(String colJavaDesc) {
        this.colJavaDesc = colJavaDesc;
    }

    public Double getColRangMax() {
        return colRangMax;
    }

    public void setColRangMax(Double colRangMax) {
        this.colRangMax = colRangMax;
    }

    public Double getColRangMin() {
        return colRangMin;
    }

    public void setColRangMin(Double colRangMin) {
        this.colRangMin = colRangMin;
    }

    public Boolean getListView() {
        return listView;
    }

    public void setListView(Boolean listView) {
        this.listView = listView;
    }

    public Boolean getFormView() {
        return formView;
    }

    public void setFormView(Boolean formView) {
        this.formView = formView;
    }

    public Boolean getEditAble() {
        return editAble;
    }

    public void setEditAble(Boolean editAble) {
        this.editAble = editAble;
    }

    public Boolean getColDBNullAble() {
        return colDBNullAble;
    }

    public void setColDBNullAble(Boolean colDBNullAble) {
        this.colDBNullAble = colDBNullAble;
    }

    public Boolean getColJavaNullAble() {
        return colJavaNullAble;
    }

    public void setColJavaNullAble(Boolean colJavaNullAble) {
        this.colJavaNullAble = colJavaNullAble;
    }
}
