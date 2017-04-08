/**
 * @File GridMain.java
 * @author zeng
 * @Date 2017-03-29
 * @Time 09:37
 */

package com.yunjuanyunshu.ui;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.yunjuanyunshu.CodeMakerSettings;
import com.yunjuanyunshu.CodeTemplate;
import com.yunjuanyunshu.Entity.ColumnEntity;
import com.yunjuanyunshu.Entity.TableEntity;
import com.yunjuanyunshu.action.CreateFileAction;
import com.yunjuanyunshu.db.dao.TableDao;
import com.yunjuanyunshu.util.CodeMakerUtil;
import com.yunjuanyunshu.util.TemplateKeyUtil;
import com.yunjuanyunshu.util.VelocityUtil;
import org.gsonformat.intellij.ConvertBridge;
import org.gsonformat.intellij.ui.ComboBoxTableModel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumnModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GridMain extends JFrame implements ConvertBridge.Operator {
    private JButton okButton;
    private JButton cancelButton;
    private JScrollPane gridPane;
    private JPanel mainPanel;
    private JTable mainTable;
    private JTextField tableNameText;
    private JTextField classNameText;
    private JTextField tableDescText;
    private JTextField classDescText;
    private JTextField PackageText;
    private JList listDBTable;
    private JScrollPane listPane;
    private Project project;
    private JFrame father;

    private HashMap<String,TableEntity> tableMap;
    private ComboBoxTableModel tableModel;

    private String schema;
    private String DBUSER;
    private String DBPASSWORD;
    private String DBURL;

    public GridMain(Project project, JFrame father, String schema, String DBUSER, String DBPASSWORD, String DBURL) {

        this.schema = schema;
        this.DBUSER = DBUSER;
        this.DBPASSWORD = DBPASSWORD;
        this.DBURL = DBURL;
        setContentPane(mainPanel);
        this.father = father;
        this.project = project;
        setTitle("MySQlConnext");
        getRootPane().setDefaultButton(okButton);
        this.setAlwaysOnTop(true);
        JComboBox comboBox = new JComboBox(ComboBoxTableModel.getValidStates());
        comboBox.setEditable(true);
        tableModel = new ComboBoxTableModel();
        listDBTable = new JList();
        listDBTable.setModel(new DefaultComboBoxModel(getTableInfo(schema,DBUSER,DBPASSWORD,DBURL)));
        //listDBTable.setModel(new DefaultComboBoxModel(new String[]{"so_student_info","test"}));
        mainTable = new JTable(tableModel);
        DefaultCellEditor editor = new DefaultCellEditor(comboBox);
        JCheckBox checkBox = new JCheckBox();
        checkBox.setHorizontalAlignment(JLabel.CENTER);
        checkBox.setEnabled(true);
        DefaultCellEditor editorCheck = new DefaultCellEditor(checkBox);

        TableColumnModel tcm = mainTable.getColumnModel();
        tcm.getColumn(6).setCellEditor(editor);
        tcm.getColumn(8).setCellEditor(editorCheck);
        tcm.getColumn(9).setCellEditor(editorCheck);
        tcm.getColumn(10).setCellEditor(editorCheck);
        // Set column widths
        tcm.getColumn(0).setPreferredWidth(100);
        //tcm.getColumn(1).setPreferredWidth(100);
        mainTable.setRowHeight(30);
        mainTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        mainTable.setPreferredScrollableViewportSize(mainTable.getPreferredSize());
        gridPane.setViewportView(mainTable);
        listPane.setViewportView(listDBTable);
        //getContentPane().add(new JScrollPane(mainTable), "Center");
        initActionListener();
        father.setVisible(false);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    private void initActionListener(){
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK(e);
            }
        });
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                onCancel();
            }
        });
        listDBTable.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                TableEntity tmptableInfo ;
                String tmpTableName = (String) listDBTable.getSelectedValue();
                if(tableMap.containsKey(tmpTableName)){
                    tmptableInfo = tableMap.get(tmpTableName);
                    if(tmptableInfo != null && tmptableInfo.getColumnEntityList() != null){
                        tableModel.setDataFromTableEntity(tmptableInfo);
                        tableNameText.setText(tmptableInfo.getTableDBName());
                        tableDescText.setText(tmptableInfo.getTableDBDesc());
                        classNameText.setText(tmptableInfo.getTableJavaName());
                        classDescText.setText(tmptableInfo.getTableJavaDesc());
                    }
                }
                mainTable.validate();
                mainTable.repaint();
                mainTable.doLayout();
                mainTable.updateUI();
            }
        });

    }

    private void onOK(ActionEvent anActionEvent) {
        CodeMakerSettings settings = ServiceManager.getService(CodeMakerSettings.class);
        CodeTemplate codeTemplate = settings.getCodeTemplate("Converter");
        Map<String, Object> map = TemplateKeyUtil.getTemplateKeyMap(getEntityFromTableData());
        String contentStr = VelocityUtil.evaluate(codeTemplate.getCodeTemplate(), map);
//        String content = "package cn.edu.zjvtit.empdemo;\n" +
//                "public class abc {\n" +
//                "String a;\n" +
//                "}";
        // async write action
        ApplicationManager.getApplication().runWriteAction(
                new CreateFileAction(CodeMakerUtil.generateClassPath(project,
                        PackageText.getText(), classNameText.getText()), contentStr, project));



    }



    private void onCancel() {
        father.dispose();
        //father.setVisible(true);
        dispose();
    }

    @Override
    public void showError(ConvertBridge.Error err) {

    }

    @Override
    public void setErrorInfo(String error) {

    }

    @Override
    public void cleanErrorInfo() {

    }

    private TableEntity getEntityFromTableData(){
        TableEntity tableEntity = new TableEntity();
        tableEntity.setPackageStr(PackageText.getText());
        tableEntity.setTableDBName(tableNameText.getText());
        tableEntity.setTableDBDesc(tableDescText.getText());
        tableEntity.setTableJavaName(classNameText.getText());
        tableEntity.setTableJavaDesc(classDescText.getText());
        List<ColumnEntity> tmpColList = new ArrayList<ColumnEntity>();
        String tmpColName="";
        for(int i =0;i<mainTable.getRowCount();i++){
            ColumnEntity tmpColumnEntity = new ColumnEntity();
            for (int j=0;j<mainTable.getColumnCount();j++){
                tmpColName = mainTable.getColumnName(j);
                // "DBName", "DBType","DBLengtg","DBDesc","JavaName","JavaType","JavaDesc","list","form","edit","max","min"
                String valueStr = (!tmpColName.equals("list") && !tmpColName.equals("form") && !tmpColName.equals("edit") && !tmpColName.equals("DBNullAble")) ? String.valueOf(mainTable.getValueAt(i,j)) : "";
                Boolean boolValue = (tmpColName.equals("list") || tmpColName.equals("form") || tmpColName.equals("edit") || tmpColName.equals("DBNullAble"))?(Boolean)mainTable.getValueAt(i,j) : false;
                switch (tmpColName){
                    case "DBName":
                        tmpColumnEntity.setColDBName(valueStr);
                        break;
                    case "DBType":
                        tmpColumnEntity.setColDBType(valueStr);
                        break;
                    case "DBLength":
                        tmpColumnEntity.setColDBLength(Long.getLong(valueStr,0));
                        break;
                    case "DBDesc":
                        tmpColumnEntity.setColDBDesc(valueStr);
                        break;
                    case "JavaName":
                        tmpColumnEntity.setColJavaName(valueStr);
                        break;
                    case "JavaType":
                        tmpColumnEntity.setColJavaType(valueStr);
                        break;
                    case "JavaDesc":
                        tmpColumnEntity.setColJavaDesc(valueStr);
                        break;
                    case "ListView":
                        tmpColumnEntity.setListView(boolValue);
                        break;
                    case "FormView":
                        tmpColumnEntity.setFormView(boolValue);
                        break;
                    case "EditAble":
                        tmpColumnEntity.setEditAble(boolValue);
                        break;
                    case "DBNullAble":
                        tmpColumnEntity.setColDBNullAble(boolValue);
                        break;
                    case "RangMax":
                        tmpColumnEntity.setColRangMax(Integer.getInteger(valueStr,0));
                        break;
                    case "RangMin":
                        tmpColumnEntity.setColRangMin(Integer.getInteger(valueStr,0));
                        break;
                }
            }
            tmpColList.add(tmpColumnEntity);
        }
        tableEntity.setColumnEntityList(tmpColList);
        return  tableEntity;
    }



    /**
     * 获取表名数组
     * @return 表名数组
     */
    public String[] getTableInfo(String schema,String DBUSER,String DBPASSWORD,String DBURL){
        TableDao tableDao = new TableDao(DBUSER,DBPASSWORD, DBURL);
        try {
            tableMap = tableDao.findHashMapAllTableInfo(schema);
            return tableDao.findAllTableName(schema);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



}
