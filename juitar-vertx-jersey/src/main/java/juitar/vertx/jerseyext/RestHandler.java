package juitar.vertx.jerseyext;

import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.spi.container.WebApplication;
import com.sun.jersey.spi.container.WebApplicationFactory;
import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServerRequest;

import java.net.URI;

public class RestHandler implements Handler<HttpServerRequest> {

    WebApplication app;

    URI baseUri;

    public RestHandler(URI baseURI, String packageName) {
        this.baseUri = baseURI;
        this.app = WebApplicationFactory.createWebApplication();
        this.app.initiate(new PackagesResourceConfig(packageName));
    }

    public RestHandler(WebApplication app, URI baseUri) {
        this.app = app;
        this.baseUri = baseUri;
    }

    @Override
    public void handle(HttpServerRequest req) {
        new RestRequestHandler(baseUri, app).handle(req);
    }


}
