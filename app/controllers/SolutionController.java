package controllers;

import models.Exercise;
import models.Solution;
import models.Vote;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import services.SessionService;

@Security.Authenticated(Secured.class)
public class SolutionController extends Controller {

    public Result upVote(Long solutionId) {
        Logger.info("Up Vote Solution " + solutionId);
        Solution solution = Solution.findById(solutionId);
        Vote.upvote(SessionService.getCurrentUser(),solution);
        return ok(String.valueOf(Solution.findById(solutionId).getPoints()));
    }

    public Result downVote(Long solutionId) {
        Logger.info("Down Vote Solution " + solutionId);
        Solution solution = Solution.findById(solutionId);
        Vote.downvote(SessionService.getCurrentUser(),solution);
        return ok(String.valueOf(Solution.findById(solutionId).getPoints()));
    }

    /**
     * deletes a solution cascading!
     *
     * @param id id of to deleting solution
     * @return
     */
    public Result delete(Long id) {
        Solution.delete(id);
        Logger.info("Soltution " + id +" deleted by " + SessionService.getCurrentUserEmail());
        return ok("Exercise deleted");
    }
}
