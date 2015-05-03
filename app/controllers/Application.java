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
        Logger.debug("[GET] index");

        return ok(views.html.index.render(""));
    }

    /**
     *
     * @param year
     * @param month
     * @param day
     * @param hour
     * @param minute
     * @return
     */
    public static Result getCommentsByDate(Integer year,
                                           Integer month,
                                           Integer day,
                                           Integer hour,
                                           Integer minute,
                                           String last) {
        Logger.debug("[GET] yyyy={}, mm={}, dd={}, hh={}, mi={}, last={}", year, month, day, hour, minute, last);

        List<Comment> list = Comment.getComments(null, year, month, day, hour, minute, last);
        return ok(Json.toJson(list));
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
        Logger.debug("[GET] threadID={}, yyyy={}, mm={}, dd={}, hh={}, mi={}, last={}", threadID, year, month, day, hour, minute, last);

        String xRequestedWith = request().getHeader("X-requested-with");
        Logger.debug("X-requested-with={}", xRequestedWith);

        List<Comment> list = Comment.getComments(threadID, year, month, day, hour, minute, last);
        return ok(Json.toJson(list));
    }

    /**
     *
     * @param CommentID
     * @return
     */
    public static Result getCommentByCommentID(String CommentID) {
        Logger.debug("[GET] CommentID={}", CommentID);

        return ok();
    }

    /**
     *
     * @param threadID
     * @return
     */
    public static Result postComment(String threadID) {
        DynamicForm form =  new DynamicForm().bindFromRequest();
        String postComment = form.get("comment");
        Logger.debug("[POST] threadID={}, Comment={}", threadID, postComment);

        Comment newComment = new Comment(threadID, postComment);
        newComment.save();

        return ok();
    }
    /**
     *
     * @param CommentID
     * @return
     */
    public static Result putComment(String CommentID) {
        Logger.debug("[PUT] threadID={}", CommentID);

        return ok();
    }
    /**
     *
     * @param CommentID
     * @return
     */
    public static Result deleteComment(String CommentID) {
        Logger.debug("[DELETE] threadID={}", CommentID);

        return ok();
    }

}
