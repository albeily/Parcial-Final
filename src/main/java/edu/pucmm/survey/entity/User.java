package edu.pucmm.survey.entity;

import edu.pucmm.survey.utils.RoleType;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "USER")
public class User implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    private String username;
    private String password;
    @Embedded
    private Person person;
    @Enumerated(EnumType.STRING)
    private RoleType role;

    public User(String username, String password, Person person, RoleType role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.person = person;
    }

    public User() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public RoleType getRole() {
        return role;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }
}
