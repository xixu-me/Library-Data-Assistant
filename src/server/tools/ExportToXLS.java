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
    public static void to() {
        try (Connection connection = DBConnection.getConnection();
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("SELECT * FROM book");) {
            WritableWorkbook book = Workbook.createWorkbook(new File("图书信息.xls"));
            WritableSheet sheet = book.createSheet("sheet1", 0);
            Label label = new Label(0, 0, "title");
            sheet.addCell(label);
            label = new Label(1, 0, "author");
            sheet.addCell(label);
            label = new Label(2, 0, "publisher");
            sheet.addCell(label);
            label = new Label(3, 0, "oldprice");
            sheet.addCell(label);
            label = new Label(4, 0, "newprice");
            sheet.addCell(label);
            label = new Label(5, 0, "href");
            sheet.addCell(label);
            int i = 1;
            while (rs.next()) {
                for (int j = 0; j <= 5; j++) {
                    label = new Label(j, i, rs.getString(j + 1));
                    sheet.addCell(label);
                }
                i++;
            }
            book.write();
            book.close();
            System.out.println("Data exported successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
