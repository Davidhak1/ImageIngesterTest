package resources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Utils {

    static private File file;
    static private FileInputStream fis;

    private static Logger log = LogManager.getLogger(base.class.getName());

    public String uuidGenerator() {
        log.info("inside method");

        String UUIDCHARS = "1234567890abcdefghijklmnopqrstuvwxyz1234567890";
        StringBuilder uuid = new StringBuilder();
        Random rnd = new Random();

        log.debug("Generating new uuid");

        while (uuid.length() < 35) { // length of the random string.
            int index = (int) (rnd.nextFloat() * UUIDCHARS.length());
            uuid.append(UUIDCHARS.charAt(index));
        }

        String uuidStr = uuid.toString();
        log.info("the new generate uuid = " + uuidStr);

        return uuidStr;
    }

    public String audiUuidExtender(String uuid, int number){
        String extenderChars = "1234567890abcdefghijklmnopqrstuvwxyz";
        StringBuilder extend = new StringBuilder();
        Random rnd = new Random();

        log.debug("Generating new uuid");

        while (extend.length() < number) { // length of the random string.
            int index = (int) (rnd.nextFloat() * extenderChars.length());
            extend.append(extenderChars.charAt(index));
        }

        return (uuid + extend.toString());
    }

    public String UUIDStoreInExcel(String uuid) throws IOException {
        log.info("inside method");

        file = new File("src/main/java/resources/generateduuid.xlsx");
        fis = new FileInputStream(file);

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

    public int getVehicleEligibleForRemovalAndSendToStore(int length) throws IOException {
        Queries q= new Queries();



        List<Vehicle> vehicles = q.getVehicleBySpecificUuidLength(length);
        if (vehicles!=null) {
            for (Vehicle v : vehicles) {
                int image_num = q.getNumberOfImagesForUuid(v.getUuid());
                storeVehiclesInExcelBeforeRemoval(v.getUuid(), v.getStatus(), v.getVin(), v.getOem(), (v.getNext_scheduled_on().toString()), image_num);
            }
        }
        else {
            return 0;
        }
        return 1;
    }

    private void storeVehiclesInExcelBeforeRemoval(String uuid, String status, String vin, String oem,String next_scheduled, int number) throws IOException {
        log.info("inside method");

         file = new File("src/main/java/resources/generateduuid.xlsx");
         fis = new FileInputStream(file);

        int rowNum = 0;

        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        XSSFSheet sheet = workbook.getSheet("deleted");

        for(Row row :sheet) {
            rowNum++;
        }

        Row row = sheet.createRow(++rowNum);
        log.debug("Storing vehicle info before removel in excel at row #"+rowNum);
        row.createCell(0).setCellValue(uuid);
        row.createCell(1).setCellValue(status);
        row.createCell(2).setCellValue(vin);
        row.createCell(3).setCellValue(oem);
        row.createCell(4).setCellValue(next_scheduled);
        row.createCell(5).setCellValue(number);
        row.createCell(6).setCellValue(1);
        row.createCell(7).setCellValue(String.valueOf(LocalDateTime.now()));


        log.debug("Writing the changes in the actual excel file");
        try {
            FileOutputStream fos = null;
            fos = new FileOutputStream(file);
            workbook.write(fos);
            fos.close();
            workbook.close();

        }catch(Exception e){
            e.printStackTrace();
        }
    }



}