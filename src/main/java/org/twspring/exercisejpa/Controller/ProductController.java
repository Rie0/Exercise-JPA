package org.twspring.exercisejpa.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.twspring.exercisejpa.Api.ApiResponse;
import org.twspring.exercisejpa.Model.Product;
import org.twspring.exercisejpa.Service.ProductService;

import java.util.List;

@RestController
@RequestMapping("api/v1/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    //=======================================GET=======================================
    @GetMapping("/get/products")
    public ResponseEntity getProducts() {
        if (productService.getProducts().isEmpty()) {
            return ResponseEntity.status(404).body(new ApiResponse("No products found"));
        }
        return ResponseEntity.status(200).body(productService.getProducts());
    }
    @GetMapping("/get/product/{id}")
    public ResponseEntity getProduct(@PathVariable Integer id) {
        if (productService.getProduct(id)==null){
            return ResponseEntity.status(404).body(new ApiResponse("No product with ID "+id+" found"));
        }
        return ResponseEntity.status(200).body(productService.getProduct(id));
    }

    //EXTRA: GET PRODUCTS BY CATEGORY
    @GetMapping("get/products/by_category/{category}")
    public ResponseEntity getProductByCategory(@PathVariable String category) {
        List<Product> foundProducts = productService.getProductsByCategory(category);
        if (foundProducts.isEmpty()) {
            return ResponseEntity.status(404).body(new ApiResponse("No products found"));
        }
        return ResponseEntity.status(200).body(foundProducts);
    }
    //=======================================POST=======================================
    @PostMapping("/add/product")
    public ResponseEntity addProduct(@Valid @RequestBody Product product, Errors errors) {
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }
        if (productService.addProduct(product)) {
            return ResponseEntity.status(201).body(new ApiResponse("Product added successfully"));
        }
        return ResponseEntity.status(409).body(new ApiResponse("Category with ID "+product.getCategoryId()+" does not exist"));
    }
    //FOR TESTS
    @PostMapping("/add/products")
    public ResponseEntity addProducts() {
        Product product1 = new Product(1,"face cream",20.50,3,false,0,0);
        Product product2 = new Product(2,"eye cream",15.50,3,false,0,0);
        Product product3 = new Product(3,"keyboard",35.70,2,false,0,0);
        Product product4 = new Product(4,"headphones",19.20,2,false,0,0);
        Product product5 = new Product(5,"white shirt",10.00,1,false,0,0);
        Product product6 = new Product(6,"blue jeans",15.50,1,false,0,0);
        productService.addProduct(product1);
        productService.addProduct(product2);
        productService.addProduct(product3);
        productService.addProduct(product4);
        productService.addProduct(product5);
        productService.addProduct(product6);
        return ResponseEntity.status(201).body(new ApiResponse("Product added successfully"));
    }
    //=======================================UPDATE=======================================
    @PutMapping("/update/product/{id}")
    public ResponseEntity updateProduct(@PathVariable Integer id,@Valid @RequestBody Product product,Errors errors) {
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }
        boolean isUpdated = productService.updateProduct(id,product);
        if (isUpdated) {
            return ResponseEntity.status(201).body(new ApiResponse("Product updated successfully"));
        }
        return ResponseEntity.status(404).body(new ApiResponse("No product with ID "+id+" found"));
    }

    @PutMapping("/update/product/apply_sale/{userId}/{categoryId}/{salePercentage}")
    public ResponseEntity applySaleToProductsByCategory(@PathVariable Integer userId,@PathVariable Integer categoryId,@PathVariable double salePercentage) {

        int flag = productService.applySaleToProductsByCategory(userId, categoryId, salePercentage);

        switch (flag) {
            case 0:
                return ResponseEntity.status(200)
                        .body(new ApiResponse("Sale applied successfully"));
            case 1:
                return ResponseEntity.status(400)
                        .body(new ApiResponse("Category not found"));
            case 2:
                return ResponseEntity.status(400)
                        .body(new ApiResponse("Admin not found"));
            case 3:
                return ResponseEntity.status(400)
                        .body(new ApiResponse("Only admins can apply a discount"));
            case 4:
                return ResponseEntity.status(400)
                        .body(new ApiResponse("Sale must be between 10 and 90 percent"));
            case 5:
                return ResponseEntity.status(400)
                        .body(new ApiResponse("Category already on sale"));
            default:
                return ResponseEntity.status(400)
                        .body(new ApiResponse("An unexpected error occurred"));
        }

    }
    //=======================================DELETE=======================================
    @DeleteMapping("/delete/product/{id}")
    public ResponseEntity deleteProduct(@PathVariable Integer id) {
        boolean isDeleted = productService.deleteProduct(id);
        if (isDeleted) {
            return ResponseEntity.status(201).body(new ApiResponse("Product deleted successfully"));
        }
        return ResponseEntity.status(404).body(new ApiResponse("No product with ID "+id+" found"));
    }
}
