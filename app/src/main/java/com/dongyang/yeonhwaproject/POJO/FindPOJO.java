package com.dongyang.yeonhwaproject.POJO;

/**
 * Created by Kim Jong-Hwa on 2018-05-27.
 */

public class FindPOJO {
    private String name = "";
    private String address = "";
    private String tel = "";
    private String review_count = "";
    private String distance = "0.9";
    private String lat = "127";
    private String lon = "31";
    private String hpid = "111";
    private String avg_point = "0.0";
    private String isHosPhar;

    public String getIsHosPhar() {
        return isHosPhar;
    }

    public void setIsHosPhar(String isHosPhar) {
        this.isHosPhar = isHosPhar;
    }

    public String getAvg_point() {
        return avg_point;
    }

    public void setAvg_point(String avg_point) {
        this.avg_point = avg_point;
    }

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
