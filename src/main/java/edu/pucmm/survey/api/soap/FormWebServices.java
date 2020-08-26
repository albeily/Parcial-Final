package edu.pucmm.survey.api.soap;

import edu.pucmm.survey.Main;
import edu.pucmm.survey.entity.Form;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public class FormWebServices {

    @WebMethod
    public List<Form> getAll() {
        return Main.getSurvey().getForms();
    }

    @WebMethod
    public boolean submit(Form form) {
        return Main.getSurvey().submit(form);
    }
}