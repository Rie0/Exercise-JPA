package org.twspring.exercisejpa.Service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.twspring.exercisejpa.Model.Category;
import org.twspring.exercisejpa.Model.Product;
import org.twspring.exercisejpa.Model.User;
import org.twspring.exercisejpa.Repository.ProductRepository;
import org.twspring.exercisejpa.Repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final UserRepository userRepository;

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public Product getProduct(Integer id) {
        if (productRepository.findById(id).isPresent()) {
            return productRepository.getById(id);
        }
        return null;
    }


    public List<Product> getProductsByCategory(String category) {
        ArrayList<Product> foundProducts = new ArrayList<>();
        Integer thisCategoryId=-1;

        for (Category cat : categoryService.getCategories()) {
            if (cat.getName().equalsIgnoreCase(category)){
                thisCategoryId = cat.getId();
            }
        }
        for (Product product : getProducts()) {
            if(product.getCategoryId()==thisCategoryId){
                foundProducts.add(product);
            }
        }
        return foundProducts;
    }

    //Add new Product
    public boolean addProduct(Product product) {
        if (categoryService.getCategory(product.getCategoryId())==null){
            return false;
        }
        productRepository.save(product);
        return true;
    }

    //Update an existing product
    public boolean updateProduct(Integer id, Product product) {
        //reviews aren't updating with the product details
        Product updatedProduct = getProduct(id);
        if (updatedProduct == null) {
            return false;
        }
        updatedProduct.setName(product.getName());
        updatedProduct.setPrice(product.getPrice());
        updatedProduct.setOnSale(product.isOnSale());
        updatedProduct.setCategoryId(product.getCategoryId());
        productRepository.save(updatedProduct);
        return true;
    }

    public int applySaleToProductsByCategory(Integer userId, Integer categoryId, double salePercentage) {

        User user = userRepository.getById(userId);

        if (categoryService.getCategory(categoryId) == null){
            return 1; //case 1: category not found
        }
        if (!userRepository.findById(userId).isPresent()){
            return 2;//case 2: Admin not found
        }
        if (!user.getRole().equalsIgnoreCase("Admin")){
            return 3; //case 3: Only admins can apply a discount
        }
        if (salePercentage<10||salePercentage>90){
            return 4; //case 4: Sale must be between 10 and 90 percent
        }
        salePercentage = salePercentage/100.0;
        for (Product product : productRepository.findAll()) {
            if (product.getCategoryId() == categoryId){
                if (product.isOnSale()){
                    return 5; //category already on sale
                }

                product.setPrice(Math.round((product.getPrice()-(product.getPrice()*salePercentage))*100.0)/100.0);
                product.setOnSale(true);
                productRepository.save(product);
            }
        }
        return 0; //success
    }

    //Delete a product
    public boolean deleteProduct(Integer id) {
        Product product = getProduct(id);
        if (product == null) {
            return false;
        }
        productRepository.delete(product);
        return true;
    }
}

