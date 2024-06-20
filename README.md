Car Sales Management System

這個項目是一個基於 Java 並使用 Swing 的 GUI 的汽車銷售管理系統。該系統允許用戶與汽車銷售數據庫進行交互，執行各種預定義和自定義的 SQL 查詢，並查看結果

目錄
1.前置需求
2.安裝
3.使用
4.項目結構
5.數據庫結構
6.示例 SQL 查詢


1.前置需求
在開始使用本項目之前，請確保你已經安裝了以下軟體：
Java Development Kit (JDK) 8 或更高版本
MariaDB 或 MySQL 服務器

2.安裝
請按照以下步驟設置並運行該項目：

1. 克隆倉庫
使用以下命令克隆項目到你的本地機器：
git clone https://github.com/yourusername/car-sales-management.git
cd car-sales-management

3. 設置數據庫
運行 'CreateTables'類以創建數據庫和表，並插入一些示例數據：
java -cp bin CreateTables

3. 配置數據庫連接
確保在 DatabaseConnection.java 文件中設置正確的數據庫連接詳情：
private static final String URL = "jdbc:mariadb://0.tcp.jp.ngrok.io:11051/411177012";
private static final String USER = "411177012";
private static final String PASSWORD = "411177012";

5. 編譯項目
你可以使用喜歡的 IDE 編譯項目，或使用命令行進行編譯：
javac -d bin src/**/*.java

5. 運行應用
你可以通過 IDE 或命令行運行應用：
java -cp bin CarSalesGUI

3.使用
執行預定義查詢
使用 GUI 上的按鈕運行預定義查詢，並在文本區域中顯示結果。

自定義 SQL 查詢
在文本框中輸入自定義 SQL 查詢，然後點擊“查詢”按鈕以執行它們。

查看所有列信息
點擊“全欄位資訊”按鈕，以獲取並顯示數據庫表中的所有列信息。

顯示列大小
點擊“各欄位大小”按鈕，以顯示每個表中的記錄數量。

4.項目結構
以下是項目目錄的簡要概述：

src/
    ├── CarSalesGUI.java       // 主 GUI 類，包含查詢執行方法
    ├── CreateTables.java      // 創建數據庫結構並插入示例數據
    ├── DatabaseConnection.java // 管理數據庫連接
    └── Queries.java           // 包含 SQL 查詢方法
5.數據庫結構
數據庫架構包括以下表：
客戶 (Customer)
經銷商 (Dealer)
品牌 (Brand)
配置 (Configuration)
供應商 (Supplier)
工廠 (Factory)
車型 (CarModel)
車輛 (Vehicle)

6.示例 SQL 查詢
以下是一些示例 SQL 查詢，展示如何從數據庫中檢索特定信息

查詢 1：查找 2023 年來自 'Getrag' 供應商在 'Getrag Plant 1' 工廠的車輛和客戶

SELECT v.VIN, c.客戶名稱
FROM 車輛 v
JOIN 配置 cfg ON v.配置ID = cfg.配置ID
JOIN 供應商 s ON cfg.供應商ID = s.供應商ID
JOIN 工廠 f ON s.供應商ID = f.供應商ID
JOIN 客戶 c ON v.客戶ID = c.客戶ID
WHERE s.供應商名稱 = 'Getrag'
AND cfg.生產日期 BETWEEN '2023-01-01' AND '2023-12-31'
AND f.工廠名稱 = 'Getrag Plant 1';

查詢 2：查找過去一年銷售額最高的經銷商。


SELECT d.經銷商名稱, SUM(v.售價) as 總銷售額
FROM 車輛 v
JOIN 品牌 b ON v.品牌ID = b.品牌ID
JOIN 經銷商 d ON b.經銷商ID = d.經銷商ID
WHERE v.銷售日期 >= DATE_SUB(CURDATE(), INTERVAL 1 YEAR)
GROUP BY d.經銷商ID
ORDER BY 總銷售額 DESC LIMIT 1;


