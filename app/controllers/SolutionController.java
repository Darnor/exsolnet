package controllers;

import models.Solution;
import models.User;
import models.Vote;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import services.SessionService;

@Security.Authenticated(Secured.class)
public class SolutionController extends Controller {

    public Result processUpvote(Long solutionId) {
        Logger.info("Up Vote Solution " + solutionId);
        Solution solution = Solution.findById(solutionId);
        Vote.upvote(SessionService.getCurrentUser(), solution);
        return ok(String.valueOf(Solution.findById(solutionId).getPoints()));
    }

    public Result processDownvote(Long solutionId) {
        Logger.info("Down Vote Solution " + solutionId);
        Solution solution = Solution.findById(solutionId);
        Vote.downvote(SessionService.getCurrentUser(), solution);
        return ok(String.valueOf(Solution.findById(solutionId).getPoints()));
    }

    /**
     * deletes a solution cascading!
     *
     * @param id id of to deleting solution
     * @return ok if solution has been deleted or unauthorized if user is not allowed to delete this solution
     */
    public Result processDelete(Long id) {
        User currentUser = SessionService.getCurrentUser();
        if (currentUser.isModerator() || currentUser.getId().equals(Solution.findById(id).getUser().getId())) {
            Solution.delete(id);
            Logger.info("Solution " + id + " deleted by " + currentUser.getEmail());
            return ok("Solution deleted");
        }
        return unauthorized("not allowed");
    }
}
