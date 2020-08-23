package edu.pucmm.survey.entity;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
public class Person implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String name;
    private String lastname;
    @Embedded
    private Photo photo;

    public Person(String name, String lastname) {
        this.name = name;
        this.lastname = lastname;
    }

    public Person() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }
}
