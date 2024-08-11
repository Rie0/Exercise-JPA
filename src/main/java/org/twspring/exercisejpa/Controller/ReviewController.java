package org.twspring.exercisejpa.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.twspring.exercisejpa.Api.ApiResponse;
import org.twspring.exercisejpa.Model.Review;
import org.twspring.exercisejpa.Service.ReviewService;

@RestController
@RequestMapping("api/v1/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    //=======================================GET=======================================
    @GetMapping("/get/reviews")
    public ResponseEntity getReviews(){
        if (reviewService.getReviews().isEmpty()){
            return ResponseEntity.status(404).body(new ApiResponse("No reviews found"));
        }
        return ResponseEntity.status(200).body(reviewService.getReviews());
    }

    @GetMapping("/get/review/{id}")
    public ResponseEntity getReview(@PathVariable int id){
        if (reviewService.getReview(id)==null){
            return ResponseEntity.status(404).body(new ApiResponse("No review found"));
        }
        return ResponseEntity.status(200).body(reviewService.getReview(id));
    }

    @GetMapping("/get/reviews/by_product/{productId}")
    public ResponseEntity getProductReviews(@PathVariable int productId){
        if (reviewService.getReviews().isEmpty()){
            return ResponseEntity.status(404).body(new ApiResponse("No reviews found"));
        }
        return ResponseEntity.status(200).body(reviewService.getProductReviews(productId));
    }

    //=======================================POST=======================================
    @PostMapping("/add/review")
    public ResponseEntity addReview(@Valid @RequestBody Review review, Errors errors){
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }
        int flag = reviewService.addReview(review);
        switch (flag){
            case 0:
                return ResponseEntity.status(201).body(new ApiResponse("Review added successfully"));
            case 1:
                return ResponseEntity.status(400).body(new ApiResponse("User with ID " + review.getUserId() + " doesn't exists"));
            case 2:
                return ResponseEntity.status(400).body(new ApiResponse("Product with ID " + review.getProductId() + " doesn't exists"));
            default:
                return ResponseEntity.status(400).body(new ApiResponse("An error occurred"));
        }
    }

    //FOR TESTING
    @PostMapping("/add/reviews")
    public ResponseEntity addReviews(){
        Review review1 = new Review(1,1,1,4,"Good stuff");
        Review review2 = new Review(2,2,1,5,"Good stuff");
        Review review3 = new Review(3,3,1,2,"Broken");
        Review review4 = new Review(4,1,3,4,"Good stuff");

        reviewService.addReview(review1);
        reviewService.addReview(review2);
        reviewService.addReview(review3);
        reviewService.addReview(review4);
        return ResponseEntity.status(201).body(new ApiResponse("Review added successfully"));
    }
    //=======================================UPDATE=======================================
    @PutMapping("/update/review/{id}")
    public ResponseEntity updateReview(@PathVariable Integer id, @Valid @RequestBody Review review, Errors errors){
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }
        int flag = reviewService.updateReview(id, review);
        switch (flag){
            case 0:
                return ResponseEntity.status(200).body(new ApiResponse("Review updated successfully"));
            case 1:
                return ResponseEntity.status(400).body(new ApiResponse("User with ID" + review.getUserId() + " doesn't exists"));
            case 2:
                return ResponseEntity.status(400).body(new ApiResponse("Product with ID" + review.getProductId() + " doesn't exists"));
            case 3:
                return ResponseEntity.status(400).body(new ApiResponse("Invalid; you can't change the product of the review"));
            case 4:
                return ResponseEntity.status(400).body(new ApiResponse("Invalid; you can't change the user of the review"));
            case 5:
                return ResponseEntity.status(400).body(new ApiResponse("Review with ID "+review.getId()+" doesn't exists"));
            default:
                return ResponseEntity.status(400).body(new ApiResponse("An error occurred"));
        }

    }

    //=======================================DELETE=======================================
    @DeleteMapping("/delete/review/{id}")
    public ResponseEntity deleteReview(@PathVariable Integer id){
        if (reviewService.deleteReview(id)){
            return ResponseEntity.status(200).body(new ApiResponse("Review deleted successfully"));
        }
        return ResponseEntity.status(404).body(new ApiResponse("No review found"));
    }
}
