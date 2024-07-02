package server.tools;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ExportToCSV {
    public static void to() {
        String outputFile = "Library Data.csv";
        try (Connection connection = DBConnection.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM book");
                FileWriter fileWriter = new FileWriter(outputFile)) {
            for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                fileWriter.append(resultSet.getMetaData().getColumnName(i));
                if (i < resultSet.getMetaData().getColumnCount())
                    fileWriter.append(",");
            }
            fileWriter.append("\n");
            while (resultSet.next()) {
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    fileWriter.append(resultSet.getString(i));
                    if (i < resultSet.getMetaData().getColumnCount())
                        fileWriter.append(",");
                }
                fileWriter.append("\n");
            }
            System.out.println("Data exported successfully!");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}
