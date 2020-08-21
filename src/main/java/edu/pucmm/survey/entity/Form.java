package edu.pucmm.survey.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "FORM")
public class Form implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne
    private User user;
    @Embedded
    private Person person;
    private String sector;
    @Enumerated(EnumType.STRING)
    private Education education;
    @Embedded
    private Location location;

    public Form(Location location, Person person, String sector, Education education, User user) {
        this.location = location;
        this.person = person;
        this.sector = sector;
        this.education = education;
        this.user = user;
    }

    public Form(Form form) {
        this.location = form.getLocation();
        this.person = form.getPerson();
        this.sector = form.getSector();
        this.education = form.getEducation();
        this.user = form.getUser();
    }

    public Form() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public Education getEducation() {
        return education;
    }

    public void setEducation(Education education) {
        this.education = education;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}