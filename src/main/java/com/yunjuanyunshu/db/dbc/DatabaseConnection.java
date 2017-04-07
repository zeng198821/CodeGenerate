/**
 * @File DatabaseConnection.java
 * @author zeng
 * @Date 2017-03-27
 * @Time 13:16
 */

package com.yunjuanyunshu.db.dbc;



import java.sql.*;

public class DatabaseConnection {

    private static final String DBDRIVER="com.mysql.jdbc.Driver";
    private static final String DBURL="jdbc:mysql://localhost:3306/zjvtit";
    private static final String DBUSER="root";
    private static final String DBPASSWORD="password";
    private Connection conn=null;
    public DatabaseConnection(String DBUSER,String DBPASSWORD,String DBURL){
        try{
            Class.forName(DBDRIVER);
            this.conn= DriverManager.getConnection("jdbc:mysql://"+DBURL,DBUSER,DBPASSWORD);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public DatabaseConnection(){
        try{
            Class.forName(DBDRIVER);
            this.conn= DriverManager.getConnection(DBURL,DBUSER,DBPASSWORD);
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    public Connection getConnection(){
        return this.conn;
    }

    public void closeConnection(){
        if(this.conn!=null){
            try{
                this.conn.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public boolean testDBConn(){
        boolean canConn = false;
        try {
            if (this.conn != null){
                String sql="SELECT 'x' tt";
                PreparedStatement pstmt=null;
                pstmt=this.conn.prepareStatement(sql);
                ResultSet rs=pstmt.executeQuery();
                if(rs.next()){
                    String tmp = rs.getString("tt");
                    if(tmp != null && tmp.equals("x")){
                        canConn =  true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return canConn;
    }

}
