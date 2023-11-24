package com.redhat.training.conference.speaker;

import java.util.HashMap;
import java.util.Map;


import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;

/**
 * Speaker
 */
@Entity
public class Speaker extends PanacheEntity {

    public String uuid;

    public String nameFirst;
    public String nameLast;
    public String organization;
    public String biography;
    public String picture;
    public String twitterHandle;

    @Transient
    public Map<String, String> links = new HashMap<>();

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[" +
                "<" + id + ">" +
                "," +
                "nameFirst=" + nameFirst +
                "," +
                "nameLast=" + nameLast +
                "]";
    }
}
