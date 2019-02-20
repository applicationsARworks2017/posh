package com.order.quickfurniture.Pojo;

public class AddRessList {
   private String id,full_name,mobile,pincode_id,address,locality,landmark,city,email;

    public AddRessList(String id, String full_name, String mobile, String pincode_id, String address, String locality,
                       String landmark, String city, String email) {
     this.id=id;
     this.full_name=full_name;
     this.mobile=mobile;
     this.pincode_id=pincode_id;
     this.address=address;
     this.locality=locality;
     this.landmark=landmark;
     this.city=city;
     this.email=email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPincode_id() {
        return pincode_id;
    }

    public void setPincode_id(String pincode_id) {
        this.pincode_id = pincode_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
