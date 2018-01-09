/**
 * @File DBConn.java
 * @author zeng
 * @Date 2017-03-28
 * @Time 17:35
 */

package com.yunjuanyunshu.ui;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.yunjuanyunshu.db.dbc.DatabaseConnection;
import org.gsonformat.intellij.ConvertBridge;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DBConn extends JFrame implements ConvertBridge.Operator {
    private JButton cancelButton;
    private JButton connectButton;
    private JTextField textIpAddress;
    private JTextField textUserName;
    private JTextField textPassword;
    private JPanel mainPanel;
    private JComboBox comboBox1;

    private CardLayout cardLayout;

    private PsiClass cls;
    private PsiFile file;
    private Project project;
    private String errorInfo = null;
    private String currentClass = null;



    public DBConn(PsiClass cls, PsiFile file, Project project) throws HeadlessException {
        this.cls = cls;
        this.file = file;
        this.project = project;
        setContentPane(mainPanel);
        setTitle("MySQlConnext");
        getRootPane().setDefaultButton(connectButton);
        this.setAlwaysOnTop(true);
        initGeneratePanel();
        initListener(this);
        setSize(300,200);
        setVisible(true);
    }


    private void initListener(DBConn dbConn) {

        connectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String tmpDBUrl = textIpAddress.getText();
                String tmpSchema = tmpDBUrl.substring(tmpDBUrl.lastIndexOf("/") + 1);
                String tmpDBUser = textUserName.getText();
                String tmpDBPasswrd = textPassword.getText();
                DatabaseConnection dbc=new DatabaseConnection(tmpDBUser,tmpDBPasswrd,tmpDBUrl);
                if(dbc.testDBConn()){
                    dbConn.setVisible(false);
                    //GridMain gridMain = new GridMain(project,dbConn,tmpSchema,tmpDBUser,tmpDBPasswrd,tmpDBUrl);
                }else {
                    JOptionPane.showMessageDialog(null, "数据库连接失败", "数据库连接", JOptionPane.WARNING_MESSAGE);
                }

            }
        });
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });
    }


    private void onOK() {

    }

    private void onCancel() {
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


    private void initGeneratePanel() {

    }
}
