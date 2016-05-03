package controllers;

import models.Solution;
import models.Vote;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import services.SessionService;

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
}
