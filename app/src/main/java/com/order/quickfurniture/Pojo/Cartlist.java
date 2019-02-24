package com.order.quickfurniture.Pojo;

public class Cartlist {
    String id;
    String user_id;
    String item_id;
    String quentity;
    String price;
    String table_no;
    String status;
    String order_no;

    /*@Override
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