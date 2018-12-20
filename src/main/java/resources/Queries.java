package resources;

import model.DownloadedImage;
import model.Vehicle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class Queries {


    private static Logger log = LogManager.getLogger(base.class.getName());

    private MysqlCon mysqlCon= new MysqlCon();

    public Vehicle getVehicleByUUID(String uuid) {
        Statement stmt = mysqlCon.getStatement();

        try {
            log.debug("Querying vehicle table, finding with 'uuid'....");
            ResultSet rs = stmt.executeQuery("select * from vehicle where uuid = '"+uuid+"';");

            while (rs.next()) {
                return new Vehicle(rs.getString(1), rs.getString(2), rs.getString(3), rs.getBoolean(4),
                        rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8),
                        (rs.getTimestamp(9) != null) ? rs.getTimestamp(9).toLocalDateTime() : null,
                        (rs.getTimestamp(10) != null) ? rs.getTimestamp(10).toLocalDateTime() : null,
                        (rs.getTimestamp(11) != null) ? rs.getTimestamp(11).toLocalDateTime() : null);

            }

        }catch (Exception e){
            System.out.println("------------------EXCEPTION IN THE QUERIES CLASS------------------");
            e.printStackTrace();
        }

        finally{
            mysqlCon.endCon();
        }
        System.out.println("No vehicle found with 'uuid' - "+uuid);
        return null;
    }

    public List<Vehicle> getVehicleByVINAndNotRemoved(String vin) {
        Statement stmt = mysqlCon.getStatement();
        List<Vehicle> vehicles = new ArrayList<Vehicle>();
        try {
            log.debug("Querying vehicle table, finding with 'vin' and Not removed....");
            ResultSet rs = stmt.executeQuery("select * from vehicle where vin = '"+vin+"' AND removed = false;");


            while (rs.next()) {
                vehicles.add(new Vehicle(rs.getString(1), rs.getString(2), rs.getString(3), rs.getBoolean(4),
                        rs.getString(5),rs.getString(6), rs.getString(7),rs.getString(8),rs.getTimestamp(9),
                        rs.getTimestamp(10),rs.getTimestamp(11)));
            }

            if(vehicles.size()>0)
                return vehicles;
        }catch (Exception e){
            System.out.println("------------------EXCEPTION IN THE QUERIES CLASS------------------");
            e.printStackTrace();
        }
        finally{
            mysqlCon.endCon();
        }

        log.warn("No vehicle found with 'vin' - "+vin + " and not removed" );
        System.out.println("No vehicle found with 'vin' - "+ vin + " and not removed");
        return null;
    }

    public List<DownloadedImage> getDownloadedImagesByUuidAndNotRemoved(String uuid) {
        Statement stmt = mysqlCon.getStatement();
        List<DownloadedImage> dImages = new ArrayList<DownloadedImage>();
        try {
            log.debug("Querying vehicle table, finding with 'vin' and Not removed....");
            ResultSet rs = stmt.executeQuery("select di.* from vehicle_image vi join downloaded_image di on vi.downloaded_image_id = di.id " +
                    "where vi.vehicle_uuid = '"+uuid+"' AND vi.is_removed=false;");

            while (rs.next()) {
                dImages.add(new DownloadedImage(rs.getLong(1), rs.getString(2), rs.getString(3),rs.getString(4),
                        rs.getInt(5),rs.getInt(6), rs.getTimestamp(7),rs.getTimestamp(8)));
            }

            if(dImages.size()>0)
                return dImages;
        }catch (Exception e){
            System.out.println("------------------EXCEPTION IN THE QUERIES CLASS------------------");
            e.printStackTrace();
        }
        finally{
            mysqlCon.endCon();
        }

        return null;

    }

    public List<Vehicle> getVehiclesByOemAndMappedImagesSpecificQuantity(String oem, int quantity) {
        Statement stmt = mysqlCon.getStatement();
        List<Vehicle> vehicles = new ArrayList<Vehicle>();
        try {
            log.debug("Querying vehicle table, finding with 'vin' and Not removed....");
            ResultSet rs = stmt.executeQuery(String.format("select v.*, count(*) from vehicle_image vi join vehicle v " +
                    "on v.uuid = vi.vehicle_uuid where v.oem = '%s' group by vehicle_uuid having count(*) = %d;", oem, quantity));

            while (rs.next()) {
                vehicles.add(new Vehicle(rs.getString(1), rs.getString(2), rs.getString(3), rs.getBoolean(4),
                        rs.getString(5),rs.getString(6), rs.getString(7),rs.getString(8),rs.getTimestamp(9),
                        rs.getTimestamp(10),rs.getTimestamp(11)));
            }

            if(vehicles.size()>0)
                return vehicles;
        }catch (Exception e){
            System.out.println("------------------EXCEPTION IN THE QUERIES CLASS------------------");
            e.printStackTrace();
        }
        finally{
            mysqlCon.endCon();
        }

        return null;
    }

    public List<Vehicle> getVehiclesByOemStatusAndNumberOfImagesMapped(String oem, String status, int quantity) {
        Statement stmt = mysqlCon.getStatement();
        List<Vehicle> vehicles = new ArrayList<Vehicle>();
        try {

            ResultSet rs = stmt.executeQuery(String.format("select v.* from vehicle_image vi join vehicle v on v.uuid = vi.vehicle_uuid  " +
                    "join downloaded_image di on di.`id` = vi.downloaded_image_id where v.oem = '%s' and " +
                    "v.status ='%s' group by vehicle_uuid having count(*) = %d;",oem,status,quantity));

            while (rs.next()) {
                vehicles.add(new Vehicle(rs.getString(1), rs.getString(2), rs.getString(3), rs.getBoolean(4),
                        rs.getString(5),rs.getString(6), rs.getString(7),rs.getString(8),rs.getTimestamp(9),
                        rs.getTimestamp(10),rs.getTimestamp(11)));
            }

            if(vehicles.size()>0)
                return vehicles;
        }catch (Exception e){
            System.out.println("------------------EXCEPTION IN THE QUERIES CLASS------------------");
            e.printStackTrace();
        }
        finally{
            mysqlCon.endCon();
        }

        return null;
    }

    public List<Vehicle> getVehiclesByAccountIdStatusNotRemoved(String accountId, String status) {
        Statement stmt = mysqlCon.getStatement();
        List<Vehicle> vehicles = new ArrayList<Vehicle>();
        try {

            ResultSet rs = stmt.executeQuery(String.format("select * from vehicle where account_id = '%s'" +
                    " AND status = '%s' AND removed = false;", accountId, status));

            while (rs.next()) {
                vehicles.add(new Vehicle(rs.getString(1), rs.getString(2), rs.getString(3), rs.getBoolean(4),
                        rs.getString(5),rs.getString(6), rs.getString(7),rs.getString(8),rs.getTimestamp(9),
                        rs.getTimestamp(10),rs.getTimestamp(11)));
            }

            if(vehicles.size()>0)
                return vehicles;
        }catch (Exception e){
            System.out.println("------------------EXCEPTION IN THE QUERIES CLASS------------------");
            e.printStackTrace();
        }
        finally{
            mysqlCon.endCon();
        }

        return null;
    }


    public int getNumberOfVehiclesByVin(String vin){
        Statement stmt = mysqlCon.getStatement();
        List<Vehicle> vehicles = new ArrayList<Vehicle>();
        int count=0;
        try {
            log.debug("Querying vehicle table, finding number of vehicles for vin = " + vin + "...");
            ResultSet rs = stmt.executeQuery(String.format("select * from vehicle where vin = '%s';", vin));

            while (rs.next()) {
                count++;
            }
        }catch (Exception e){
            System.out.println("------------------EXCEPTION IN THE QUERIES CLASS------------------");
            e.printStackTrace();
        }
        finally{
            mysqlCon.endCon();
        }

        return count;

    }

    public int getNumberOfVehiclesByAccountIdNotRemoved(String accountId){
        Statement stmt = mysqlCon.getStatement();
        List<Vehicle> vehicles = new ArrayList<Vehicle>();
        int count=0;
        try {
            log.debug("Querying vehicle table, finding number of vehicles for account_id = " + accountId + "...");
            ResultSet rs = stmt.executeQuery(String.format("select * from vehicle where account_id = '%s' AND removed = false;", accountId));

            while (rs.next()) {
                count++;
            }
        }catch (Exception e){
            System.out.println("------------------EXCEPTION IN THE QUERIES CLASS------------------");
            e.printStackTrace();
        }
        finally{
            mysqlCon.endCon();
        }

        return count;

    }

    public int getNumberOfAccountsByOem(String oem){
        Statement stmt = mysqlCon.getStatement();
        List<Vehicle> vehicles = new ArrayList<Vehicle>();
        int count=0;
        try {
            log.debug("Querying vehicle table, finding number of vehicles for oem = " + oem + "...");
            ResultSet rs = stmt.executeQuery(String.format("select distinct(account_id) from vehicle where oem = '%s'", oem));

            while (rs.next()) {
                count++;
            }
        }catch (Exception e){
            System.out.println("------------------EXCEPTION IN THE QUERIES CLASS------------------");
            e.printStackTrace();
        }
        finally{
            mysqlCon.endCon();
        }

        return count;

    }

    public List<Vehicle> getVehicleBySpecificUuidLength(int length) {
        Statement stmt = mysqlCon.getStatement();
        List<Vehicle> vehicles = new ArrayList<Vehicle>();
        try {
            log.debug("Querying vehicle table, finding with uuid length = "+length+ "...");
            ResultSet rs = stmt.executeQuery("select * from vehicle where LENGTH(UUID) = "+length+";");


            while (rs.next()) {
                vehicles.add(new Vehicle(rs.getString(1), rs.getString(2), rs.getString(3), rs.getBoolean(4),
                        rs.getString(5),rs.getString(6), rs.getString(7),rs.getString(8),rs.getTimestamp(9),
                        rs.getTimestamp(10),rs.getTimestamp(11)));
            }

            if(vehicles.size()>0)
                return vehicles;
        }catch (Exception e){
            System.out.println("------------------EXCEPTION IN THE QUERIES CLASS------------------");
            e.printStackTrace();
        }
        finally{
            mysqlCon.endCon();
        }

        return null;
    }

    public List<Vehicle> getVehiclesByRemoved(boolean removed) {
        Statement stmt = mysqlCon.getStatement();
        List<Vehicle> vehicles = new ArrayList<Vehicle>();
        try {
            log.debug("Querying vehicle table, finding with removed ="+removed+ "...");
            ResultSet rs = stmt.executeQuery("select * from vehicle where removed = "+removed+";");

            while (rs.next()) {
                vehicles.add(new Vehicle(rs.getString(1), rs.getString(2), rs.getString(3), rs.getBoolean(4),
                        rs.getString(5),rs.getString(6), rs.getString(7),rs.getString(8),rs.getTimestamp(9),
                        rs.getTimestamp(10),rs.getTimestamp(11)));
            }

            if(vehicles.size()>0)
                return vehicles;
        }catch (Exception e){
            System.out.println("------------------EXCEPTION IN THE QUERIES CLASS------------------");
            e.printStackTrace();
        }
        finally{
            mysqlCon.endCon();
        }

        return null;
    }

    public int getNumberOfImagesForUuid(String uuid){
        Statement stmt = mysqlCon.getStatement();
        List<Vehicle> vehicles = new ArrayList<Vehicle>();
        int count=0;
        try {
            log.debug("Querying vehicle table, finding number of images for uuid = "+uuid+ "...");
            ResultSet rs = stmt.executeQuery(String.format("select * from vehicle_image where vehicle_uuid = '%s';", uuid));

            while (rs.next()) {
                count++;
            }
        }catch (Exception e){
            System.out.println("------------------EXCEPTION IN THE QUERIES CLASS------------------");
            e.printStackTrace();
        }
        finally{
            mysqlCon.endCon();
        }

        return count;

    }

    public List<Vehicle> getVehiclesByStatusAndRemovedAndOem(String status, boolean removed, String oem){
        List<Vehicle> vehicles = new ArrayList<Vehicle>();

        Statement stmt = mysqlCon.getStatement();
        try {
            log.debug("Querying vehicle table, finding with 'status' & 'removed' & 'oem'....");
            ResultSet rs = stmt.executeQuery("select * from vehicle where status = '"+status+"' AND removed = "+removed+" AND oem = '"+oem+"';");

            while (rs.next()) {
                vehicles.add(new Vehicle(rs.getString(1), rs.getString(2), rs.getString(3), rs.getBoolean(4),
                        rs.getString(5),rs.getString(6), rs.getString(7),rs.getString(8),rs.getTimestamp(9),
                            rs.getTimestamp(10),rs.getTimestamp(11)));
            }

            if(vehicles.size()>0)
            return vehicles;
        }catch (Exception e){
            System.out.println("------------------EXCEPTION IN THE QUERIES CLASS------------------");
            e.printStackTrace();
        }
        finally{
            mysqlCon.endCon();
        }

        log.warn("No vehicle found with 'oem', 'removed', and 'status - " + oem + ", " + removed +", " + status );
        System.out.println("No vehicle found with 'status' and 'removed' and 'oem' - "+ status + "\t" + removed + "\t" + oem);
        return null;

    }

    public int updateVehicleNextScheduledOnToNow(String uuid) {

        Statement stmt = mysqlCon.getStatement();
        int response=0;
        try {
            log.debug("Querying vehicle table, updating nextScheduledOn timestamp to now ....");
            response = stmt.executeUpdate("UPDATE vehicle SET next_scheduled_on = now() where uuid = '"+uuid+"'");
            log.info("The query effected "+ response + "raws");

            if(response <1)
             {
                 System.out.println("The Query to update the timestamp did not effect any rows in the table");
                 log.warn("The Query to update the timestamp did not effect any rows in the table");
             }


        }catch (Exception e){
            System.out.println("------------------EXCEPTION IN THE QUERIES CLASS------------------");
            e.printStackTrace();
        }
        finally{
            mysqlCon.endCon();
            return response;
        }

    }

    public int updateVehicleSetNextSchedulesToNull() {

        Statement stmt = mysqlCon.getStatement();
        int response=0;
        try {
            log.debug("Querying vehicle table, setting next_scheduled_on's to NULL ....");
            response = stmt.executeUpdate("UPDATE vehicle SET next_scheduled_on = NULL WHERE (next_scheduled_on IS" +
                                                             " NOT NULL AND next_scheduled_on !='9999-01-01 00:00:00');");
            log.info("The query effected "+ response + "raws");

            if(response <1)
            {
                System.out.println("The Query to set the timestamp to NULL did not effect any rows in the table");
                log.warn("The Query to set the timestamp to NULL did not effect any rows in the table");
            }


        }catch (Exception e){
            System.out.println("------------------EXCEPTION IN THE QUERIES CLASS------------------");
            e.printStackTrace();
        }
        finally{
            mysqlCon.endCon();
            return response;
        }

    }

    public int updateVehicleSetRemovedToTrue(String uuid) {

        Statement stmt = mysqlCon.getStatement();
        int response=0;
        try {
            log.debug("Querying vehicle table, setting removed column to true for uuid: "+ uuid);
            response = stmt.executeUpdate("update vehicle set removed = true where uuid ='"+uuid+"';");
            log.info("The query effected "+ response + "raws");

            if(response <1)
            {
                System.out.println("The Query to set the removed column to true for uuid: +"+ uuid +" didn't effect any rows");
                log.error("The Query to set the removed column to true for uuid: +"+ uuid +" didn't effect any rows");
            }


        }catch (Exception e){
            System.out.println("------------------EXCEPTION IN THE QUERIES CLASS------------------");
            e.printStackTrace();
        }
        finally{
            mysqlCon.endCon();
            return response;
        }

    }

    public int updateVehicleSetRemovedToFalse(String uuid) {

        Statement stmt = mysqlCon.getStatement();
        int response=0;
        try {
            log.debug("Querying vehicle table, setting removed column to true for uuid: "+ uuid);
            response = stmt.executeUpdate(String.format("update vehicle set removed = false where uuid ='%s';", uuid));
            log.info("The query effected "+ response + "raws");

            if(response <1)
            {
                System.out.println("The Query to set the removed column to false for uuid: +"+ uuid +" didn't effect any rows");
                log.error("The Query to set the removed column to false for uuid: +"+ uuid +" didn't effect any rows");
            }


        }catch (Exception e){
            System.out.println("------------------EXCEPTION IN THE QUERIES CLASS------------------");
            e.printStackTrace();
        }
        finally{
            mysqlCon.endCon();
            return response;
        }

    }

    public void updateVehicleSetRemovedToTrueWhereLengthIs(int length) {

        Statement stmt = mysqlCon.getStatement();
        int response=0;
        try {
            log.debug("Querying vehicle table, setting removed column to true for uuid.length = 35 ");
            response = stmt.executeUpdate("update vehicle set removed = true where LENGTH(uuid) = "+length+" AND removed = false;");
            log.info("The query effected "+ response + "raws");


                System.out.println("The Query to set the removed column to true for uuid.length = 35 effect "+response+" rows");
                log.warn("The Query to set the removed column to true for uuid.length = 35 effect "+response+" rows");



        }catch (Exception e){
            System.out.println("------------------EXCEPTION IN THE QUERIES CLASS------------------");
            e.printStackTrace();
        }
        finally{
            mysqlCon.endCon();
        }


    }

    public void deleteImagesForVehiclesHavingUuidLength(int length) {

        Statement stmt = mysqlCon.getStatement();
        int response=0;
        try {

            if (length == 32) {
                throw new Exception("Not allowed to delete with length 32");
            }
        }
        catch(Exception e){
            System.out.println("------------------EXCEPTION IN THE QUERIES CLASS------------------");
            e.printStackTrace();
        }
        try {
            log.debug(String.format("Deleting image associations for uuid.length = %d", length));
            response = stmt.executeUpdate(String.format("delete from vehicle_image where LENGTH(vehicle_uuid) = %d;", length));
            log.info("The query effected "+ response + "rows");


            System.out.printf("The Query of deleting image associations for uuid.length = %d effected %d rows%n", length, response);
            log.info(String.format("The Query of deleting image associations for uuid.length = %d effected %d rows", length, response));

        }catch (Exception e){
            e.printStackTrace();
        }
        finally{
            mysqlCon.endCon();
        }

    }

    public void deleteVehiclesHavingUuidLength(int length) {

        Statement stmt = mysqlCon.getStatement();
        int response=0;
        try {

            if (length == 32) {
                throw new Exception("Not allowed to delete with length 32");
            }
        }
        catch(Exception e){
            System.out.println("------------------EXCEPTION IN THE QUERIES CLASS------------------");
            e.printStackTrace();
        }
        try {
            log.debug(String.format("Deleting image associations for uuid.length = %d", length));
            response = stmt.executeUpdate(String.format("delete from vehicle where LENGTH(uuid) = %d;", length));
            log.info("The query effected "+ response + "rows");


            System.out.printf("The Query of deleting vehicles for uuid.length = %d effected %d rows%n", length, response);
            log.info(String.format("The Query of deleting vehicles for uuid.length = %d effected %d rows", length, response));

        }catch (Exception e){
            System.out.println("------------------EXCEPTION IN THE QUERIES CLASS------------------");
            e.printStackTrace();
        }
        finally{
            mysqlCon.endCon();
        }

    }

    public int getNumberOfImagesMappedToVehicleByUuidNotRemoved(String uuid){

        Statement stmt = mysqlCon.getStatement();
        int count = 0;

        try{
            log.debug("getting the number of Images mapped to a vehicle by uuid..."+ ", uuid:"+uuid+ "not removed");
            ResultSet rs = stmt.executeQuery("select * from vehicle_image where vehicle_uuid = '"+uuid+"' AND is_removed = false;");
            while(rs.next()){
                count++;
            }

        }catch (Exception e){
            System.out.println("------------------EXCEPTION IN THE QUERIES CLASS------------------");
            e.printStackTrace();
        }
        finally {
            mysqlCon.endCon();
            log.info(count+" images mapped to vehicle with uuid:"+uuid);
            return count;
        }


    }

    public List<Vehicle> getVehiclesByStatus(String status){
        List<Vehicle> vehicles = new ArrayList<Vehicle>();

        Statement stmt = mysqlCon.getStatement();
        try {
            log.debug("Querying vehicle table, finding with status....");
            ResultSet rs = stmt.executeQuery("select * from vehicle where status = '"+status+"';");

            while (rs.next()) {
                vehicles.add( new Vehicle(rs.getString(1), rs.getString(2), rs.getString(3), rs.getBoolean(4),
                        rs.getString(5),rs.getString(6), rs.getString(7),rs.getString(8),rs.getTimestamp(9),
                        rs.getTimestamp(10),rs.getTimestamp(11)));
            }
            if(vehicles.size()>0) {
                return vehicles;
            }
        }catch (Exception e){
            System.out.println("------------------EXCEPTION IN THE QUERIES CLASS------------------");
            e.printStackTrace();
        }
        finally{
            mysqlCon.endCon();
        }

        log.warn("No vehicle found with 'status' - "+ status );
        System.out.println("No vehicle found with 'status' - "+ status);
        return null;

    }

    private List<Vehicle> getVehiclesByOem(String oem){
        Statement stmt = mysqlCon.getStatement();

        List<Vehicle> vehicles = new ArrayList<Vehicle>();

        try {
            log.debug("Querying vehicle table, finding with 'oem'....");
            ResultSet rs = stmt.executeQuery(String.format("select * from vehicle where oem = '%s';", oem));

            while (rs.next()) {
                vehicles.add(new Vehicle(rs.getString(1), rs.getString(2), rs.getString(3), rs.getBoolean(4),
                        rs.getString(5),rs.getString(6), rs.getString(7),rs.getString(8),rs.getTimestamp(9),
                        rs.getTimestamp(10),rs.getTimestamp(11)));

            }
            if(vehicles.size()>0) {

                return vehicles;
            }
        }catch (Exception e){
            System.out.println("------------------EXCEPTION IN THE QUERIES CLASS------------------");
            e.printStackTrace();
        }
        finally{
            mysqlCon.endCon();
        }

        log.warn("No vehicle found with 'status' - " + oem );
        System.out.println("No vehicle found with 'oem' - "+ oem);
        return null;

    }

    public List<Vehicle> getVehiclesByStatusAndRemoved(String status, boolean removed){
        List<Vehicle> vehicles = new ArrayList<Vehicle>();

        Statement stmt = mysqlCon.getStatement();
        try {
            log.debug("Querying vehicle table, finding with 'status' & 'removed'....");
            ResultSet rs = stmt.executeQuery("select * from vehicle where status = '"+status+"' AND removed = '"+removed+"';");

            while (rs.next()) {
                vehicles.add(new Vehicle(rs.getString(1), rs.getString(2), rs.getString(3), rs.getBoolean(4),
                        rs.getString(5),rs.getString(6), rs.getString(7),rs.getString(8),rs.getTimestamp(9),
                        rs.getTimestamp(10),rs.getTimestamp(11)));
            }

            return vehicles;
        }catch (Exception e){
            System.out.println("------------------EXCEPTION IN THE QUERIES CLASS------------------");
            e.printStackTrace();
        }
        finally{
            mysqlCon.endCon();
        }

        log.warn("No vehicle found with 'status', and 'removed - " + status + ", " + removed );
        System.out.println("No vehicle found with 'status' and 'removed' - "+ status + "\t" + removed);
        return null;

    }

    public List<Vehicle> getVehiclesByOemAndStatusAndCreatedOnNotNull(String oem, String status){
        Statement stmt = mysqlCon.getStatement();

        List<Vehicle> vehicles = new ArrayList<Vehicle>();

        try {
            log.debug("Querying vehicle table, finding with 'oem' & 'status'....");
            ResultSet rs = stmt.executeQuery("select * from vehicle where removed = false AND  oem = '"+oem+"' " +
                    "AND status = '"+status+ "' AND created_on is not null;");

            while (rs.next()) {
                vehicles.add(new Vehicle(rs.getString(1), rs.getString(2), rs.getString(3), rs.getBoolean(4),
                        rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8),
                        rs.getTimestamp(9), rs.getTimestamp(10), rs.getTimestamp(11)));
            }

            return vehicles;
        }catch (Exception e){
            System.out.println("------------------EXCEPTION IN THE QUERIES CLASS-----------------");
            e.printStackTrace();
        }
        finally{
            mysqlCon.endCon();
        }

        return null;

    }

//    public static void main(String[] args) {
//
//    }


}