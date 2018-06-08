package com.dongyang.yeonhwaproject.POJO;

/**
 * Created by Kim Jong-Hwa on 2018-05-27.
 */

public class FindPOJO {
    private String name;
    private String address;
    private String tel;
    private String review_count;
    private String distance;
    private String lat;
    private String lon;
    private String hpid;
    private boolean is_review_in = false;

    public String getHpid() {
        return hpid;
    }

    public void setHpid(String hpid) {
        this.hpid = hpid;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public boolean isIs_review_in() {
        return is_review_in;
    }

    public String getReview_count() {
        return review_count;
    }

    public void setReview_count(String review_count) {
        this.review_count = review_count;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public boolean getIs_review_in() {
        return is_review_in;
    }

    public void setIs_review_in(boolean is_review_in) {
        this.is_review_in = is_review_in;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
