package edu.pucmm.survey.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.pucmm.survey.controller.Survey;
import edu.pucmm.survey.entity.Form;
import edu.pucmm.survey.entity.Person;
import edu.pucmm.survey.entity.User;
import edu.pucmm.survey.handler.command.Command;
import edu.pucmm.survey.handler.command.CommandMethod;
import edu.pucmm.survey.handler.message.Message;
import edu.pucmm.survey.handler.message.MessageType;
import edu.pucmm.survey.utils.BaseHandler;
import edu.pucmm.survey.utils.RoleType;
import io.javalin.Javalin;
import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.javalin.apibuilder.ApiBuilder.*;

public class AdminHandler extends BaseHandler {

    private final List<Session> sessions;

    public AdminHandler(Javalin app, Survey survey) {
        super(app, survey);
        this.sessions = new ArrayList<>();
    }

    @Override
    public void routes() {

        app.before("/admin", ctx -> {
            User user = ctx.sessionAttribute("user");

            if (user == null) {
                ctx.redirect("/account/login");
            } else if (user.getRole() != RoleType.ADMIN) {
                ctx.redirect("/account/login");
            }
        });

        app.routes(() -> {
            path("/admin", () -> {
                get(ctx -> {
                    Map<String, Object> model = new HashMap<>();

                    model.put("users", survey.getUsers());
                    model.put("forms", survey.getForms());

                    ctx.render("/local/design/html/admin.html", model);
                });

                form();
                account();
                socket();
            });
        });
    }

    private void socket() {
        ws(ws -> {

            ws.onConnect(ctx -> {
                sessions.add(ctx.session);
            });

            ws.onMessage(ctx -> {
                JSONArray objects = new JSONArray(ctx.message());
                String type = objects.getString(0);
                ObjectMapper mapper = new ObjectMapper();
                Command<?> command = null;
                String message = "";

                switch (type) {
                    case "FORM":
                        command = mapper.readValue(objects.get(1).toString(), new TypeReference<Command<Form>>() {
                        });
                        command.execute(survey);
                        message = mapper.writeValueAsString(new Message(MessageType.FORM, new Command<>(command.getMethod(), survey.getForms())));
                        break;
                    case "USER":
                        command = mapper.readValue(objects.get(1).toString(), new TypeReference<Command<User>>() {
                        });
                        command.execute(survey);
                        message = mapper.writeValueAsString(new Message(MessageType.USER, new Command<>(command.getMethod(), survey.getUsersDecrypted())));
                        break;
                    default:
                        message = "";
                        break;
                }

                if (command.getMethod() == CommandMethod.LIST) {
                    ctx.send(message);
                } else {
                    broadcast(message);
                }
            });

            ws.onBinaryMessage(ctx -> {
            });

            ws.onClose(ctx -> {
                sessions.remove(ctx.session);
            });

            ws.onError(ctx -> {
            });
        });
    }

    private void broadcast(String message) {
        for (Session session : sessions) {
            try {
                session.getRemote().sendString(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void form() {

    }

    private void account() {
        path("/account", () -> {

            post("/submit", ctx -> {
                Person person;
                User user;

                String name = ctx.formParam("name");
                String lastname = ctx.formParam("lastname");
                String username = ctx.formParam("username");
                String password = ctx.formParam("password");
                RoleType role = RoleType.valueOf(ctx.formParam("role"));

                person = new Person(name, lastname);
                user = new User(username, password, person, role);

                survey.submit(user);

                ctx.redirect("/admin");
            });
        });
    }
}