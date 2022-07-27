package nus.iss.ADBackend.Repo;

import nus.iss.ADBackend.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    @Query("select c from Comment c where c.user.id = ?1 and c.dateTime = ?2")
    Comment findByUserIdAndDateTime(int id, LocalDateTime time);
    @Query("select c from Comment c where c.recipe.id = ?1")
    List<Comment> findAllByRecipeId(int id);

    @Modifying
    @Query("delete from Comment c where c.recipe.id = ?1")
    void deleteAllByRecipeId(int id);
}
