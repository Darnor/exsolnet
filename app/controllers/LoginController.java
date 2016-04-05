package controllers;

import com.google.inject.Inject;
import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import repositories.UserRepository;
import services.SessionService;

/**
 * Created by mario on 05.04.16.
 */
public class LoginController extends Controller {

    @Inject
    UserRepository userRepository;

    @Inject
    SessionService sessionService;

    @Inject
    FormFactory formFactory;

    public Result renderLogin(){
            return ok(views.html.login.render(session("connected")));
    }

    public Result login() {
        Form<User> userForm = formFactory.form(User.class);
        User user = userForm.bindFromRequest().get();
        if(userRepository.authenticate(user.getEmail(), user.getPassword()) != null){
            sessionService.set(user.getEmail());
        }
        return redirect(routes.HomeController.index());
    }

    public Result logout() {
        if(sessionService.isLoggedin()){
            sessionService.clear();
        }
        return redirect(routes.HomeController.index());
    }
}
