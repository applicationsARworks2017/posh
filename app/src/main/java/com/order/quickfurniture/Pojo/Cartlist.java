package com.order.quickfurniture.Pojo;

import org.json.JSONObject;

public class Cartlist {
    String id,c_id,i_id,category_id,sub_category_id,item_type_id,discount,actual_price,description;
    String user_id;
    String item_id;
    String quentity;
    String price;
    String table_no;
    String status;
    String order_no;
    String quantity;
    String item_name;
    String photo;


    public Cartlist(String c_id, String user_id, String item_id, String quentity, String price,
                    String i_id, String category_id, String sub_category_id, String item_type_id,
                    String name, String discount, String actual_price, String image,
                    String description) {
        this.quantity=quentity;
        this.item_name=name;
        this.photo=image;
        this.c_id=c_id;
        this.user_id=user_id;
        this.item_id=item_id;
        this.price=price;
        this.i_id=i_id;
        this.category_id=category_id;
        this.sub_category_id=sub_category_id;
        this.item_type_id=item_type_id;
        this.discount=discount;
        this.actual_price=actual_price;
        this.description=description;
    }

    public String getC_id() {
        return c_id;
    }

    public void setC_id(String c_id) {
        this.c_id = c_id;
    }

    public String getI_id() {
        return i_id;
    }

    public void setI_id(String i_id) {
        this.i_id = i_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getSub_category_id() {
        return sub_category_id;
    }

    public void setSub_category_id(String sub_category_id) {
        this.sub_category_id = sub_category_id;
    }

    public String getItem_type_id() {
        return item_type_id;
    }

    public void setItem_type_id(String item_type_id) {
        this.item_type_id = item_type_id;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getActual_price() {
        return actual_price;
    }

    public void setActual_price(String actual_price) {
        this.actual_price = actual_price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
   /* @Override
    public String toString() {
        return "ClassPojo [id = " + id + ",user_id = " + user_id + ", item_id = " + item_id  +
                ", quentity = " + quentity + ", price = " + price+ ",status="+status+"" +
                ",table_no="+table_no+",order_no="+order_no+"]";
    }*/

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getQuentity() {
        return quentity;
    }

    public void setQuentity(String quentity) {
        this.quentity = quentity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTable_no() {
        return table_no;
    }

    public void setTable_no(String table_no) {
        this.table_no = table_no;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }
}
/*{
    "carts": [
        {
            "id": 26,
            "user_id": 1,
            "item_id": 5,
            "quentity": 1,
            "price": 100,
            "created": "2019-02-18T14:00:05+00:00",
            "modified": "2019-02-18T14:00:05+00:00",
            "item": {
                "id": 5,
                "category_id": 3,
                "sub_category_id": 1,
                "item_type_id": 7,
                "brand_id": 6,
                "name": "Walls",
                "price": 100,
                "discount": 10,
                "actual_price": 9.96,
                "image": "http://applicationworld.net/furniture/files/items/file15459623981292757876.jpg,http://applicationworld.net/furniture/files/items/file15459623982080139866.jpg",
                "description": null,
                "created": "2018-12-18T18:58:26+00:00",
                "modified": "2018-12-28T01:59:58+00:00"
            },
            "user": {
                "id": 1,
                "name": "sdfsdfd",
                "email": "admin@gmail.com",
                "mobile": "7205674061",
                "photo": "file1543543632488457718.png",
                "created": "2018-11-30T02:07:12+00:00",
                "modified": "2018-11-30T02:07:12+00:00",
                "usertype": "Admin",
                "fcm_id": "df",
                "ios_fcm_id": "dfdf"
            }
        },*/