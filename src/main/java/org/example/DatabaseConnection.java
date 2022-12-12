package org.example;

import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection {

    public List<String> getDBTable(String db) {
        List<String> tables = new ArrayList<>();
        String url = "jdbc:sqlite:" + db;

        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);

        String queryTables = "SELECT name FROM sqlite_master WHERE type ='table' AND name NOT LIKE 'sqlite_%'";
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = con.prepareStatement(queryTables)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    tables.add(resultSet.getString("name"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tables;
    }

}
