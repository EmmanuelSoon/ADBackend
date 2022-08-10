package nus.iss.ADBackend.Service;

import nus.iss.ADBackend.Repo.CommentRepository;
import nus.iss.ADBackend.model.Comment;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.ls.LSInput;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    CommentRepository cRepo;

    public void createComment(Comment comment) {
        cRepo.saveAndFlush(comment);
    }
    public boolean saveComment(Comment comment) {
        if (cRepo.findById(comment.getId()) != null) {
            cRepo.saveAndFlush(comment);
            return true;
        }
        return false;
    }

    public List<Comment> findCommentsByUserId(int id) {
        return cRepo.findAllByUserId(id);
    }
    public List<Comment> findCommentsByRecipeId(int recipeId) {
        return cRepo.findAllByRecipeId(recipeId);
    }
    public Comment findCommentByID(int id) {
        return cRepo.findById(id);
    }
    public Comment findCommentByUserIdAndDateTime(int userId, LocalDateTime dateTime) {
        return cRepo.findByUserIdAndDateTime(userId, dateTime);
    }
    @Transactional
    public void deleteCommentById(int id) {
        cRepo.deleteById(id);
    }
}
