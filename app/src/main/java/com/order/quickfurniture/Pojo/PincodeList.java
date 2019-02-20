package com.order.quickfurniture.Pojo;

public class PincodeList {
   private String id,ship_charge,area,pin;
    public PincodeList(String id, String ship_charge, String area,String pin) {
        this.id=id;
        this.ship_charge=ship_charge;
        this.area=area;
        this.pin=pin;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShip_charge() {
        return ship_charge;
    }

    public void setShip_charge(String ship_charge) {
        this.ship_charge = ship_charge;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
