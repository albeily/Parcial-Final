package edu.pucmm.survey.api.soap;

import com.sun.net.httpserver.HttpContext;
import edu.pucmm.survey.controller.Survey;
import edu.pucmm.survey.utils.BaseHandler;
import io.javalin.Javalin;
import org.eclipse.jetty.http.spi.HttpSpiContextHandler;
import org.eclipse.jetty.http.spi.JettyHttpContext;
import org.eclipse.jetty.http.spi.JettyHttpServer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;

import javax.xml.ws.Endpoint;
import java.lang.reflect.Method;

public class Soap extends BaseHandler {

    public Soap(Javalin app, Survey survey) {
        super(app, survey);
    }

    @Override
    public void routes() {
        Server server = app.server().server();
        ContextHandlerCollection contextHandlerCollection = new ContextHandlerCollection();
        server.setHandler(contextHandlerCollection);

        try {
            HttpContext context = build(server, "/api/soap/form");

            FormWebServices wsa = new FormWebServices(survey);
            Endpoint endpoint = Endpoint.create(wsa);
            endpoint.publish(context);
            // http://localhost:7000/api/soap/FormWebServices?wsdl
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private HttpContext build(Server server, String contextString) throws Exception {
        JettyHttpServer jettyHttpServer = new JettyHttpServer(server, true);
        JettyHttpContext ctx = (JettyHttpContext) jettyHttpServer.createContext(contextString);
        Method method = JettyHttpContext.class.getDeclaredMethod("getJettyContextHandler");
        method.setAccessible(true);
        HttpSpiContextHandler contextHandler = (HttpSpiContextHandler) method.invoke(ctx);
        contextHandler.start();

        return ctx;
    }
}