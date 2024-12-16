
package com.example.tmdb.model;

public class ProductionCompany {


    public Integer id;

    public Object logo_path;

    public String name;

    public String origin_country;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Object getLogo_path() {
        return logo_path;
    }

    public void setLogo_path(Object logo_path) {
        this.logo_path = logo_path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrigin_country() {
        return origin_country;
    }

    public void setOrigin_country(String origin_country) {
        this.origin_country = origin_country;
    }
}
