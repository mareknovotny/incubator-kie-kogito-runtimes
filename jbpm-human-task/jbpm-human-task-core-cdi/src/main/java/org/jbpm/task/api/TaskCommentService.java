/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jbpm.task.api;

import java.util.List;
import org.jbpm.task.Comment;

/**
 *
 * @author salaboy
 */
public interface TaskCommentService {

    long addComment(long taskId, Comment comment);

    void deleteComment(long taskId, long commentId);

    List<Comment> getComments(long taskId);

    Comment getCommentById(long commentId);
}
