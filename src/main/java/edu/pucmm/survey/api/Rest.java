package edu.pucmm.survey.api;

import edu.pucmm.survey.controller.Survey;
import edu.pucmm.survey.entity.Form;
import edu.pucmm.survey.utils.BaseHandler;
import io.javalin.Javalin;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Rest extends BaseHandler {

    public Rest(Javalin app, Survey survey) {
        super(app, survey);
    }

    @Override
    public void routes() {
        app.routes(() -> {
            path("/api/rest", () -> {

                path("/form", () -> {
                    after(ctx -> {
                        ctx.header("Content-Type", "application/json");
                    });

                    get("/", ctx -> {
                        ctx.json(survey.getForms());
                    });

                    post("/", ctx -> {
                        Form form = ctx.bodyAsClass(Form.class);
                        ctx.json(survey.submit(form));
                    });
                });
            });
        });
    }
}