package model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Vehicle {

    private String uuid;
    private String account_id;
    private String production_number;
    private boolean removed;
    private String status;
    private String vin;
    private String removed_at;
    private String oem;
    private LocalDateTime created_on;
    private LocalDateTime last_modified_on;
    private LocalDateTime next_scheduled_on;

    public Vehicle(String uuid, String account_id, String production_number, boolean removed, String status, String vin,
                   String removed_at, String oem, LocalDateTime created_on, LocalDateTime last_modified_on, LocalDateTime next_scheduled_on) {
        this.uuid = uuid;
        this.account_id = account_id;
        this.production_number = production_number;
        this.removed = removed;
        this.status = status;
        this.vin = vin;
        this.removed_at = removed_at;
        this.oem = oem;
        this.created_on = created_on;
        this.last_modified_on = last_modified_on;
        this.next_scheduled_on = next_scheduled_on;
    }

    public Vehicle(Vehicle v)
    {
        this.uuid = v.uuid;
        this.account_id = v.account_id;
        this.production_number = v.production_number;
        this.removed = v.removed;
        this.status = v.status;
        this.vin = v.vin;
        this.removed_at = v.removed_at;
        this.oem = v.oem;
        this.created_on = v.created_on;
        this.last_modified_on = v.last_modified_on;
        this.next_scheduled_on = v.next_scheduled_on;
    }

    public Vehicle(String uuid, String account_id, String production_number, boolean removed, String status, String vin,
                   String removed_at, String oem, Timestamp created_on, Timestamp last_modified_on, Timestamp next_scheduled_on) {

        this.uuid = uuid;
        this.account_id = account_id;
        this.production_number = production_number;
        this.removed = removed;
        this.status = status;
        this.vin = vin;
        this.removed_at = removed_at;
        this.oem = oem;


        if(created_on!=null)
        {
            this.created_on = created_on.toLocalDateTime();
        }

        if(last_modified_on!=null)
        {
            this.last_modified_on = last_modified_on.toLocalDateTime();
        }

        if(next_scheduled_on!=null)
        {
            this.next_scheduled_on = next_scheduled_on.toLocalDateTime();
        }

    }

    @Override
    public String toString() {
        return "\nVehicle{" +
                "uuid='" + uuid + '\'' +
                ", account_id='" + account_id + '\'' +
                ", production_number='" + production_number + '\'' +
                ", removed=" + removed +
                ", status='" + status + '\'' +
                ", vin='" + vin + '\'' +
                ", removed_at='" + removed_at + '\'' +
                ", oem='" + oem + '\'' +
                ", created_on=" + created_on +
                ", last_modified_on=" + last_modified_on +
                ", next_scheduled_on=" + next_scheduled_on +
                "}\n";
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public String getProduction_number() {
        return production_number;
    }

    public void setProduction_number(String production_number) {
        this.production_number = production_number;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getRemoved_at() {
        return removed_at;
    }

    public void setRemoved_at(String removed_at) {
        this.removed_at = removed_at;
    }

    public String getOem() {
        return oem;
    }

    public void setOem(String oem) {
        this.oem = oem;
    }

    public LocalDateTime getCreated_on() {
        return created_on;
    }

    public void setCreated_on(LocalDateTime created_on) {
        this.created_on = created_on;
    }

    public LocalDateTime getLast_modified_on() {
        return last_modified_on;
    }

    public void setLast_modified_on(LocalDateTime last_modified_on) {
        this.last_modified_on = last_modified_on;
    }

    public LocalDateTime getNext_scheduled_on() {
        return next_scheduled_on;
    }

    public void setNext_scheduled_on(LocalDateTime next_scheduled_on) {
        this.next_scheduled_on = next_scheduled_on;
    }
}
