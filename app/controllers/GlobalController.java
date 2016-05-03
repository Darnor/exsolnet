package controllers;

import com.google.inject.Inject;
import play.Configuration;
import play.Environment;
import play.Logger;
import play.api.OptionalSourceMapper;
import play.api.routing.Router;
import play.http.DefaultHttpErrorHandler;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import services.SessionService;
import views.html.error500;

import javax.inject.Provider;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static play.mvc.Results.notFound;

public class GlobalController extends DefaultHttpErrorHandler{

    @Inject
    public GlobalController(Configuration configuration, Environment environment, OptionalSourceMapper sourceMapper, Provider<Router> routes) {
        super(configuration, environment, sourceMapper, routes);
    }

    @Override
    public CompletionStage<Result> onClientError(Http.RequestHeader request, int statusCode, String message) {
        return CompletableFuture.completedFuture(
               notFound(views.html.error404.render(SessionService.getCurrentUser(), "Seite nicht gefunden"))
        );
    }

    @Override
    public CompletionStage<Result> onServerError(Http.RequestHeader request, Throwable exception) {
        Logger.error("500", exception);
        return CompletableFuture.completedFuture(
                Results.internalServerError(error500.render(SessionService.getCurrentUser(), "Ein interner Fehler ist aufgetreten"))
        );
    }
}
