package resources;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Queries {

    public static Logger log = LogManager.getLogger(base.class.getName());
    private MysqlCon mysqlCon= new MysqlCon();
    public Vehicle getVehicleByUUID(String uuid) {
        Statement stmt = mysqlCon.getStatement();

        try {
            log.debug("Querying vehicle table, finding with 'uuid'....");
            ResultSet rs = stmt.executeQuery("select * from vehicle where uuid = '"+uuid+"';");

            while (rs.next())
                return new Vehicle(rs.getString(1), rs.getString(2), rs.getString(3), rs.getBoolean(4),
                        rs.getString(5),rs.getString(6), rs.getString(7),rs.getString(8),rs.getTimestamp(9).toLocalDateTime(),
                        rs.getTimestamp(10).toLocalDateTime(),rs.getTimestamp(11).toLocalDateTime());

        }catch (Exception e){
            e.printStackTrace();
        }

        finally{
            mysqlCon.endCon();
        }
        System.out.println("No vehicle found with 'uuid' - "+uuid);
        return null;
    }

    public Vehicle getVehicleByVIN(String vin) {
        Statement stmt = mysqlCon.getStatement();
        try {
            log.debug("Querying vehicle table, finding with 'vin'....");
            ResultSet rs = stmt.executeQuery("select * from vehicle where vin = '"+vin+"';");

            while (rs.next())
                return new Vehicle(rs.getString(1), rs.getString(2), rs.getString(3), rs.getBoolean(4),
                        rs.getString(5),rs.getString(6), rs.getString(7),rs.getString(8),rs.getTimestamp(9).toLocalDateTime(),
                        rs.getTimestamp(10).toLocalDateTime(),rs.getTimestamp(11).toLocalDateTime());

        }catch (Exception e){
            e.printStackTrace();
        }
        finally{
            mysqlCon.endCon();
        }

        log.warn("No vehicle found with 'uuid' - "+vin );
        System.out.println("No vehicle found with 'uuid' - "+vin);
        return null;
    }

    public List<Vehicle> getVehiclesByStatus(String status){
            List<Vehicle> vehicles = new ArrayList<Vehicle>();

        Statement stmt = mysqlCon.getStatement();
            try {
                log.debug("Querying vehicle table, finding with status....");
                ResultSet rs = stmt.executeQuery("select * from vehicle where status = '"+status+"';");

                while (rs.next()) {
                    vehicles.add( new Vehicle(rs.getString(1), rs.getString(2), rs.getString(3), rs.getBoolean(4),
                            rs.getString(5),rs.getString(6), rs.getString(7),rs.getString(8),rs.getTimestamp(9).toLocalDateTime(),
                            rs.getTimestamp(10).toLocalDateTime(),rs.getTimestamp(11).toLocalDateTime()));
                }
                if(vehicles.size()>0) {
                    return vehicles;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            finally{
                mysqlCon.endCon();
            }

            log.warn("No vehicle found with 'status' - "+ status );
        System.out.println("No vehicle found with 'status' - "+ status);
            return null;

    }

    public List<Vehicle> getVehiclesByOem(String oem){
        Statement stmt = mysqlCon.getStatement();

        List<Vehicle> vehicles = new ArrayList<Vehicle>();

        try {
            log.debug("Querying vehicle table, finding with 'oem'....");
            ResultSet rs = stmt.executeQuery("select * from vehicle where oem = '"+oem+"';");

            while (rs.next()) {
                vehicles.add(new Vehicle(rs.getString(1), rs.getString(2), rs.getString(3), rs.getBoolean(4),
                        rs.getString(5),rs.getString(6), rs.getString(7),rs.getString(8),rs.getTimestamp(9).toLocalDateTime(),
                        rs.getTimestamp(10).toLocalDateTime(),rs.getTimestamp(11).toLocalDateTime()));

            }
            if(vehicles.size()>0) {

                return vehicles;
            }
        }catch (Exception e){
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
                        rs.getString(5),rs.getString(6), rs.getString(7),rs.getString(8),rs.getTimestamp(9).toLocalDateTime(),
                        rs.getTimestamp(10).toLocalDateTime(),rs.getTimestamp(11).toLocalDateTime()));
            }

            return vehicles;
        }catch (Exception e){
            e.printStackTrace();
        }
        finally{
            mysqlCon.endCon();
        }

        log.warn("No vehicle found with 'status', and 'removed - " + status + ", " + removed );
        System.out.println("No vehicle found with 'status' and 'removed' - "+ status + "\t" + removed);
        return null;

    }

    public List<Vehicle> getVehiclesByOemAndStatus(String oem, String status){
        Statement stmt = mysqlCon.getStatement();

        List<Vehicle> vehicles = new ArrayList<Vehicle>();

        try {
            log.debug("Querying vehicle table, finding with 'oem' & 'status'....");
            ResultSet rs = stmt.executeQuery("select * from vehicle where oem = '"+oem+"' AND status = '"+status+ "';");

            while (rs.next()) {
                vehicles.add(new Vehicle(rs.getString(1), rs.getString(2), rs.getString(3), rs.getBoolean(4),
                        rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getTimestamp(9).toLocalDateTime(),
                        rs.getTimestamp(10).toLocalDateTime(), rs.getTimestamp(11).toLocalDateTime()));
            }

            return vehicles;
        }catch (Exception e){
            e.printStackTrace();
        }
        finally{
            mysqlCon.endCon();
        }
        log.warn("No vehicle found with 'oem', and 'status - " + oem + ", " + status );
        System.out.println("No vehicle found with 'oem' & 'status' - "+ oem + "\t" + status);
        return null;

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
            e.printStackTrace();
        }
        finally{
            mysqlCon.endCon();
            return response;
        }

    }


    public int getNumberOfImagesMappedToVehicleByUuid(String uuid){

        Statement stmt = mysqlCon.getStatement();
        int count = 0;

        try{
            log.debug("getting the number of Images mapped to a vehicle by uuid..."+ ", uuid:"+uuid);
            ResultSet rs = stmt.executeQuery("select * from vehicle_image where vehicle_uuid = '"+uuid+"';");
            while(rs.next()){
                count++;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            mysqlCon.endCon();
            log.info(count+" images mapped to vehicle with uuid:"+uuid);
            return count;
        }


    }

    public static void main(String[] args) {

        Queries q = new Queries();
//        Vehicle a = q.getVehicleByVIN("WBAJB9C57KB288436");
//        System.out.println(a);
        List<Vehicle> b = q.getVehiclesByOem("bmw");

        int count = 0;
        for(Vehicle v : b)
        {
            System.out.println(v);
            count++;
        }
        System.out.println(count);

    }


}