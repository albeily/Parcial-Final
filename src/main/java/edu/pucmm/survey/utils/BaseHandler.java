package edu.pucmm.survey.utils;

import edu.pucmm.survey.controller.Survey;
import io.javalin.Javalin;

public abstract class BaseHandler {

    protected final Javalin app;
    protected final Survey survey;

    public BaseHandler(Javalin app, Survey survey) {
        this.app = app;
        this.survey = survey;
    }

    abstract public void routes();
}
