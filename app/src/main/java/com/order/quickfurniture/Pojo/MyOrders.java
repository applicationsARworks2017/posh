package com.order.quickfurniture.Pojo;

public class MyOrders {
    String c_id,sub_total,total_tax,shipping_cost,total,status,
            _id,item_id,price,item_name,item_photo;

    public String getC_id() {
        return c_id;
    }

    public void setC_id(String c_id) {
        this.c_id = c_id;
    }

    public String getSub_total() {
        return sub_total;
    }

    public void setSub_total(String sub_total) {
        this.sub_total = sub_total;
    }

    public String getTotal_tax() {
        return total_tax;
    }

    public void setTotal_tax(String total_tax) {
        this.total_tax = total_tax;
    }

    public String getShipping_cost() {
        return shipping_cost;
    }

    public void setShipping_cost(String shipping_cost) {
        this.shipping_cost = shipping_cost;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_photo() {
        return item_photo;
    }

    public void setItem_photo(String item_photo) {
        this.item_photo = item_photo;
    }

    public MyOrders(String c_id, String sub_total, String total_tax,
                    String shipping_cost, String total, String status, String id,
                    String item_id, String price, String item_name, String item_photo) {

        this.c_id = c_id;
        this.sub_total = sub_total;
        this.total_tax = total_tax;
        this.shipping_cost = shipping_cost;
        this.total = total;
        this.status = status;
        this._id = id;
        this.item_id = item_id;
        this.price = price;
        this.item_name = item_name;
        this.item_photo = item_photo;

    }
}
