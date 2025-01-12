# Library Data Assistant

A Java-based client-server application for managing library book data with web crawling capabilities.

## Features

- User authentication system
- Book data management:
  - Display library data
  - Web crawling of book information from Dangdang.com
  - Query books by title, author, publisher, or price
  - Delete book records
  - Update book information
  - Export data to CSV or XLS formats
- Multi-threaded web crawler
- Client-server architecture using Socket communication
- JSON-based data exchange
- MySQL database integration

## Technical Stack

- Java
- MySQL
- Socket Programming
- GSON for JSON handling
- JSoup for web crawling
- JXL for Excel file handling

## Project Structure

```plaintext
src/
├── client/
│   └── Client.java           # Client-side application
├── server/
│   ├── Server.java           # Server application
│   ├── dao/
│   │   └── UserDAO.java      # Data Access Object for users
│   ├── thread/
│   │   └── HandleThread.java # Server thread handler
│   ├── tools/
│   │   ├── crawling/         # Web crawling components
│   │   ├── DBConnection.java # Database connection utility
│   │   ├── Encoding.java     # Encoding utilities
│   │   ├── ExportToCSV.java  # CSV export functionality
│   │   └── ExportToXLS.java  # Excel export functionality
│   └── vo/
│       └── User.java         # User value object

```

## Setup

1. Install MySQL and create a database named `lda`
2. Configure database connection in `src/server/tools/DBConnection.java`:

   ```java
   private final static String url = "jdbc:mysql://localhost:3306/lda?allowPublicKeyRetrieval=true&useSSL=false";
   private final static String user = "root";
   private final static String password = "your_password";
   ```

3. Compile the project
4. Start the server: `java server.Server`
5. Start the client: `java client.Client`

## Usage

1. Login with your credentials
2. Use the menu to:
   - View library data
   - Crawl new book data
   - Query specific books
   - Delete books
   - Modify book information
   - Export data to CSV/XLS

## License

Copyright &copy; [Xi Xu](https://xi-xu.me). All rights reserved.

Licensed under the [GPL-3.0](LICENSE) license.
