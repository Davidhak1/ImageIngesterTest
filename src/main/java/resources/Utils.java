package resources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Random;

public class Utils {
    public static Logger log = LogManager.getLogger(base.class.getName());

    public String uuidGenerator() {
        log.info("inside method");

        String UUIDCHARS = "1234567890abcdefghijklmnopqrstuvwxyz1234567890";
        StringBuilder uuid = new StringBuilder();
        Random rnd = new Random();

        log.debug("Generating new uuid");

        while (uuid.length() < 32) { // length of the random string.
            int index = (int) (rnd.nextFloat() * UUIDCHARS.length());
            uuid.append(UUIDCHARS.charAt(index));
        }

        String uuidStr = uuid.toString();
        log.info("the new generate uuid = " + uuidStr);

        return uuidStr;
    }

    public String UUIDStoreInExcel(String uuid) throws IOException
    {
        log.info("inside method");

        File file = new File("src/main/java/resources/generateduuid.xlsx");
        FileInputStream fis = new FileInputStream(file);

        int rowNum = 0;

        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        XSSFSheet sheet = workbook.getSheet("uuid");

        for(Row row :sheet) {
            rowNum++;
        }

        Row row = sheet.createRow(++rowNum);
        log.debug("Storing the new uuid in excel workbook at row #"+rowNum);
        row.createCell(0).setCellValue(uuid);

        log.debug("Storing the times in excel workbook at row #"+rowNum);
        row.createCell(1).setCellValue(String.valueOf(LocalDateTime.now()));


        FileOutputStream fos = new FileOutputStream(file);
        log.debug("Writing the changes in the actual excel file");
        workbook.write(fos);
        fos.close();

        workbook.close();

        return uuid;

    }

//    public static void main(String[] args) {
//        Utils rs = new Utils();
//        System.out.println(rs.uuidGenerator());
//
//    }
}