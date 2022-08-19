package nus.iss.ADBackend.controller;

import nus.iss.ADBackend.Service.CommentService;
import nus.iss.ADBackend.Service.IngredientService;
import nus.iss.ADBackend.Service.RecipeService;
import nus.iss.ADBackend.Service.UserService;
import nus.iss.ADBackend.helper.CommentForm;
import nus.iss.ADBackend.model.Comment;
import nus.iss.ADBackend.model.Recipe;
import nus.iss.ADBackend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@RestController
@RequestMapping(value= "/comment", produces = "application/json")
@CrossOrigin(origins = "http://localhost:3000")
public class CommentController {
    @Autowired
    private RecipeService recipeService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserService userService;

    @GetMapping("/recipe/{id}")
    public List<Comment> getCommentsByRecipeId(@PathVariable int id) {
        return commentService.findCommentsByRecipeId(id);
    }
    @PostMapping ("/create")
    public ResponseEntity createComment(@RequestBody CommentForm commentForm) {
        User u = userService.findUserById(commentForm.getUserId());
        Recipe recipe = recipeService.findRecipeById(commentForm.getRecipeId());
        if (u == null || recipe == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        String content = commentForm.getContent();
        double rating = commentForm.getRating();

        Comment comment = new Comment(rating, content, u, LocalDateTime.now(ZoneId.of("Asia/Singapore")), recipe);
        commentService.createComment(comment);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public List<Comment> getCommentsByUserId(@PathVariable int id) {
        return commentService.findCommentsByUserId(id);
    }
    @GetMapping("/delete/{id}")
    public void deleteCommentsById(@PathVariable int id) {
        commentService.deleteCommentById(id);
    }
}
