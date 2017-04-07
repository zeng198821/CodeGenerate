/**
 * @File ComboBoxTableModel.java
 * @author zeng
 * @Date 2017-03-29
 * @Time 14:47
 */

package org.gsonformat.intellij.ui;

import com.yunjuanyunshu.Entity.ColumnEntity;
import com.yunjuanyunshu.Entity.TableEntity;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class ComboBoxTableModel extends AbstractTableModel {

    public int getRowCount() {
        return data.length;
    }

    public int getColumnCount() {
        return COLUMN_COUNT;
    }

    public Object getValueAt(int row, int column) {
        return data[row][column];
    }



    public Class getColumnClass(int column) {
        return (data[0][column]).getClass();
    }

    public String getColumnName(int column) {
        return columnNames[column];
    }

    public boolean isCellEditable(int row, int column) {
        return !(column == 0 || column == 1 || column == 2 || column == 3);
    }

    public void setValueAt(Object value, int row, int column) {
        //if (isValidValue(value)) {
            data[row][column] = value;
            fireTableRowsUpdated(row, row);
        //}
    }

    // Extra public methods
    public static String[] getValidStates() {
        return validStates;
    }

    // Protected methods
    protected boolean isValidValue(Object value) {
        if (value instanceof String) {
            String sValue = (String)value;

            for (int i = 0; i < validStates.length; i++) {
                if (sValue.equals(validStates[i])) {
                    return true;
                }
            }
        }

        return false;
    }

    protected static final int COLUMN_COUNT = 13;

    protected static final String[] validStates = {
            "String", "long", "int","float","double","boolean","Date"
    };

    public void setDataFromTableEntity(TableEntity tableEntity){
        if(tableEntity == null || tableEntity.getColumnEntityList() == null){
            return;
        }
        List<ColumnEntity> tmpColList = tableEntity.getColumnEntityList();
        Object[][] tmpdata = new Object[tmpColList.size()][13];
        int i =0;
        for(ColumnEntity tmp : tmpColList){
            tmpdata[i][0] = tmp.getColDBName();
            tmpdata[i][1] = tmp.getColDBType();
            tmpdata[i][2] = tmp.getColDBLength();
            tmpdata[i][3] = tmp.getColDBDesc();
            tmpdata[i][4] = tmp.getColDBNullAble();
            tmpdata[i][5] = tmp.getColJavaName();
            tmpdata[i][6] = tmp.getColJavaType();
            tmpdata[i][7] = tmp.getColJavaDesc();
            tmpdata[i][8] = true;
            tmpdata[i][9] = true;
            tmpdata[i][10] = true;
            tmpdata[i][11] = "";
            tmpdata[i][12] = "";
            i++;
        }
        data = tmpdata;
        fireTableRowsUpdated(0,tmpColList.size());
        fireTableCellUpdated(0,tmpColList.size());

    }

    protected Object[][] data = new Object[][]
            {

//            { "id", "varchar(64)","64","流水ID","id", "String","流水ID",false,false,true,"","" },
//            { "student_no", "varchar(64)","64","学号","studentNo", "String","学号",true,true,true,"",""},
//            { "student_name", "varchar(20)","20","学生姓名","studentName", "String","学生姓名",true,true,true,"",""},
//            { "class_no", "varchar(64)","64","班级ID","classNo", "String","班级ID",false,true,true,"",""},
//            { "sex", "char(1)" ,"1","性别","sex", "String" ,"性别",false,true,true,"",""},
//            { "age", "int(10)" ,"10","年龄","age", "Long" ,"年龄",true,true,true,"18","30"},
//            { "student_type","char(5)","5","学生来源","studentType","String","学生来源",false,false,true,"",""}
    };

    protected static final String[] columnNames = {
            "DBName", "DBType","DBLength","DBDesc","DBNullAble","JavaName","JavaType","JavaDesc","list","form","edit","max","min"
    };
}
