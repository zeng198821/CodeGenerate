/**
 * @File GridMain.java
 * @author zeng
 * @Date 2017-03-29
 * @Time 09:37
 */

package com.yunjuanyunshu.ui;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.yunjuanyunshu.CodeMakerSettings;
import com.yunjuanyunshu.CodeTemplate;
import com.yunjuanyunshu.Entity.ColumnEntity;
import com.yunjuanyunshu.Entity.TableEntity;
import com.yunjuanyunshu.action.CreateFileAction;
import com.yunjuanyunshu.db.dao.ColumnDao;
import com.yunjuanyunshu.db.dao.TableDao;
import com.yunjuanyunshu.db.dbc.DatabaseConnection;
import com.yunjuanyunshu.util.CodeMakerUtil;
import com.yunjuanyunshu.util.TemplateKeyUtil;
import com.yunjuanyunshu.util.VelocityUtil;
import org.gsonformat.intellij.ConvertBridge;
import org.gsonformat.intellij.ui.ComboBoxTableModel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MouseInputListener;
import javax.swing.table.TableColumnModel;
import java.awt.event.*;
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
    private JList listClassType;
    private JScrollPane listClassPane;
    private JTextField ipText;
    private JTextField portText;
    private JTextField schemaText;
    private JTextField userNameText;
    private JTextField passwordText;
    private JButton connButton;
    private JTextField classRootText;


    private HashMap<String,TableEntity> tableMap;
    private ComboBoxTableModel tableModel;
    private CodeMakerSettings settings;


    private PsiClass cls;
    private PsiFile file;
    private Project project;

    private boolean isConnect;


    private TableDao tableDao;


    public GridMain(PsiClass cls, PsiFile file, Project project) {
        this.cls = cls;
        this.file = file;
        this.project = project;
        isConnect = false;
        setContentPane(mainPanel);
        //this.father = father;
        this.project = project;
        settings = ServiceManager.getService(CodeMakerSettings.class);
        setTitle("Code Generate                      By Zeng");
        getRootPane().setDefaultButton(okButton);
        this.setAlwaysOnTop(true);
        JComboBox comboBox = new JComboBox(ComboBoxTableModel.getValidStates());
        comboBox.setEditable(true);
        tableModel = new ComboBoxTableModel();
        listDBTable = new JList();
        listDBTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listClassType = new JList(settings.getCodeTemplateNamesArray());
        listClassType.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        //listClassType.setModel();
        //listDBTable.setModel(new DefaultComboBoxModel(new String[]{"so_student_info","test"}));
        mainTable = new JTable(tableModel);
        MouseInputListener mouseInputListener = getMouseInputListener(mainTable);//
        mainTable.addMouseListener(mouseInputListener);
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
        tcm.getColumn(11).setCellEditor(editorCheck);
        tcm.getColumn(12).setCellEditor(editorCheck);
        tcm.getColumn(13).setCellEditor(editorCheck);
        tcm.getColumn(14).setCellEditor(editorCheck);
        // Set column widths
        tcm.getColumn(0).setPreferredWidth(100);
        tcm.getColumn(8).setPreferredWidth(50);
        tcm.getColumn(9).setPreferredWidth(50);
        tcm.getColumn(10).setPreferredWidth(50);
        tcm.getColumn(11).setPreferredWidth(50);
        tcm.getColumn(12).setPreferredWidth(50);
        tcm.getColumn(13).setPreferredWidth(50);
        tcm.getColumn(14).setPreferredWidth(50);
        //tcm.getColumn(1).setPreferredWidth(100);
        mainTable.setRowHeight(30);
        mainTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        mainTable.setPreferredScrollableViewportSize(mainTable.getPreferredSize());
        gridPane.setViewportView(mainTable);
        listPane.setViewportView(listDBTable);
        listClassPane.setViewportView(listClassType);
        //getContentPane().add(new JScrollPane(mainTable), "Center");
        getDbConfig();
        initActionListener();
        //father.setVisible(false);
        setSize(1366, 768);
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
                String tmpIp = ipText.getText();
                String tmpPort = portText.getText();
                String tmpSchema = schemaText.getText();
                String tmpDBUrl = tmpIp + ":" + tmpPort + "/" + tmpSchema;
                String tmpDBUser = userNameText.getText();
                String tmpDBPasswrd = passwordText.getText();

                if(tableMap.containsKey(tmpTableName)){
                    tmptableInfo = tableMap.get(tmpTableName);
                    ColumnDao columnDao =new ColumnDao(tmpDBUser,tmpDBPasswrd ,tmpDBUrl);
                    try {
                        tmptableInfo.setColumnEntityList(columnDao.findAllColumnInfo(tmpSchema,tmpTableName));
                    } catch (Exception e1) {
                        tmptableInfo.setColumnEntityList(null);
                        e1.printStackTrace();
                    }
                    if(tmptableInfo != null && tmptableInfo.getColumnEntityList() != null){
                        tableModel.setDataFromTableEntity(tmptableInfo);
                        tableNameText.setText(tmptableInfo.getTableDBName());
                        tableDescText.setText(tmptableInfo.getTableDBDesc());
                        classNameText.setText(tmptableInfo.getTableJavaName());
                        classDescText.setText(tmptableInfo.getTableJavaDesc());
                    }
                    // 清空模板选中
                    listClassType.clearSelection();
                    listClassType.updateUI();
                }
                // 刷新界面
                mainTable.validate();
                mainTable.repaint();
                mainTable.doLayout();
                mainTable.updateUI();
            }
        });

        /**
         * 点击数据库登录事件
         */
        connButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(!isConnect){
                    doConnDB();
                }else {
                    doDisConnDB();
                }
            }
        });

        listClassType.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                if(e.getClickCount()==2){   //When double click JList
                    int tmpListCount = listClassType.getModel().getSize();
                    int[] tmpListSelectedIdx  = new int[tmpListCount];
                    for(int i =0;i<tmpListCount;i++){
                        tmpListSelectedIdx[i] = i;
                    }
                    listClassType.setSelectedIndices(tmpListSelectedIdx);
                    //listClassType.setSelectedValue();
                }
            }
        });

    }

    private void doConnDB (){
        if(isConnect){
            Messages.showInfoMessage("数据库已经连接，无需再次登录", "数据库连接");
            //JOptionPane.showMessageDialog(null, "数据库已经连接，无需再次登录", "数据库连接", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String tmpIp = ipText.getText();
        String tmpPort = portText.getText();
        String tmpSchema = schemaText.getText();
        String tmpDBUrl = tmpIp + ":" + tmpPort + "/" + tmpSchema;
        String tmpDBUser = userNameText.getText();
        String tmpDBPasswrd = passwordText.getText();
        DatabaseConnection dbc=new DatabaseConnection(tmpDBUser,tmpDBPasswrd,tmpDBUrl);
        connButton.setEnabled(false);
        if(dbc.testDBConn()) {
            connButton.setText("Dis Conn");
            isConnect = true;
            doEnable(!isConnect);
            listDBTable.setModel(new DefaultComboBoxModel(getTableInfo(tmpSchema, tmpDBUser, tmpDBPasswrd, tmpDBUrl)));
            setDBConfig(tmpIp,tmpPort,tmpSchema,tmpDBUser,tmpDBPasswrd);
        }else {
            Messages.showErrorDialog("数据库连接失败", "数据库连接");
            //JOptionPane.showMessageDialog(null, "数据库连接失败", "数据库连接", JOptionPane.WARNING_MESSAGE);
        }
        connButton.setEnabled(true);

    }
    private void doDisConnDB (){
        tableDao.disConn();
        isConnect = false;
        listDBTable.setModel(new DefaultComboBoxModel(new String[0]));
        connButton.setText("Conn");
        doEnable(!isConnect);
    }

    private void doEnable(boolean enable){
        ipText.setEnabled(enable);
        portText.setEnabled(enable);
        schemaText.setEnabled(enable);
        userNameText.setEnabled(enable);
        passwordText.setEnabled(enable);
    }
    private void onOK(ActionEvent anActionEvent) {
        // 执行生成代码

        List<CodeTemplate> tmpTemplateList = getCodeTemplateList();
        if(tmpTemplateList == null || tmpTemplateList.size() == 0){
            Messages.showInfoMessage("未选中任何模板", "代码生成");
            //JOptionPane.showMessageDialog(null, "未选中任何模板", "代码生成", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String tmpClassRootPath = classRootText.getText();
        String tmpPackage = PackageText.getText();
        String tempInfoStr="文件：\n";
        String tmpFilePath = "";
        int tmpSuccessCount=0;
        for(CodeTemplate codeTemplate : tmpTemplateList){
            try{
                Map<String, Object> map = TemplateKeyUtil.getTemplateKeyMap(getEntityFromTableData());
                String contentStr = VelocityUtil.evaluate(codeTemplate.getCodeTemplate(), map);
                tmpFilePath = CodeMakerUtil.generateClassPath(project,tmpClassRootPath,
                        PackageText.getText(), VelocityUtil.evaluate(codeTemplate.getClassNameVm(), map));
                // 异步写入
                ApplicationManager.getApplication().runWriteAction(
                        new CreateFileAction(tmpFilePath, contentStr, project));
                tempInfoStr = tempInfoStr + "\t"+tmpFilePath+"\n";
                tmpSuccessCount++;
            }catch (Exception ex){

                ex.printStackTrace();
                //JOptionPane.showMessageDialog(null, codeTemplate.getClassNameVm()+ " 生成错误，请检查代码！", "代码生成", JOptionPane.ERROR_MESSAGE);
                Messages.showErrorDialog(codeTemplate.getClassNameVm()+ " 生成错误，请检查代码:\n"+ex.getMessage()+"\n"+ex.toString(), "代码生成");
            }
        }
        PropertiesComponent.getInstance().setValue("codeGenerteClassPath",tmpClassRootPath);
        PropertiesComponent.getInstance().setValue("codeGenertePackage",tmpPackage);
        tempInfoStr = tempInfoStr + "生成成功！";
        //JOptionPane.showMessageDialog(null, tempInfoStr, "代码生成", JOptionPane.INFORMATION_MESSAGE);
        if(tmpSuccessCount>0){
            Messages.showInfoMessage(tempInfoStr, "代码生成");
        }

    }

    private void setDBConfig(String tmpIp,String tmpPort,String tmpSchema,String tmpDBUser,String tmpDBPasswrd){
        PropertiesComponent.getInstance().setValue("codeGenerteDBIpAddress",tmpIp);
        PropertiesComponent.getInstance().setValue("codeGenerteDBPort",tmpPort);
        PropertiesComponent.getInstance().setValue("codeGenerteDBSchema",tmpSchema);
        PropertiesComponent.getInstance().setValue("codeGenerteDBUserName",tmpDBUser);
        PropertiesComponent.getInstance().setValue("codeGenerteDBPassword",tmpDBPasswrd);
    }
    private void getDbConfig(){
        ipText.setText(PropertiesComponent.getInstance().getValue("codeGenerteDBIpAddress"));
        portText.setText(PropertiesComponent.getInstance().getValue("codeGenerteDBPort"));
        schemaText.setText(PropertiesComponent.getInstance().getValue("codeGenerteDBSchema"));
        userNameText.setText(PropertiesComponent.getInstance().getValue("codeGenerteDBUserName"));
        passwordText.setText(PropertiesComponent.getInstance().getValue("codeGenerteDBPassword"));
        classRootText.setText(PropertiesComponent.getInstance().getValue("codeGenerteClassPath"));
        PackageText.setText(PropertiesComponent.getInstance().getValue("codeGenertePackage"));
    }

    private void onCancel() {

        //father.dispose();
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

    /**
     * 获取表中字段配置信息
     * @return
     */
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
                String valueStr = (!tmpColName.equals("list") && !tmpColName.equals("form") && !tmpColName.equals("edit") && !tmpColName.equals("select") && !tmpColName.equals("DBNullAble") && !tmpColName.equals("JavaNullAble") && !tmpColName.equals("exp") && !tmpColName.equals("imp")) ? String.valueOf(mainTable.getValueAt(i,j)) : "";
                Boolean boolValue = (tmpColName.equals("list") || tmpColName.equals("form") || tmpColName.equals("edit") || tmpColName.equals("DBNullAble") || tmpColName.equals("JavaNullAble") || tmpColName.equals("exp") || tmpColName.equals("imp")) ? (Boolean)mainTable.getValueAt(i,j) : false;
                switch (tmpColName){
                    case "DBName":
                        tmpColumnEntity.setColDBName(valueStr);
                        break;
                    case "DBType":
                        tmpColumnEntity.setColDBType(valueStr);
                        break;
                    case "DBLength":
                        tmpColumnEntity.setColDBLength(parseLong(valueStr));
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
                    case "JavaNullAble":
                        tmpColumnEntity.setColJavaNullAble(boolValue);
                        break;
                    case "list":
                        tmpColumnEntity.setListView(boolValue);
                        break;
                    case "form":
                        tmpColumnEntity.setFormView(boolValue);
                        break;
                    case "edit":
                        tmpColumnEntity.setEditAble(boolValue);
                        break;
                    case "select":
                        tmpColumnEntity.setSelect(boolValue);
                        break;
                    case "exp":
                        tmpColumnEntity.setExp(boolValue);
                        break;
                    case "imp":
                        tmpColumnEntity.setImp(boolValue);
                        break;
                    case "dict":
                        tmpColumnEntity.setDict(valueStr);
                        break;
                    case "max":
                        tmpColumnEntity.setColRangMax(parseDouble(valueStr));
                        break;
                    case "min":
                        tmpColumnEntity.setColRangMin(parseDouble(valueStr));
                        break;
                }
            }
            tmpColList.add(tmpColumnEntity);
        }
        tableEntity.setColumnEntityList(tmpColList);
        return  tableEntity;
    }

    private Long parseLong(String val){
        long tmp = 0L;
        try{
            tmp = Long.parseLong(val);
        }catch (Exception e){return null;};

        return tmp;
    }
    private Integer parseInt(String val){
        int tmp = 0;
        try{
            tmp = Integer.parseInt(val);
        }catch (Exception e){return null;};
        return tmp;
    }
    private Float parseFloat(String val){
        float tmp = 0;
        try{
            tmp = Float.parseFloat(val);
        }catch (Exception e){return null;};
        return tmp;
    }
    private Double parseDouble(String val){
        double tmp = 0;
        try{
            tmp = Double.parseDouble(val);
        }catch (Exception e){return null;};
        return tmp;
    }
    /**
     * 获取表名数组
     * @return 表名数组
     */
    public String[] getTableInfo(String schema,String DBUSER,String DBPASSWORD,String DBURL){
        tableDao = new TableDao(DBUSER,DBPASSWORD, DBURL);
        try {
            tableMap = tableDao.findHashMapAllTableInfo(schema);
            return tableDao.findAllTableName(schema);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取选中的模板列表
     * @return 选中的模板列表
     */
    private List<CodeTemplate> getCodeTemplateList (){
        List<CodeTemplate> tmpCodeTemplateList = new ArrayList<CodeTemplate>();
        List<String>  tmpSelectedList =  listClassType.getSelectedValuesList();
        CodeMakerSettings settings = ServiceManager.getService(CodeMakerSettings.class);
        for(String tmp : tmpSelectedList){
            tmpCodeTemplateList.add(settings.getCodeTemplate(tmp));
        }
        return tmpCodeTemplateList;
    }


    private MouseInputListener getMouseInputListener(final JTable jTable) {
        return new MouseInputListener() {

            final List<Integer> showMenuColIdxs = java.util.Arrays.asList(new Integer[]{8,9,10,11,12,13,14});

            public void mouseClicked(MouseEvent e) {
                processEvent(e);
            }

            /***
             * //in order to trigger Left-click the event
             */
            public void mousePressed(MouseEvent e) {
                processEvent(e);// is necessary!!!
            }

            public void mouseReleased(MouseEvent e) {
                // processEvent(e);

                //return;
                if (e.getButton() == MouseEvent.BUTTON3) {// right click
                    if(!showMenuColIdxs.contains(jTable.getSelectedColumn())){
                        return;
                    }
                    JPopupMenu popupmenu = new JPopupMenu();
                    JMenuItem runM = new JMenuItem("全选");
                    JMenuItem copyParameterM = new JMenuItem("反选");
                    MyMenuActionListener yMenuActionListener = new MyMenuActionListener(jTable);
                    runM.addActionListener(yMenuActionListener);
                    copyParameterM.addActionListener(yMenuActionListener);
                    popupmenu.add(runM);
                    popupmenu.add(copyParameterM);
                    popupmenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }




            public void mouseEntered(MouseEvent e) {
                processEvent(e);
            }

            public void mouseExited(MouseEvent e) {
                processEvent(e);
            }

            public void mouseDragged(MouseEvent e) {
                processEvent(e);
            }

            public void mouseMoved(MouseEvent e) {
                processEvent(e);
            }

            private void processEvent(MouseEvent e) {
                // Right-click on
                if ((e.getModifiers() & MouseEvent.BUTTON3_MASK) != 0) {
                    // System.out.println(e.getModifiers());
                    // System.out.println("Right-click on");
                    int modifiers = e.getModifiers();
                    modifiers -= MouseEvent.BUTTON3_MASK;
                    //modifiers |= MouseEvent.BUTTON1_MASK;
                    MouseEvent ne = new MouseEvent(e.getComponent(), e.getID(),
                            e.getWhen(), modifiers, e.getX(), e.getY(),
                            e.getClickCount(), false);
                    jTable.dispatchEvent(ne);// in order to trigger Left-click
                    jTable.updateUI();
                    // the event
                }
            }
        };
    }

    /**
     * 右键操作
     */
    class MyMenuActionListener implements ActionListener {

        JTable jTable;
        public MyMenuActionListener(JTable jTable){
            this.jTable = jTable;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if (command.equalsIgnoreCase("全选")) {
                setSelectAll(jTable.getSelectedColumn());

            } else if (command.equalsIgnoreCase("反选")) {
                setSelectOpposite(jTable.getSelectedColumn());
            }
        }

        private void setSelectAll(int colIdx){
            int tmpTableRows = jTable.getRowCount();
            for(int i =0 ;i<tmpTableRows;i++){
                jTable.setValueAt(Boolean.TRUE,i,colIdx);
            }
            jTable.updateUI();
        }

        private void setSelectOpposite(int colIdx){
            int tmpTableRows = jTable.getRowCount();
            for(int i =0 ;i<tmpTableRows;i++){
                jTable.setValueAt(((Boolean)jTable.getValueAt(i,colIdx)).equals(Boolean.TRUE) ? Boolean.FALSE :Boolean.TRUE,i,colIdx);
            }
            jTable.updateUI();
        }

    }


}
