package org.example;

import org.sqlite.SQLiteDataSource;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection {

    public List<String> getDBTable(String db) {
        List<String> tables = new ArrayList<>();
        String url = "jdbc:sqlite:" + db;

        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);
        dataSource.setReadOnly(true);

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
            System.out.println("file : " + db + " doesn't exit");
        }
        return tables;
    }

    DefaultTableModel executeQuery(String db, String query) {
        String url = "jdbc:sqlite:" + db;
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);

        List<Object> cols = new ArrayList<>();
        DefaultTableModel model = new DefaultTableModel();
        try (Connection con = dataSource.getConnection()) {
            try (Statement statement = con.createStatement()) {
                try (ResultSet columns = statement.executeQuery(query)) {
                    ResultSetMetaData rs = columns.getMetaData();
                    for (int i = 1; i <= rs.getColumnCount(); i++) {
                        cols.add(rs.getColumnName(i));
                    }
                    model.setColumnIdentifiers(cols.toArray());
                    while (columns.next()) {
                        List<Object> data = new ArrayList<>();
                        for (int i = 1; i <= rs.getColumnCount(); i++) {
                            data.add(columns.getObject(i));
                        }
                        model.addRow(data.toArray());
                    }
                } catch (SQLException e) {
                    System.out.println("Invalid Query");
                    return null;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return model;
    }
}
