package com.order.quickfurniture.Pojo;

public class Items {
    String id,category_id,sub_category_id,name,price,actual_price,discount,photo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getActual_price() {
        return actual_price;
    }

    public void setActual_price(String actual_price) {
        this.actual_price = actual_price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Items(String id, String category_id, String sub_category_id, String name,
                 String price, String actual_price, String discount, String photo) {
        this.id=id;
        this.category_id=category_id;
        this.sub_category_id=sub_category_id;
        this.name=name;
        this.price=price;
        this.actual_price=actual_price;
        this.discount=discount;
        this.photo=photo;


    }
}
