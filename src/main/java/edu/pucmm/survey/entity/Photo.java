package edu.pucmm.survey.entity;

import javax.persistence.Embeddable;
import javax.persistence.Lob;
import java.io.Serializable;

@Embeddable
public class Photo implements Serializable {

    @Lob
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
