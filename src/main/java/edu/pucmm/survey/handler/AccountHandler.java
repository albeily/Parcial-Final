package edu.pucmm.survey.handler;

import edu.pucmm.survey.controller.Survey;
import edu.pucmm.survey.entity.User;
import edu.pucmm.survey.utils.BaseHandler;
import io.javalin.Javalin;

import static io.javalin.apibuilder.ApiBuilder.*;

public class AccountHandler extends BaseHandler {

    private final int rememberTime;

    public AccountHandler(Javalin app, Survey survey) {
        super(app, survey);
        this.rememberTime = 604800;
    }

    @Override
    public void routes() {

        app.routes(() -> {
            path("/account", () -> {
                get(ctx -> {
                    User user = (User) ctx.sessionAttribute("user");
                    System.out.println(":)");

                    if (user != null) {
                        ctx.redirect("/");
                    } else {
                        ctx.redirect("/account/login");
                    }
                });

                get("/login", ctx -> {
                    User user = (User) ctx.sessionAttribute("user");

                    if (user != null) {
                        if (survey.existUser(user.getUsername())) {
                            ctx.redirect("/");
                        }
                    } else {
                        ctx.render("/local/design/html/login.html");
                    }
                });

                post("/login", ctx -> {
                    String username = ctx.formParam("username");
                    String password = ctx.formParam("password");
                    boolean remember = ctx.formParam("remember") != null;

                    User user = survey.auth(username, password, false);

                    if (user != null) {
                        ctx.sessionAttribute("user", user);

                        if (remember) {
                            ctx.cookie("UID", username, rememberTime);
                            ctx.cookie("UPA", user.getPassword(), rememberTime);
                        } else {
                            ctx.cookie("UID", username, 5000);
                            ctx.cookie("UPA", user.getPassword(), 5000);
                        }

                        ctx.redirect("/");
                    } else {
                        ctx.redirect("/account/login");
                    }
                });

                get("/logout", ctx -> {
                    ctx.sessionAttribute("user", null);
                    ctx.cookie("UID", "", 0);
                    ctx.cookie("UPA", "", 0);
                    ctx.redirect("/");
                });
            });
        });
    }
}