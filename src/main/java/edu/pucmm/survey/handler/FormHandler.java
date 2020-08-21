package edu.pucmm.survey.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.pucmm.survey.controller.Survey;
import edu.pucmm.survey.entity.Form;
import edu.pucmm.survey.entity.User;
import edu.pucmm.survey.utils.BaseHandler;
import edu.pucmm.survey.utils.RoleType;
import io.javalin.Javalin;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;

public class FormHandler extends BaseHandler {

    private final List<Session> socketUsers;

    public FormHandler(Javalin app, Survey survey) {
        super(app, survey);
        this.socketUsers = new ArrayList<Session>();
    }

    @Override
    public void routes() {

        app.before("/survey", ctx -> {
            User user = ctx.sessionAttribute("user");

            if (user == null) {
                ctx.redirect("/account/login");
            } else if (user.getRole() != RoleType.USER) {
                ctx.redirect("/account/login");
            }
        });

        app.routes(() -> {
            path("/survey", () -> {
                get(ctx -> {
                    ctx.render("/local/design/html/surveyform.html");
                });
            });
        });

        webSocket();
    }

    private void webSocket() {
        app.ws("/", ws -> {

            ws.onConnect(ctx -> {
                socketUsers.add(ctx.session);
            });

            ws.onMessage(ctx -> {
                ObjectMapper mapper = new ObjectMapper();
                List<Form> forms = mapper.readValue(ctx.message(), new TypeReference<List<Form>>() {
                });

                survey.submit(forms);

                broadcast(ctx.message());
            });

            ws.onBinaryMessage(ctx -> {
            });

            ws.onClose(ctx -> {
                socketUsers.remove(ctx.session);
            });

            ws.onError(ctx -> {
            });
        });
    }

    private void broadcast(String message) {
        for (Session session : socketUsers) {
            try {
                session.getRemote().sendString(":'D");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}