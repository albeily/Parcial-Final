package edu.pucmm.survey.utils;

import edu.pucmm.survey.controller.Survey;
import edu.pucmm.survey.entity.*;

public class DefaultData {

    private final Survey survey;

    public DefaultData(Survey survey) {
        this.survey = survey;
    }

    public void load() {
        Person person;
        User user;

        person = new Person("admin", "admin");
        user = new User("admin", "admin", person, RoleType.ADMIN);
        survey.submit(user);

        person = new Person("Jorge", "Michelen");
        survey.submit(new Form(new Location(18.483402, -69.929611), person, "La zurza", Education.DOCTORATE, user));

        person = new Person("Albeily", "Romano");
        survey.submit(new Form(new Location(19.470800, -70.692039), person, "Gurabo", Education.POSTGRADUATE, user));

        person = new Person("user", "user");
        user = new User("user", "user", person, RoleType.USER);
        survey.submit(user);
    }
}