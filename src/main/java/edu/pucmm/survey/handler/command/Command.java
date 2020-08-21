package edu.pucmm.survey.handler.command;

import edu.pucmm.survey.controller.Survey;
import edu.pucmm.survey.entity.Form;
import edu.pucmm.survey.entity.User;

public class Command<T> {

    private CommandMethod method;
    private T entity;

    public Command(CommandMethod method, T entity) {
        this.method = method;
        this.entity = entity;
    }

    public Command() {
    }

    public CommandMethod getMethod() {
        return method;
    }

    public void setMethod(CommandMethod method) {
        this.method = method;
    }

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }

    public boolean execute(Survey survey) {
        boolean ok = false;

        switch (method) {
            case CREATE:
                ok = create(survey);
                break;
            case DELETE:
                ok = delete(survey);
                break;
            case LIST:
                ok = true;
                break;
        }

        return ok;
    }

    private boolean create(Survey survey) {
        boolean ok = false;

        if (entity instanceof Form) {
            ok = survey.submit((Form) entity);
        } else if (entity instanceof User) {
            ok = survey.submit((User) entity);
        }

        return ok;
    }

    private boolean delete(Survey survey) {
        boolean ok = false;

        if (entity instanceof Form) {
            ok = survey.erase((Form) entity);
        } else if (entity instanceof User) {
            ok = survey.erase((User) entity);
        }

        return ok;
    }
}
