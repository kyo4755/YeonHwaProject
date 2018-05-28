package com.dongyang.yeonhwaproject.POJO;

/**
 * Created by Kim Jong-Hwa on 2018-05-27.
 */

public class FindPOJO {
    private String image_src;
    private String name;
    private String review_count;
    private String distance;
    private boolean is_review_in;

    public String getImage_src() {
        return image_src;
    }

    public void setImage_src(String image_src) {
        this.image_src = image_src;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
