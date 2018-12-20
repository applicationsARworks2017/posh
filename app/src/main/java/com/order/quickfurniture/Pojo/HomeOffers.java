package com.order.quickfurniture.Pojo;

public class HomeOffers {
    String id, category_id, sub_category_id,name,price,image;


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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public HomeOffers(String id, String category_id, String sub_category_id, String name, String price, String image) {
        this.id=id;
        this.category_id=category_id;
        this.sub_category_id=sub_category_id;
        this.name=name;
        this.price=price;
        this.image=image;

    }
}
