package edu.pucmm.survey.handler;

import edu.pucmm.survey.controller.Survey;
import edu.pucmm.survey.entity.User;
import edu.pucmm.survey.utils.BaseHandler;
import edu.pucmm.survey.utils.RoleType;
import io.javalin.Javalin;
import org.jasypt.util.text.AES256TextEncryptor;

public class Handler extends BaseHandler {

    private final AES256TextEncryptor encryptor;

    public Handler(Javalin app, Survey survey, AES256TextEncryptor encryptor) {
        super(app, survey);
        this.encryptor = encryptor;
    }

    @Override
    public void routes() {
        app.before(ctx -> {
            User user = ctx.sessionAttribute("user");
            String username;
            String password;

            if (user == null) {
                username = ctx.cookie("UID");
                password = ctx.cookie("UPA");

                if (username != null && password != null) {
                    if (!((username + password).isBlank() || (username + password).isEmpty())) {
                        user = survey.auth(username, password, true);

                        if (user != null) {
                            ctx.sessionAttribute("user", user);
                        }
                    }
                }
            }
        });

        app.get("/", ctx -> {
            User user = ctx.sessionAttribute("user");

            if (user != null) {
                if (user.getRole() == RoleType.ADMIN) {
                    ctx.redirect("/admin");
                } else if (user.getRole() == RoleType.USER) {
                    ctx.redirect("/survey");
                } else {
                    ctx.redirect("/account/login");
                }
            } else {
                ctx.redirect("/account/login");
            }
        });

        initialize();
    }

    private void initialize() {
        new AdminHandler(app, survey).routes();
        new FormHandler(app, survey).routes();
        new AccountHandler(app, survey).routes();
    }
}