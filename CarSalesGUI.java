import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class CarSalesGUI {

    private JFrame frame;
    private JTextField queryField;
    private JTextArea resultArea;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    CarSalesGUI window = new CarSalesGUI();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public CarSalesGUI() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 700, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null); // Use absolute layout

        JLabel lblEnterQuery = new JLabel("Enter Query:");
        lblEnterQuery.setBounds(10, 10, 80, 25); // Set position and size
        frame.getContentPane().add(lblEnterQuery);

        queryField = new JTextField();
        queryField.setBounds(100, 10, 400, 25); // Set position and size
        frame.getContentPane().add(queryField);

        JButton btnExecute = new JButton("查詢");
        btnExecute.setBounds(510, 10, 80, 25); // Set position and size
        btnExecute.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                runQuery(queryField.getText());
            }
        });
        frame.getContentPane().add(btnExecute);

        // Adding buttons for predefined queries
        JButton btnQuery1 = new JButton("Query 1");
        btnQuery1.setBounds(10, 50, 100, 25); // Set position and size
        btnQuery1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                runQuery1();
            }
        });
        frame.getContentPane().add(btnQuery1);

        JButton btnQuery2 = new JButton("Query 2");
        btnQuery2.setBounds(120, 50, 100, 25); // Set position and size
        btnQuery2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                runQuery2();
            }
        });
        frame.getContentPane().add(btnQuery2);

        JButton btnQuery3 = new JButton("Query 3");
        btnQuery3.setBounds(230, 50, 100, 25); // Set position and size
        btnQuery3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                runQuery3();
            }
        });
        frame.getContentPane().add(btnQuery3);

        JButton btnQuery4 = new JButton("Query 4");
        btnQuery4.setBounds(340, 50, 100, 25); // Set position and size
        btnQuery4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                runQuery4();
            }
        });
        frame.getContentPane().add(btnQuery4);

        JButton btnQuery5 = new JButton("Query 5");
        btnQuery5.setBounds(450, 50, 100, 25); // Set position and size
        btnQuery5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                runQuery5();
            }
        });
        frame.getContentPane().add(btnQuery5);

        resultArea = new JTextArea();
        resultArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBounds(10, 120, 660, 330);
        frame.getContentPane().add(scrollPane);

        JButton manualQueryButton = new JButton("全欄位資訊");
        manualQueryButton.setBounds(572, 51, 98, 23);
        frame.getContentPane().add(manualQueryButton);
        manualQueryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                runManualQuery();
            }
        });

        JButton columnSizeButton = new JButton("各欄位大小");
        columnSizeButton.setBounds(572, 87, 98, 23);
        columnSizeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showColumnSizes();
            }
        });
        frame.getContentPane().add(columnSizeButton);
    }

    private void runQuery1() {
        try (Connection connection = DatabaseConnection.getConnection(); Statement stmt = connection.createStatement()) {
            String query = "SELECT v.VIN, c.客戶名稱 "
                    + "FROM 車輛 v "
                    + "JOIN 配置 cfg ON v.配置ID = cfg.配置ID "
                    + "JOIN 供應商 s ON cfg.供應商ID = s.供應商ID "
                    + "JOIN 工廠 f ON s.供應商ID = f.供應商ID "
                    + "JOIN 客戶 c ON v.客戶ID = c.客戶ID "
                    + "WHERE s.供應商名稱 = 'Getrag' "
                    + "AND cfg.生產日期 BETWEEN '2023-01-01' AND '2023-12-31' "
                    + "AND f.工廠名稱 = 'Getrag Plant 1'";
            ResultSet rs = stmt.executeQuery(query);
            boolean hasResults = false;
            while (rs.next()) {
                hasResults = true;
                System.out.println("VIN: " + rs.getString("VIN") + ", 客戶: " + rs.getString("客戶名稱"));
            }
            if (!hasResults) {
                System.out.println("沒有找到符合條件的車輛和客戶。");
            }
            runQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void runQuery2() {
        try (Connection connection = DatabaseConnection.getConnection(); Statement stmt = connection.createStatement()) {
            String query = "SELECT d.經銷商名稱, SUM(v.售價) as 總銷售額 "
                    + "FROM 車輛 v "
                    + "JOIN 品牌 b ON v.品牌ID = b.品牌ID "
                    + "JOIN 經銷商 d ON b.經銷商ID = d.經銷商ID "
                    + "WHERE v.銷售日期 >= DATE_SUB(CURDATE(), INTERVAL 1 YEAR) "
                    + "GROUP BY d.經銷商ID "
                    + "ORDER BY 總銷售額 DESC LIMIT 1";

            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                System.out.println("銷售額最高的經銷商: " + rs.getString("經銷商名稱") + ", 總銷售額: " + rs.getDouble("總銷售額"));
            } else {
                JOptionPane.showMessageDialog(frame, "沒有找到符合條件的經銷商。", "提示", JOptionPane.INFORMATION_MESSAGE);
                System.out.println("沒有找到符合條件的經銷商。");
            }
            runQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void runQuery3() {
        try (Connection connection = DatabaseConnection.getConnection(); Statement stmt = connection.createStatement()) {
            String query = "SELECT b.品牌名稱, COUNT(*) as 銷售量 "
                    + "FROM 車輛 v "
                    + "JOIN 品牌 b ON v.品牌ID = b.品牌ID "
                    + "WHERE v.銷售日期 >= DATE_SUB(CURDATE(), INTERVAL 1 YEAR) "
                    + "GROUP BY b.品牌ID "
                    + "ORDER BY 銷售量 DESC LIMIT 2";

            ResultSet rs = stmt.executeQuery(query);
            boolean hasResults = false;
            while (rs.next()) {
                hasResults = true;
                System.out.println("品牌: " + rs.getString("品牌名稱") + ", 銷售量: " + rs.getInt("銷售量"));
            }
            if (!hasResults) {
                System.out.println("沒有找到符合條件的品牌。");
            }
            runQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void runQuery4() {
        try (Connection connection = DatabaseConnection.getConnection(); Statement stmt = connection.createStatement()) {
            String query = "SELECT MONTH(v.銷售日期) as 月份, COUNT(*) as 銷售量 "
                    + "FROM 車輛 v "
                    + "JOIN 車型 m ON v.車型ID = m.車型ID "
                    + "WHERE m.車輛型式 = 'SUV' "
                    + "GROUP BY 月份 "
                    + "ORDER BY 銷售量 DESC LIMIT 1";

            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                System.out.println("SUV 銷量最好的月份: " + rs.getInt("月份") + "月, 銷售量: " + rs.getInt("銷售量"));
            } else {
                System.out.println("沒有找到符合條件的月份。");
            }
            runQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void runQuery5() {
        try (Connection connection = DatabaseConnection.getConnection(); Statement stmt = connection.createStatement()) {
            String query = "SELECT d.經銷商名稱, AVG(DATEDIFF(v.銷售日期, d.庫存日期)) as 平均庫存天數 "
                    + "FROM 車輛 v "
                    + "JOIN 品牌 b ON v.品牌ID = b.品牌ID "
                    + "JOIN 經銷商 d ON b.經銷商ID = d.經銷商ID "
                    + "GROUP BY d.經銷商ID "
                    + "ORDER BY 平均庫存天數 DESC";

            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                System.out.println("經銷商: " + rs.getString("經銷商名稱") + ", 平均庫存天數: " + rs.getInt("平均庫存天數"));
            } else {
                System.out.println("沒有找到符合條件的經銷商。");
            }
            runQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void runManualQuery() {
        String[] queries = {
            "SELECT * FROM 客戶",
            "SELECT * FROM 經銷商",
            "SELECT * FROM 品牌",
            "SELECT * FROM 配置",
            "SELECT * FROM 供應商",
            "SELECT * FROM 工廠",
            "SELECT * FROM 車型",
            "SELECT * FROM 車輛"
        };

        StringBuilder result = new StringBuilder();

        try (Connection connection = DatabaseConnection.getConnection(); Statement stmt = connection.createStatement()) {
            for (String query : queries) {
                ResultSet rs = stmt.executeQuery(query);
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();

                // Append column names
                for (int i = 1; i <= columnCount; i++) {
                    result.append(rsmd.getColumnName(i)).append("\t");
                }
                result.append("\n");

                // Append rows
                while (rs.next()) {
                    for (int i = 1; i <= columnCount; i++) {
                        result.append(rs.getString(i)).append("\t");
                    }
                    result.append("\n");
                }
                result.append("\n");

                rs.close();
            }

            resultArea.setText(result.toString());

        } catch (SQLException e) {
            e.printStackTrace();
            resultArea.setText("Error executing query: " + e.getMessage());
        }
    }

    private void showColumnSizes() {
        try (Connection connection = DatabaseConnection.getConnection(); Statement stmt = connection.createStatement()) {
            String query = "SELECT '客戶' AS 表名, COUNT(*) AS 資料量 FROM 客戶 "
                    + "UNION ALL SELECT '經銷商', COUNT(*) FROM 經銷商 "
                    + "UNION ALL SELECT '品牌', COUNT(*) FROM 品牌 "
                    + "UNION ALL SELECT '配置', COUNT(*) FROM 配置 "
                    + "UNION ALL SELECT '供應商', COUNT(*) FROM 供應商 "
                    + "UNION ALL SELECT '工廠', COUNT(*) FROM 工廠 "
                    + "UNION ALL SELECT '車型', COUNT(*) FROM 車型 "
                    + "UNION ALL SELECT '車輛', COUNT(*) FROM 車輛";

            ResultSet rs = stmt.executeQuery(query);
            // 增加其他表的大小查詢
            while (rs.next()) {
                System.out.println(rs.getString("表名") + " 表的資料量: " + rs.getInt("資料量"));
            }
            runQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void runQuery(String query) {
        try (Connection connection = DatabaseConnection.getConnection(); Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            StringBuilder result = new StringBuilder();
            for (int i = 1; i <= columnCount; i++) {
                result.append(rsmd.getColumnName(i)).append("\t");
            }
            result.append("\n");

            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    result.append(rs.getString(i)).append("\t");
                }
                result.append("\n");
            }
            resultArea.setText(result.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            resultArea.setText("Error executing query: " + e.getMessage());
        }
    }
}

class DatabaseConnect {
    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mariadb://0.tcp.jp.ngrok.io:11051/411177012", "411177012", "411177012");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
