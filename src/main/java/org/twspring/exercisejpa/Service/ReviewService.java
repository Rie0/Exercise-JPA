package org.twspring.exercisejpa.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.twspring.exercisejpa.Model.Product;
import org.twspring.exercisejpa.Model.Review;
import org.twspring.exercisejpa.Repository.ProductRepository;
import org.twspring.exercisejpa.Repository.ReviewRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService{

    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final ProductService productService;
    private final ProductRepository productRepository;

    //GET
    //get all
    public List<Review> getReviews() {
        return reviewRepository.findAll();
    }

    //get all (by product)
    public ArrayList<Review> getProductReviews(Integer productId) {
        ArrayList<Review> foundReviews = new ArrayList();
        for (Review review : getReviews()) {
            if (review.getProductId() == productId) {
                foundReviews.add(review);
            }
        }
        return foundReviews;
    }

    //get one
    public Review getReview(Integer id) {
        if(reviewRepository.findById(id).isPresent()) {
            return reviewRepository.getById(id);
        }
        return null;
    }

    //POST
    public int addReview(Review review) {
        //check if user posting exists
        Product product = productService.getProduct(review.getProductId());
        if (userService.getUser(review.getUserId()) == null) {
            return 1; //case 1: User with ID doesn't exist
        }
        if (product == null) {
            return 2; //case 2: Product with ID doesn't exist
        }
        reviewRepository.save(review);

        //update product number of reviews and average score
        //in case it's the first review
        if (product.getNumberOfReview() == 0) {
            product.setAverageScore(review.getScore());
            product.setNumberOfReview(1);
            productRepository.save(product);
            return 0;
        }

        int oldNumberOfReviews = product.getNumberOfReview();
        double oldAverageScore = product.getAverageScore();

        product.setNumberOfReview(oldNumberOfReviews + 1);

        product.setAverageScore(Math.round(((oldAverageScore * oldNumberOfReviews + review.getScore()) / (oldNumberOfReviews + 1))*100.0)/100.0);

        productRepository.save(product);
        return 0; // case 0: success
    }


    public int updateReview(Integer id, Review review) {

        Review oldReview = getReview(id);
        Product product = productService.getProduct(oldReview.getProductId());
        if (oldReview == null) {
            return 5; // case 5: review doesn't exist
        }
        //check if user posting exists
        if (userService.getUser(getReview(id).getUserId()) == null) {
            return 1; //case 1: User with ID doesn't exist
        }
        if (product == null) {
            return 2; //case 2: Product with ID doesn't exist
        }

        //You cannot change the user of the review or the product of the review
        if (review.getProductId() != getReview(id).getProductId()) {
            return 3; //case 3: invalid; you can't change the product of the review
        }
        if (review.getUserId() != getReview(id).getUserId()) {
            return 4; //case 4: invalid; you can't change the user of the review
        }

        // Update the review

        int oldScore = oldReview.getScore();
        int newScore = review.getScore();
        double oldAverageScore = product.getAverageScore();
        int numberOfReviews = product.getNumberOfReview();

        // Update average score

        product.setAverageScore(Math.round((((numberOfReviews * oldAverageScore) - oldScore + newScore) / numberOfReviews)*100.0)/100.0);

        Review newReview = new Review();
        newReview.setScore(newScore);
        newReview.setComment(review.getComment());
        reviewRepository.save(newReview);
        productRepository.save(product);

        return 0; //success
    }



    public boolean deleteReview(Integer id) {

        Review thisReview = getReview(id);
        if (thisReview == null) {
            return false; //doesn't exist
        }
        Product product = productService.getProduct(thisReview.getProductId());
        int oldNumberOfReviews = product.getNumberOfReview();
        double oldAverageScore = product.getAverageScore();

        // Update the product's review
        if (oldNumberOfReviews > 1) {
            product.setNumberOfReview(oldNumberOfReviews - 1);

            product.setAverageScore(Math.round(((oldNumberOfReviews * oldAverageScore - thisReview.getScore()) /
                    (oldNumberOfReviews - 1))*100.0)/100.0);
        } else {
            //in case there's only this review
            product.setNumberOfReview(0);
            product.setAverageScore(0);
        }
        productRepository.save(product);
        reviewRepository.delete(thisReview);
        return true; // Success
    }
}
