package lk.ijse.Db;

import lombok.Getter;

import java.awt.dnd.DragGestureEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public class DbConnection {

    private static DbConnection dbConnection;
    @Getter
    private Connection connection;

    private DbConnection() throws SQLException {
        connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/channelingcenter",
                "root",
                "Mahee@10985"
        );
    }
    public static DbConnection getInstance() throws SQLException {
        if (Objects.isNull(dbConnection)) {
            dbConnection = new DbConnection();
            return dbConnection;
        }else{
            return dbConnection;
        }

    }
}
