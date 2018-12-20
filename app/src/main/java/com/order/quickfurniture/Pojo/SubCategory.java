package com.order.quickfurniture.Pojo;

public class SubCategory {
    String id,title,photo;

    public SubCategory() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public SubCategory(String id, String title, String photo) {
        this.id=id;
        this.title=title;
        this.photo=photo;

    }
}
