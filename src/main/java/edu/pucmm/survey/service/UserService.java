package edu.pucmm.survey.service;

import edu.pucmm.survey.entity.User;

public class UserService extends DBMService<User> {

    public UserService() {
        super(User.class);
    }
}