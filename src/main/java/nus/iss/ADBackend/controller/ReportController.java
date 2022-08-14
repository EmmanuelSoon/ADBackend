package nus.iss.ADBackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nus.iss.ADBackend.Service.CommentService;
import nus.iss.ADBackend.Service.RecipeService;
import nus.iss.ADBackend.Service.ReportService;
import nus.iss.ADBackend.Service.UserService;
import nus.iss.ADBackend.helper.ReportForm;
import nus.iss.ADBackend.model.Recipe;
import nus.iss.ADBackend.model.Report;
import nus.iss.ADBackend.model.ReportCategory;
import nus.iss.ADBackend.model.User;

@RestController
@RequestMapping(value = "/report", produces = "application/json")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ReportController {
	@Autowired
	private RecipeService recipeService;
	@Autowired
	private CommentService commentService;
	@Autowired
	private UserService userService;
	@Autowired
	private ReportService reportService;

	@PostMapping("/create")
	public ResponseEntity createComment(@RequestBody ReportForm reportForm) {
		User u = userService.findUserById(reportForm.getUserId());
		Recipe recipe = recipeService.findRecipeById(reportForm.getRecipeId());
		String content = reportForm.getContent();
		ReportCategory reportCategory = ReportCategory.values()[reportForm.getCategoryId()];
		if (u == null || recipe == null) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		Report report = new Report(content, u, recipe, reportCategory);
		reportService.createReport(report);
		return new ResponseEntity<>(null, HttpStatus.OK);
	}
}
