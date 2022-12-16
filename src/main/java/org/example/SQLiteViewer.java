package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class SQLiteViewer extends JFrame {
    JTextField fileNameTextField;
    JButton openFileButton;
    JComboBox tablesComboBox;
    JButton executeQueryButton;
    JTextArea queryTextArea;
    JScrollPane tableScrollPane;
    JTable table;

    public SQLiteViewer() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 900);
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);
        setTitle("SQLite Viewer");

        fileNameTextField = createFileNameTextField();
        add(fileNameTextField);

        openFileButton = createOpenFileButton();
        add(openFileButton);

        tablesComboBox = createTablesComboBox();
        add(tablesComboBox);

        executeQueryButton = createExecuteQueryButton();
        add(executeQueryButton);

        queryTextArea = createQueryTextArea();
        add(queryTextArea);

        tableScrollPane = createEmptyTable();
        add(tableScrollPane);

        openFileButton.addActionListener(actionEvent -> openDB(fileNameTextField.getText().trim()));
        tablesComboBox.addActionListener(actionEvent -> generateSelectQuery());
        executeQueryButton.addActionListener(actionEvent -> createTable());

        setVisible(true);


    }

    private JTextField createFileNameTextField() {
        JTextField fileNameTextField = new JTextField();
        fileNameTextField.setName("FileNameTextField");
        fileNameTextField.setLocation(10, 15);
        fileNameTextField.setSize(new Dimension(590, 20));
        return fileNameTextField;
    }

    private JButton createOpenFileButton() {
        JButton openFileButton = new JButton("Open");
        openFileButton.setName("OpenFileButton");
        openFileButton.setLocation(610, 15);
        openFileButton.setSize(65, 20);
        return openFileButton;
    }

    private JComboBox createTablesComboBox() {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        JComboBox tablesComboBox = new JComboBox(model);
        tablesComboBox.setName("TablesComboBox");
        tablesComboBox.setLocation(10, 55);
        tablesComboBox.setSize(665, 20);
        return tablesComboBox;
    }

    private JTextArea createQueryTextArea() {
        JTextArea queryTextArea = new JTextArea();
        queryTextArea.setName("QueryTextArea");
        queryTextArea.setLocation(10, 95);
        queryTextArea.setSize(565, 100);
        return queryTextArea;

    }

    private JButton createExecuteQueryButton() {
        JButton executeQueryButton = new JButton("Execute");
        executeQueryButton.setName("ExecuteQueryButton");
        executeQueryButton.setLocation(595, 95);
        executeQueryButton.setSize(80, 30);
        return executeQueryButton;
    }

    private void createTable() {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        DefaultTableModel defaultTableModel = databaseConnection.executeQuery(fileNameTextField.getText().trim(),
                queryTextArea.getText().trim());
        table.setModel(defaultTableModel);
        repaint();
    }

    private JScrollPane createEmptyTable() {
        table = new JTable();
        table.setName("Table");
        JScrollPane jScrollPane = new JScrollPane(table);
        jScrollPane.setLocation(10, 205);
        jScrollPane.setSize(665, 650);
        return jScrollPane;
    }

    private void openDB(String db) {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        List<String> tables = databaseConnection.getDBTable(db);
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addAll(tables);
        tablesComboBox.setModel(model);
        queryTextArea.setText("");
        if (!tables.isEmpty()) {
            tablesComboBox.setSelectedIndex(0);
            String table = String.valueOf(tablesComboBox.getSelectedItem());
            queryTextArea.setText("SELECT * FROM " + table + ";");
        }

    }

    private void generateSelectQuery() {
        String table = String.valueOf(tablesComboBox.getSelectedItem());
        queryTextArea.setText("SELECT * FROM " + table + ";");
    }

}
