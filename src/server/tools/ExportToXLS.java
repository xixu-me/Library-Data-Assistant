package server.tools;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class ExportToXLS {
    public static void to(String query, String outputPath) {
        String[] columnHeaders = { "title", "author", "publisher", "oldprice", "newprice", "href" };
        try (Connection connection = DBConnection.getConnection();
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(query)) {
            WritableWorkbook workbook = Workbook.createWorkbook(new File(outputPath));
            WritableSheet sheet = workbook.createSheet("Sheet1", 0);
            createColumnHeaders(sheet, columnHeaders);
            writeDataRows(sheet, rs);
            workbook.write();
            workbook.close();
            System.out.println("Data exported successfully to " + outputPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createColumnHeaders(WritableSheet sheet, String[] columnHeaders) throws Exception {
        for (int i = 0; i < columnHeaders.length; i++) {
            Label label = new Label(i, 0, columnHeaders[i]);
            sheet.addCell(label);
        }
    }

    private static void writeDataRows(WritableSheet sheet, ResultSet rs) throws Exception {
        int rowIndex = 1;
        while (rs.next()) {
            for (int columnIndex = 0; columnIndex <= 5; columnIndex++) {
                Label label = new Label(columnIndex, rowIndex, rs.getString(columnIndex + 1));
                sheet.addCell(label);
            }
            rowIndex++;
        }
    }
}
