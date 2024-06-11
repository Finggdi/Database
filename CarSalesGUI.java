import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Scanner;

public class CarSalesGUI {

    private JFrame frame;
    private JTextField queryTextField;
    private JTextArea resultArea;
    Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
    	
        CarSalesGUI gui = new CarSalesGUI();
        gui.createAndShowGUI();
    }

    private void createAndShowGUI() {
        frame = new JFrame("Car Sales Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);

        frame.setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        // 查詢範例問題的按鈕
        JButton queryButton1 = new JButton("Find Defective Transmissions");
        queryButton1.setBounds(50, 20, 300, 25);
        panel.add(queryButton1);

        JButton queryButton2 = new JButton("Find Top Dealer By Sales");
        queryButton2.setBounds(50, 50, 300, 25);
        panel.add(queryButton2);

        JButton queryButton3 = new JButton("Find Top Brands By Unit Sales");
        queryButton3.setBounds(50, 80, 300, 25);
        panel.add(queryButton3);

        JButton queryButton4 = new JButton("Find Best Month For SUVs");
        queryButton4.setBounds(50, 110, 300, 25);
        panel.add(queryButton4);

        JButton queryButton5 = new JButton("Find Dealers With Longest Inventory Time");
        queryButton5.setBounds(50, 140, 300, 25);
        panel.add(queryButton5);

        // 手動輸入查詢指令的按鈕
        queryTextField = new JTextField();
        queryTextField.setBounds(50, 170, 300, 25);
        panel.add(queryTextField);

        JButton manualQueryButton = new JButton("Run Query");
        manualQueryButton.setBounds(360, 170, 100, 25);
        panel.add(manualQueryButton);

        // 查詢各個欄位的按鈕
        JButton columnQueryButton = new JButton("Query Column Data");
        columnQueryButton.setBounds(50, 200, 300, 25);
        panel.add(columnQueryButton);

        // 列出各個欄位大小的按鈕
        JButton columnSizeButton = new JButton("Show Column Sizes");
        columnSizeButton.setBounds(50, 230, 300, 25);
        panel.add(columnSizeButton);

        // 顯示查詢結果的區域
        resultArea = new JTextArea();
        resultArea.setBounds(5, 260, 570, 100);
        panel.add(resultArea);

        // 添加按鈕的動作監聽器
        queryButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                runQuery1();
            }
        });

        queryButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                runQuery2();
            }
        });

        queryButton3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                runQuery3();
            }
        });

        queryButton4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                runQuery4();
            }
        });

        queryButton5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                runQuery5();
            }
        });

        manualQueryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                runManualQuery();
            }
        });

        columnQueryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                queryColumnData();
            }
        });

        columnSizeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showColumnSizes();
            }
        });
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
            boolean hasResults = false;
            while (rs.next()) {
                hasResults = true;
                System.out.println("經銷商: " + rs.getString("經銷商名稱") + ", 平均庫存天數: " + rs.getInt("平均庫存天數"));
            }
            if (!hasResults) {
                System.out.println("沒有找到符合條件的經銷商。");
            }
            runQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void runManualQuery() {
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

    private void queryColumnData() {
        // 實現查詢各個欄位的程式碼
        String query = sc.next(); // 替換成您想查詢的具體欄位資料
        runQuery(query);
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
        }
    }
}
