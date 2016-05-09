package controllers;

import models.Comment;
import models.Exercise;
import models.Solution;
import play.Logger;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import services.SessionService;

import javax.inject.Inject;

@Security.Authenticated(Secured.class)
public class CommentController extends Controller {

    private static final String CONTENT_FIELD = "content";

    @Inject
    FormFactory formFactory;

    public Result createExerciseComment(Long exerciseId) {
        Comment.create(formFactory.form().bindFromRequest().get(CONTENT_FIELD), Exercise.findById(exerciseId), SessionService.getCurrentUser());
        return redirect(routes.ExerciseController.renderDetail(exerciseId));
    }

    public Result createSolutionComment(Long solutionId) {
        Solution solution = Solution.findById(solutionId);
        Comment.create(formFactory.form().bindFromRequest().get(CONTENT_FIELD), solution, SessionService.getCurrentUser());
        return redirect(routes.ExerciseController.renderDetail(solution.getExercise().getId()));
    }

    public Result updateComment(Long commentId) {
        Comment comment = Comment.findById(commentId);
        if(comment.getUser().getId().equals(SessionService.getCurrentUser().getId())){
            Comment.updateContent(commentId, formFactory.form().bindFromRequest().get(CONTENT_FIELD));
            Logger.debug("Comment: "+commentId+" updated with content: "+formFactory.form().bindFromRequest().get(CONTENT_FIELD));
        }
        else{
            Logger.error("User: "+SessionService.getCurrentUser().getId()+" tried to edit comment: "+commentId+" from user: "+comment.getUser().getId());
        }
        if(comment.getExercise()!=null)
            return redirect(routes.ExerciseController.renderDetail(comment.getExercise().getId()));
        if(comment.getSolution() != null)
            return redirect(routes.ExerciseController.renderDetail(comment.getSolution().getExercise().getId()));
        return null;
    }
}
