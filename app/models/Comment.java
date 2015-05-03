package models;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.Play;
import play.db.ebean.Model;

import com.avaje.ebean.ExpressionList;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author ikd9684
 */
@Entity
public class Comment extends Model {
    /** */
    private static final long serialVersionUID = 1253311865414920077L;

    /** UUID生成のための"名前" */
    private static String UUID_NAME = Play.application().configuration().getString("application.name");

    /** */
    public static final SimpleDateFormat SDF_WITH_SLASH = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
    /** */
    public static final SimpleDateFormat SDF_WITHOUT_SLASH = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    /** */
    public static final DecimalFormat DF4 = new DecimalFormat("0000");
    /** */
    public static final DecimalFormat DF2 = new DecimalFormat("00");

    /** */
    public static Finder<String, Comment> finder = new Finder<String, Comment>(String.class, Comment.class);

    @Id
    @JsonProperty("comment_id")
    public String comment_id;

    @JsonProperty("thread_id")
    public String thread_id;

    @JsonProperty("comment")
    public String comment;

    @JsonProperty("timestamp")
    public String timestamp;

    public Comment(String thread_id_, String comment_) {
        this.thread_id = thread_id_;
        this.comment = comment_;

        Date now = new Date();
        this.timestamp = SDF_WITH_SLASH.format(now);
        this.comment_id = getNewUUID();
    }

    /**
     *
     * @return
     */
    private static String getNewUUID() {
        String name = UUID_NAME + SDF_WITHOUT_SLASH.format(new Date());
        return UUID.nameUUIDFromBytes(name.getBytes()).toString();
    }

    /**
    *
    * @param thread_id
    * @param year
    * @param month
    * @param day
    * @param hour
    * @param minute
    * @param last
    * @return
    */
   public static List<Comment> getComments(String thread_id,
                                           Integer year,
                                           Integer month,
                                           Integer day,
                                           Integer hour,
                                           Integer minute,
                                           String last) {

       ExpressionList<Comment> exList = Comment.finder.orderBy("timestamp").where();

       if (thread_id != null) {
           exList = exList.eq("thread_id", thread_id);
       }

       if (year != null) {
           String targetTimestamp = DF4.format(year);

           if (month != null) {
               targetTimestamp += "/" + DF2.format(month);
           }
           if (day != null) {
               targetTimestamp += "/" + DF2.format(day);
           }
           if (hour != null) {
               targetTimestamp += " " + DF2.format(hour);
           }
           if (minute != null) {
               targetTimestamp += ":" + DF2.format(minute);
           }

           exList = exList.like("timestamp", targetTimestamp + "%");
       }

       if (last != null) {
           exList = exList.gt("timestamp", last);
       }

       return exList.findList();
    }

    /**
     *
     */
    @Override
    public String toString() {
        return String.format("Comment[commentID={0}, threadID={1}, timestamp={2}, comment={3}]", comment_id, thread_id, timestamp, comment);
    }

}
