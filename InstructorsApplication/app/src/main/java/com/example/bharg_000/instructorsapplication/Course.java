package com.example.bharg_000.instructorsapplication;

/**
 * Created by BAngadi on 4/5/2018.
 */

public class Course {
    public String title;
    public String image;
    public String desc;
    public String userid;
    public String show;
    public Course(){

    }

    public Course(String title, String image, String desc, String userid,String show) {
        this.title = title;
        this.image = image;
        this.desc = desc;
        this.show=show;
        this.userid = userid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getShow() {
        return show;
    }

    public void setShow(String show) {
        this.show = show;
    }
}
