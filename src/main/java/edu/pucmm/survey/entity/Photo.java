package edu.pucmm.survey.entity;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Embeddable
public class Photo implements Serializable {

    private String uri;

    public Photo(String uri) {
        this.uri = uri;
    }

    public Photo() {
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
