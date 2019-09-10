package Databases;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;


public class connectDatabase {
    public String driver, url, user, password;
    public PreparedStatement prStatement;
    public Connection con;
    public ResultSet rs;

    public connectDatabase(String uurl, String uuser, String upassword){
        driver = "com.mysql.cj.jdbc.Driver";
        url = uurl;
        user = uuser;
        password = upassword;
        try{
            Class.forName(driver);
            con = DriverManager.getConnection(url, user, password);
            if (!con.isClosed()) {
                System.out.println("connect to database successfully");
            }
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void getRs(String sql) throws SQLException {
        prStatement = con.prepareStatement(sql);
        rs = prStatement.executeQuery();
    }

    public static void main(String[] args) {
        connectDatabase cd = new connectDatabase("jdbc:mysql://localhost:3306/test?useSSL=false&serverTimezone=UTC", "root", "setsunaFseiei");
        try {
            cd.getRs("select * from table_test;");
        }catch (SQLException e){
            e.printStackTrace();
        }

    }
}

