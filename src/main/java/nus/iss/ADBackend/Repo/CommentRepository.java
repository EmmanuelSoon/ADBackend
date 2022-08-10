package nus.iss.ADBackend.Repo;

import nus.iss.ADBackend.model.Comment;
import nus.iss.ADBackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {


    @Query("select c from Comment c where c.user.id = ?1 and c.dateTime = ?2")
    Comment findByUserIdAndDateTime(int id, LocalDateTime time);
    @Query("select c from Comment c where c.recipe.id = ?1 order by c.dateTime desc")
    List<Comment> findAllByRecipeId(int id);
    @Modifying
    @Query("delete from Comment c where c.recipe.id = ?1")
    void deleteAllByRecipeId(int id);
    @Modifying
    @Query("delete from Comment c where c.id = ?1")
    void deleteById(int id);
    @Query("select c from Comment  c where c.user.id = ?1 order by c.dateTime desc")
    List<Comment> findAllByUserId(int userId);
    Comment findById(int id);
}
