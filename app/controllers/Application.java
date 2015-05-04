package controllers;

import java.util.List;

import models.Comment;
import play.Logger;
import play.Play;
import play.data.DynamicForm;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class Application extends Controller {

    /**
     *
     * @return
     */
    public static int getMaxCommentLength() {
        return Integer.parseInt(Play.application().configuration().getString("comment.maxLength"));
    }

    /**
     *
     * @return
     */
    public static Result index() {
        Logger.debug("[GET] ({})index", request().remoteAddress());

        return ok(views.html.index.render(""));
    }

    /**
     *
     * @param threadID
     * @param year
     * @param month
     * @param day
     * @param hour
     * @param minute
     * @return
     */
    public static Result getCommentsByThreadID(String threadID,
                                               Integer year,
                                               Integer month,
                                               Integer day,
                                               Integer hour,
                                               Integer minute,
                                               String last) {
        Logger.debug("[GET] ({})threadID={}, yyyy={}, mm={}, dd={}, hh={}, mi={}, last={}", request().remoteAddress(), threadID, year, month, day, hour, minute, last);

        if (isValidAjaxRequest()) {
            List<Comment> list = Comment.getComments(threadID, year, month, day, hour, minute, last);
            return ok(Json.toJson(list));
        }
        else {
            return badRequest("400:BadRequest");
        }
    }

    /**
     *
     * @param CommentID
     * @return
     */
    public static Result getCommentByCommentID(String CommentID) {
        Logger.debug("[GET] ({})CommentID={}", request().remoteAddress(), CommentID);

        if (isValidAjaxRequest()) {
            return ok();
        }
        else {
            return badRequest("400:BadRequest");
        }
    }

    /**
     *
     * @param threadID
     * @return
     */
    public static Result postComment(String threadID) {

        if (isValidAjaxRequest()) {
            DynamicForm form =  new DynamicForm().bindFromRequest();
            String postComment = form.get("comment");
            Logger.debug("[POST] ({})threadID={}, Comment={}", request().remoteAddress(), threadID, postComment);

            Comment newComment = new Comment(threadID, postComment);
            newComment.save();

            return ok();
        }
        else {
            return badRequest("400:BadRequest");
        }
    }
    /**
     *
     * @param CommentID
     * @return
     */
    public static Result putComment(String CommentID) {
        Logger.debug("[PUT] ({})threadID={}", request().remoteAddress(), CommentID);

        if (isValidAjaxRequest()) {
            return ok();
        }
        else {
            return badRequest("400:BadRequest");
        }
    }
    /**
     *
     * @param CommentID
     * @return
     */
    public static Result deleteComment(String CommentID) {
        Logger.debug("[DELETE] ({})threadID={}", request().remoteAddress(), CommentID);

        if (isValidAjaxRequest()) {
            return ok();
        }
        else {
            return badRequest("400:BadRequest");
        }
    }

    /**
     *
     * @return
     */
    public static boolean isValidAjaxRequest() {
        String xRequestedWith = request().getHeader("X-requested-with");
        return xRequestedWith != null;
    }

}
