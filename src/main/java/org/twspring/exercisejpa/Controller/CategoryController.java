package org.twspring.exercisejpa.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.twspring.exercisejpa.Api.ApiResponse;
import org.twspring.exercisejpa.Model.Category;
import org.twspring.exercisejpa.Service.CategoryService;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/category")
public class CategoryController {
    private final CategoryService categoryService;

    //=======================================GET=======================================

    @GetMapping("get/categories")
    public ResponseEntity getCategories() {
        if (categoryService.getCategories().isEmpty()) {
            return ResponseEntity.status(404).body(new ApiResponse("No categories found"));
        }
        return ResponseEntity.status(200).body(categoryService.getCategories());
    }

    @GetMapping("/get/category/{id}")
    public ResponseEntity getCategory(@PathVariable Integer id) {
        Category category = categoryService.getCategory(id);
        if (category == null) {
            return ResponseEntity.status(404).body(new ApiResponse("No category found"));
        }
        return ResponseEntity.status(200).body(category);
    }

    //=======================================POST=======================================

    @PostMapping("add/category")
    public ResponseEntity addCategory(@Valid @RequestBody Category category, Errors errors){
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }
        categoryService.addCategory(category);
        return ResponseEntity.status(201).body(new ApiResponse("Category added successfully"));
    }

    //FOR TESTS
    @PostMapping("add/categories")
    public ResponseEntity addCategories(){
        Category category1 = new Category(1,"Clothes");
        Category category2 = new Category(2,"Technology");
        Category category3 = new Category(3,"Beauty");
        categoryService.addCategory(category1);
        categoryService.addCategory(category2);
        categoryService.addCategory(category3);
        return ResponseEntity.status(201).body(new ApiResponse("Category added successfully"));
    }

    //=======================================UPDATE=======================================
    @PutMapping("update/category/{id}")
    public ResponseEntity updateCategory(@PathVariable int id,@Valid @RequestBody Category category, Errors errors){
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }
        boolean isUpdated = categoryService.updateCategory(id, category);
        if (isUpdated) {
            return ResponseEntity.status(201).body(new ApiResponse("Category updated successfully"));
        }
        return ResponseEntity.status(404).body(new ApiResponse("No category with ID "+id+" found"));
    }
    //=======================================DELETE=======================================

    @DeleteMapping("delete/category/{id}")
    public ResponseEntity deleteCategory(@PathVariable int id){
        boolean isDeleted = categoryService.deleteCategory(id);
        if (isDeleted) {
            return ResponseEntity.status(201).body(new ApiResponse("Category deleted successfully"));
        }
        return ResponseEntity.status(404).body(new ApiResponse("No category with ID "+id+" found"));
    }
}
