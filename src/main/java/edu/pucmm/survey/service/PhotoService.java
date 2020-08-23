package edu.pucmm.survey.service;

import edu.pucmm.survey.entity.Photo;

public class PhotoService extends DBMService<Photo> {

    public PhotoService() {
        super(Photo.class);
    }
}