package edu.pucmm.survey.api.soap;

import edu.pucmm.survey.controller.Survey;
import edu.pucmm.survey.entity.Form;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public class FormWebServices {

    private final Survey survey;

    public FormWebServices(Survey survey) {
        this.survey = survey;
    }

    @WebMethod
    public List<Form> getAll() {
        return survey.getForms();
    }

    @WebMethod
    public boolean submit(Form form) {
        return survey.submit(form);
    }
}