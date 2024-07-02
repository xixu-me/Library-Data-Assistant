package server.tools;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ExportToCSV {
    public static void to(String outputFile, String query) {
        try (Connection connection = DBConnection.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                FileWriter fileWriter = new FileWriter(outputFile)) {
            writeColumnNames(resultSet, fileWriter);
            writeDataRows(resultSet, fileWriter);
            System.out.println("Data exported successfully to " + outputFile);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeColumnNames(ResultSet resultSet, FileWriter fileWriter) throws SQLException, IOException {
        for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
            fileWriter.append(resultSet.getMetaData().getColumnName(i));
            if (i < resultSet.getMetaData().getColumnCount())
                fileWriter.append(",");
        }
        fileWriter.append("\n");
    }

    private static void writeDataRows(ResultSet resultSet, FileWriter fileWriter) throws SQLException, IOException {
        while (resultSet.next()) {
            for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                fileWriter.append(resultSet.getString(i));
                if (i < resultSet.getMetaData().getColumnCount())
                    fileWriter.append(",");
            }
            fileWriter.append("\n");
        }
    }
}