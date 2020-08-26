package edu.pucmm.survey.api;

import edu.pucmm.survey.controller.Survey;
import edu.pucmm.survey.entity.Form;
import edu.pucmm.survey.utils.BaseHandler;
import io.javalin.Javalin;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Rest extends BaseHandler {

    private static final String SECRET_KEY = "ADMIN";

    public Rest(Javalin app, Survey survey) {
        super(app, survey);
    }

    @Override
    public void routes() {
        app.routes(() -> {
            path("/api/rest", () -> {
                get(ctx -> {
                    ctx.render("/local/rest/html/client.html");
                });

                path("/form", () -> {
                    before(ctx -> {

                    });

                    after(ctx -> {
                        ctx.header("Content-Type", "application/json");
                    });

                    get(ctx -> {
                        if(ctx.cookie("token")!=null){
                            if(ctx.cookie("token").equals(ctx.req.getHeader("token"))) {
                                ctx.header("Authorization",ctx.cookie("token"));
                                ctx.json(survey.getForms());
                            }  }

                    });

                    post(ctx -> {
                        if(ctx.cookie("token")!=null){
                            if(ctx.cookie("token").equals(ctx.req.getHeader("token"))) {
                                ctx.header("Authorization",ctx.cookie("token"));
                                Form form = ctx.bodyAsClass(Form.class);
                                ctx.json(survey.submit(form));
                            }  }
                    });

                    path("/verify", () -> {
                        get(ctx -> {

                            if (ctx.req.getHeader("client").equalsIgnoreCase("admin")) {
                                System.out.println(ctx.res.getHeader("client"));
                                ctx.header("Authorization", createJWT("user"));
                                ctx.cookie("token",ctx.res.getHeader("Authorization"));
                            } else {
                                ctx.header("Authorization", "denied");
                            }
                        });
                    });
                });
            });

            path("/api/soap", () -> {
                get(ctx -> {
                    ctx.render("/local/soap/resources/html/client.html");
                });
            });
        });
    }

    public String createJWT(String username) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        JwtBuilder builder = Jwts.builder().setId(username)
                .setIssuedAt(now)
                .setSubject("PWEB")
                .setIssuer("PUCMM")
                .signWith(signatureAlgorithm, signingKey);
        long expMillis = nowMillis + 300000;
        Date exp = new Date(expMillis);
        builder.setExpiration(exp);

        return builder.compact();
    }

    public Claims decodeJWT(String jwt) {
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                .parseClaimsJws(jwt).getBody();
        return claims;
    }
}