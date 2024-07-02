# 图书信息管理系统设计文档

## 1 需求分析

设计一个基于Socket的客户端-服务器（C/S）程序图书信息管理系统。该系统将实现“用户登录”、“图书信息展示”、“图书信息爬取”、“图书信息查询”、“图书信息删除”、“图书信息修改”、“图书信息导出”以及“系统退出”等功能。以下是各项功能的详细需求分析。

### 1.1 服务端监听

- 启动服务端程序，服务端程序监听端口，等待客户端连接，提示“Server listening on port 8888”。
- 当连接到客户端后，提示“Client connected: 127.0.0.1”。
- 接收客户端发送的登录请求后，提示“Receive the client's request data: {"code":0,"data":"{\"userName\":\"admin\",\"password\":\"admin\"}"}”，并验证用户信息。
- 当客户端退出系统，与客户端的连接断开后，提示“Client exited!”，继续监听端口。

### 1.2 用户登录

- 用户启动客户端程序，首先进入登录界面，分别提示用户输入用户名“Please enter your username:”和密码“Please enter your password:”。
- 系统通过socket连接发送登录请求到服务器端。
- 服务器验证用户信息，返回验证结果。
- 如果验证成功，用户进入主菜单；如果失败，提示错误信息“Login failed: Username does not exist!”或“Login failed: Wrong password!”。
- 用户最多有3次登录机会，超过3次登录失败，提示“Login failed. Welcome to visit next time!”并退出程序。

### 1.3 主菜单

- 登录成功后，显示主菜单，如下所示：

    ```plaintext
    ======== Library Data Assistant ========
    Current user: admin
    1. Display library data;
    2. Crawling library data;
    3. Querying library data;
    4. Deleting library data;
    5. Modifying library data;
    6. Exporting library data;
    7. Exiting Assistant.
    Please enter options (1-8):
    ```

- 用户通过输入选项前的数字选择相应的功能。

### 1.4 各项功能

#### 1.4.1 图书信息显示

- 当用户输入1时，系统进入图书信息显示功能。
- 系统通过socket连接向服务器发送请求。
- 服务器查询数据库中的图书信息，并将前10条记录返回给客户端。
- 客户端接收到数据后，显示在用户界面上。
- 客户端显示完10条记录后，提示“Displaying ended!”，返回主菜单。

#### 1.4.2 图书信息爬取

- 当用户输入2时，系统进入图书信息爬取功能。
- 系统通过socket连接向服务器发送爬取请求。
- 服务器端启动爬虫程序，提示“Crawling started...”，从当当网站爬取100本图书的信息。
- 爬取完成后，将图书信息存储到MySQL数据库中，并通知客户端爬取成功，提示“Crawling ended!”，返回主菜单。

#### 1.4.3 图书信息查询

- 当用户输入3时，系统进入图书信息查询功能。
- 系统显示查询选项，如下所示：

    ```plaintext
    ====== Querying library data =======
    1. Querying by title;
    2. Querying by author;
    3. Querying by publisher;
    4. Querying by original price;
    5. Return to previous menu.
    Please enter options (1-5):
    ```

- 用户选择查询方式，提示“Please enter the xxx:”，用户输入查询条件。
- 系统通过socket连接向服务器发送查询请求。
- 服务器根据请求查询数据库，并将查询结果返回给客户端。
- 客户端接收到数据后，显示查询结果。
- 查询结束后，提示“Querying ended!”，返回主菜单。

#### 1.4.4 图书信息删除

- 当用户输入4时，系统进入图书信息删除功能。
- 系统提示用户输入要删除的图书标题“Please enter the title:”。
- 用户输入标题后，系统通过socket连接向服务器发送删除请求。
- 服务器执行删除操作，并返回操作结果给客户端。
- 客户端显示删除操作的结果，如果删除成功，提示“Delete successfully!”，否则提示“No record found to delete!”，返回主菜单。

#### 1.4.5 图书信息修改

- 当用户输入5时，系统进入图书信息修改功能。
- 系统提示用户输入要修改的图书标题“Please enter the title:”。
- 用户输入后，系统显示修改选项，如下所示：

    ```plaintext
    ====== Modifying library data =======
    1. Modifying author;
    2. Modifying publisher;
    3. Modifying price;
    4. Return to previous menu.
    Please enter options (1-4):
    ```

- 用户选择修改方式，提示“Please enter the modified xxx:”，用户输入修改内容。
- 系统通过socket连接向服务器发送修改请求。
- 服务器执行修改操作，并返回操作结果给客户端，如果成功，提示“Modify successfully!”，否则提示“No record found to modify!”，返回主菜单。

#### 1.4.6 图书信息导出

- 当用户输入6时，系统进入图书信息导出功能。
- 系统显示导出选项，如下所示：

    ```plaintext
    ====== Exporting library data =======
    1. Exporting to CSV file;
    2. Exporting to XLS file;
    3. Return to previous menu.
    Please enter options (1-3):
    ```

- 用户选择导出方式，系统通过socket连接向服务器发送导出请求。

- 服务器将数据库中的图书信息导出到CSV或XLS文件中，并返回操作结果给客户端，如果成功，提示“Data exported successfully to output.csv!”或“Data exported successfully to output.xls!”，返回主菜单。

### 1.5 系统退出

- 当用户输入7时，客户端程序退出，提示“You have exited the assistant. Welcome to visit next time!”

## 2 系统设计

### 2.1 用户用例图

### 2.2 E-R 图

### 2.3 UML 类图（Class Diagram）

### 2.4 UML 时序图（Sequence Diagram）

#### 2.4.1 用户登录

#### 2.4.2 **** 模块

### 2.5 UML 活动图（Activity Diagram）

#### 2.5.1 用户登录

#### 2.5.2 **** 模块

## 3 系统实现

### 3.1 项目结构

该系统的项目结构如图 所示。

![alt text](image-15.png)
图 项目结构

该系统主要由客户端（`client`）和服务器端（`server`）组成，涉及到数据库操作、多线程处理、数据爬取和导出等功能。下面是每个包和类的作用说明：

客户端 (`client`)

- `Client.java`: 客户端主类，负责与用户交互，包括查询和显示图书信息等功能。

服务器端 (`server`)

- `dao` (Data Access Object)
  - `UserDAO.java`: 负责用户数据的访问和操作，如查询用户信息等。
- `thread`
  - `HandleThread.java`: 用于处理服务器端的多线程任务，例如并发处理客户端请求。
- `tools`
  - `crawling`
    - `Book.java`: 定义了图书信息的数据结构，包括标题、作者、出版社等。
    - `CrawlerTools.java`: 包含爬虫工具的实现，用于从网络上抓取图书信息。
    - `Driver.java`: 是爬虫的启动类，负责初始化和执行爬虫任务。
    - `NewsThread.java`: 用于处理特定的爬虫任务，如新闻信息的爬取。
  - `DBConnection.java`: 负责数据库连接的管理，提供数据库连接功能。
  - `Encoding.java`: 用于处理数据的编码转换。
  - `ExportToCSV.java`: 提供将数据导出为CSV格式文件的功能。
  - `ExportToXLS.java`: 提供将数据导出为XLS格式文件的功能。
- `vo` (Value Object)
  - `User.java`: 定义了用户信息的数据结构，如用户名、密码等。
- `Server.java`: 服务器端主类，负责监听客户端连接，处理客户端请求等。

### 3.2 Client.java

- `public static void main(String[] args)`: 程序入口，设置连接，登录，处理用户选择。
- `private static void setupConnection()`: 设置与服务器的连接。
- `private static void handleUserChoices()`: 处理用户的菜单选择。
- `public static void close()`: 关闭连接和其他资源。
- `public static boolean login()`: 处理用户登录。
- `private static User getUserCredentials()`: 获取用户凭证。
- `public static String send(String data)`: 发送数据到服务器并接收响应。
- `public static int menu()`: 显示用户菜单并获取选择。
- `private static void openConnection()`: 打开数据库连接。
- `private static void closeResources()`: 关闭数据库资源。
- `public static void display()`: 显示图书数据。
- `public static void query()`: 查询图书数据。
- `public static void delete()`: 删除图书数据。
- `public static void update()`: 更新图书数据。
- `private static void updateField(String field, String title)`: 更新图书的特定字段。
- `private static void updatePrice(String title)`: 更新图书价格。
- `private static void export()`: 导出图书数据。

### 3.3 UserDAO.java

- `public static User get(String userName)`: 根据用户名获取用户。
- `public static ArrayList<User> query(User userCondition)`: 查询符合条件的用户列表。
- `private static User createUserFromResultSet(ResultSet rs)`: 从结果集创建用户对象。

### 3.4 HandleThread.java

- `public void run()`: 处理客户端请求的主要方法。
- `private String handleRequest(int code, String data)`: 根据请求代码处理请求。
- `private String loginHandle(String str)`: 处理登录请求。
- `private void closeResources()`: 关闭连接资源。

### 3.5 Book.java

- `public Book(String title, String author, String publisher, double oldprice, double newprice, String href)`: 构造函数，定义图书属性。
- `getters` 和 `setters`: 获取和设置图书属性。
- `public String toString()`: 返回图书信息的字符串表示。

### 3.6 CrawlerTools.java

- `public static String get(String urlStr, String charset)`: 从URL获取内容。
- `public static String encodingUrl(String url)`: 对URL进行编码。

### 3.7 Driver.java

- `public static void crawl()`: 启动爬虫抓取数据。

### 3.8 NewsThread.java

- `public NewsThread(String url)`: 构造函数，初始化URL。
- `public void run()`: 执行爬虫任务，解析数据并存入数据库。

### 3.9 DBConnection.java

- `public static Connection getConnection()`: 获取数据库连接。
- `public static void close(AutoCloseable... resources)`: 关闭数据库资源。

### 3.10 Encoding.java

- `public static String md5(String password)`: 对字符串进行MD5编码。
- `public static String base64FromFile(String filePath)`: 从文件获取Base64编码字符串。
- `public static void base64ToFile(String base64Str, String filePath)`: 将Base64编码字符串写入文件。

### 3.11 ExportToCSV.java

- `public static void to(String outputFile, String query)`: 将查询结果导出为CSV文件。
- `private static void writeColumnNames(ResultSet resultSet, FileWriter fileWriter)`: 写入列名。
- `private static void writeDataRows(ResultSet resultSet, FileWriter fileWriter)`: 写入数据行。

### 3.12 ExportToXLS.java

- `public static void to(String query, String outputPath)`: 将查询结果导出为XLS文件。
- `private static void createColumnHeaders(WritableSheet sheet, String[] columnHeaders)`: 创建列标题。
- `private static void writeDataRows(WritableSheet sheet, ResultSet rs)`: 写入数据行。

### 3.13 User.java

- `public User(String userName, String password)`: 构造函数，定义用户属性。
- `getters` 和 `setters`: 获取和设置用户属性。

### 3.14 Server.java

- `public static void main(String[] args)`: 服务器程序入口，监听端口，接受客户端连接。
