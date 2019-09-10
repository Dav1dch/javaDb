package Sync;
import Databases.connectDatabase;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SyncDatabase {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/test?useSSL=false&serverTimezone=UTC";
        String url1 = "jdbc:mysql://localhost:3306/test2?useSSL=false&serverTimezone=UTC";
        String user = "root";
        String password = "setsunaFseiei";
        connectDatabase cd = new connectDatabase(url, user, password);
        connectDatabase cd1 = new connectDatabase(url1, user, password);
        try {
            cd1.getRs("select * from table_test2");
            cd.getRs("select * from table_test");
            while (cd1.rs.next()){
                System.out.println(cd1.rs.getString(2));
            }
            while (cd.rs.next()){
                System.out.println(cd.rs.getString(1));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }




    }


}
