package edu.pucmm.survey;

import edu.pucmm.survey.api.soap.Soap;
import edu.pucmm.survey.controller.Survey;
import edu.pucmm.survey.handler.Handler;
import edu.pucmm.survey.service.DBService;
import edu.pucmm.survey.utils.DefaultData;
import io.javalin.Javalin;
import io.javalin.core.util.RouteOverviewPlugin;
import org.jasypt.util.text.AES256TextEncryptor;

import java.sql.SQLException;


public class Main {

    private static AES256TextEncryptor encryptor;
    private static final String password = "admin";

    private static DBService dbService;
    private static Survey survey;
    private static Javalin app;

    public static void main(String[] args) {

        try {
            encryptor = new AES256TextEncryptor();
            encryptor.setPassword(password);

            dbService = new DBService();
            dbService.startDB();

            survey = new Survey(encryptor);

            app = Javalin.create(config -> {
                config.addStaticFiles("/local/");
                config.addStaticFiles("/local/design/js/");
                config.addStaticFiles("/local/rest/");
                config.registerPlugin(new RouteOverviewPlugin("/admin/routes"));
                config.enableCorsForAllOrigins();
            });

            new Soap(app, survey).routes();

            app.start(herokuPort());

            new Handler(app, survey, encryptor).routes();
            new DefaultData(survey).load();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private static int herokuPort() {
        int port = 7000;

        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            port = Integer.parseInt(processBuilder.environment().get("PORT"));
        }

        return port;
    }

    public static Survey getSurvey() {
        return survey;
    }
}
