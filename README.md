# Coindesk 服務演示

這是一個 Spring Boot 應用程式，展示了如何整合 Coindesk API，轉換其資料，並提供幣別管理的 CRUD 操作。它使用內存中的 H2 資料庫來儲存幣別資訊，包括本地化的中文名稱。

## 功能

*   **Coindesk API 整合**：
    *   從 Coindesk 獲取當前的比特幣價格資訊。
    *   將原始 API 回應轉換為自訂格式。
    *   使用資料庫中儲存的中文幣別名稱豐富回應內容。
    *   將時間戳記格式化為 "yyyy/MM/dd HH:mm:ss"。
*   **幣別管理 (CRUD)**：
    *   新增、讀取、更新和刪除幣別。
    *   儲存幣別代碼（例如：USD, GBP）和中文名稱（例如：美元, 英鎊）。
*   **技術亮點**：
    *   **分層架構**：Controller -> Service -> Repository。
    *   **DTO/Entity 映射**：使用 MapStruct 在 DTO、業務物件 (BO) 和實體之間進行高效的 Bean 映射。
    *   **資料庫**：使用 Spring Data JPA 的 H2 內存資料庫。
    *   **驗證**：基本的輸入驗證。
    *   **統一回應**：所有端點都使用標準化的 `ApiResponse<T>` 包裝。

## 技術棧

*   **Java**: 1.8
*   **Framework**: Spring Boot 2.7.18
*   **Database**: H2 Database (內存資料庫)
*   **ORM**: Spring Data JPA (Hibernate)
*   **Tools**:
    *   **Lombok**: 減少樣板程式碼 (Getters, Setters 等)。
    *   **MapStruct**: 型別安全的 Bean 映射。
*   **Build Tool**: Maven

## 專案結構

```
src/main/java/com/cathay/coindesk
├── config          # 配置類 (例如：RestTemplate)
├── controller      # REST Controllers (API 端點)
├── exception       # 全域例外處理
├── mapper          # MapStruct Mappers
├── model           # 資料模型
│   ├── bo          # 業務物件 (Business Objects)
│   ├── dto         # 資料傳輸物件 (Data Transfer Objects)
│   └── entity      # 資料庫實體 (Database Entities)
├── repository      # JPA Repositories
└── service         # 業務邏輯
```

## API 文件

### 1. Coindesk API
前綴：`/api/coindesk`

| 方法 | 端點 | 描述 |
| :--- | :--- | :--- |
| `GET` | `/original` | 呼叫 Coindesk API 並回傳原始回應。 |
| `GET` | `/transform` | 呼叫 Coindesk API，轉換資料並加入中文名稱。 |

### 2. 幣別管理 API
前綴：`/api/currency`

| 方法 | 端點 | 描述 | Payload (JSON) |
| :--- | :--- | :--- | :--- |
| `GET` | `/` | 獲取所有幣別。 | N/A |
| `GET` | `/{code}` | 根據代碼獲取特定幣別（例如：USD）。 | N/A |
| `POST` | `/` | 新增幣別。 | `{"code": "...", "chineseName": "..."}` |
| `PUT` | `/{code}` | 更新現有幣別。 | `{"code": "...", "chineseName": "..."}` |
| `DELETE` | `/{code}` | 刪除幣別。 | N/A |

## 快速開始

### 先決條件

*   JDK 1.8+
*   Maven 3.x

### 安裝

1.  複製 (Clone) 儲存庫：
    ```bash
    git clone https://github.com/adcw987654/CathayDemo.git
    ```
2.  進入專案目錄：
    ```bash
    cd CathayDemo/coindesk-service
    ```

### 執行應用程式

使用 Maven 執行 Spring Boot 應用程式：

```bash
mvn spring-boot:run
```

應用程式將在預設的 `8080` 埠啟動。

### H2 控制台

您可以透過以下網址存取 H2 內存資料庫控制台：
*   **URL**: `http://localhost:8080/h2-console`
*   **JDBC URL**: `jdbc:h2:mem:coindeskdb`
*   **User Name**: `sa`
*   **Password**: `password`

## 資料庫初始化

應用程式在啟動時會自動檢查 `src/main/resources` 中的 `schema.sql` 和 `data.sql` 以初始化資料庫結構並載入初始資料。

**初始資料 (`data.sql`):**
```sql
INSERT INTO CURRENCY (CODE, CHINESE_NAME) VALUES ('USD', '美元');
INSERT INTO CURRENCY (CODE, CHINESE_NAME) VALUES ('GBP', '英鎊');
INSERT INTO CURRENCY (CODE, CHINESE_NAME) VALUES ('EUR', '歐元');
```
