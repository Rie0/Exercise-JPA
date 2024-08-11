package org.twspring.exercisejpa.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.twspring.exercisejpa.Model.Merchant;
import org.twspring.exercisejpa.Model.MerchantStock;
import org.twspring.exercisejpa.Model.Product;
import org.twspring.exercisejpa.Model.User;
import org.twspring.exercisejpa.Repository.MerchantStockRepository;
import org.twspring.exercisejpa.Repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    static final double primeSubscriptionCost = 4.99;
    static final double primeDiscount = 0.2; //%20 discount

    private final UserRepository userRepository;
    private final MerchantService merchantService;
    private final ProductService productService;
    private final MerchantStockService merchantStockService;
    private final MerchantStockRepository merchantStockRepository;


    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUser(Integer id) {
        if (userRepository.findById(id).isPresent()) {
            return userRepository.getById(id);
        }
        return null;
    }

    public void addUser(User user) {
        userRepository.save(user);
    }

    public boolean updateUser(Integer id, User user) {
        User updatedUser = getUser(id);
        if (updatedUser == null) {
            return false;
        }
        updatedUser.setUsername(user.getUsername());
        updatedUser.setEmail(user.getEmail());
        updatedUser.setPassword(user.getPassword());
        updatedUser.setBalance(user.getBalance());
        updatedUser.setRole(user.getRole());
        userRepository.save(updatedUser);
        return true;
    }

    //EXTRA: PRIME MEMBERSHIP
    public int subscribeToPrime(Integer id) {
        User user = getUser(id);
        //check cases:
        if(user == null) {
            return 1; //user with ID doesn't exist.
        }
        if (!user.getRole().equalsIgnoreCase("Customer")){
            return 2; //Only customers can apply for prime membership
        }
        if (user.isPrimeMember()) {
            return 3; //Already a member
        }
        if (user.getBalance()<primeSubscriptionCost) {
            return 4; //Not enough balance
        }
        user.setBalance(getUser(id).getBalance()-primeSubscriptionCost);
        user.setPrimeMember(true);
        userRepository.save(user);
        return 0; //success
    }

    public int purchaseProduct(Integer userId, Integer merchantId, Integer productId) {
        User user = getUser(userId);
        Merchant merchant = merchantService.getMerchant(merchantId);
        Product product = productService.getProduct(productId);
        //Not found cases
        if (merchant == null) {
            return 1; //case 1: no merchant with ID found
        }
        if (product == null) {
            return 2; //case 2: no product with ID found
        }
        if (user == null) {
            return 3; //case 3: no user with ID found
        }
        if (user.getRole().equalsIgnoreCase("Admin")){
            return 4; //case 4: User is an admin, admins cannot purchase from the website.
        }

        //Check if a merchant has the product

        for (MerchantStock merchantStock: merchantStockRepository.findAll()) {

            if (merchantStock.getMerchantId()==merchantId && merchantStock.getProductId()==productId) {
                //check if the product is stocked
                if (merchantStock.getStock()>0){
                    if (user.getBalance()>=product.getPrice()){
                        //EXTRA APPLY PRIME MEMBERSHIP DISCOUNT
                        if (user.isPrimeMember()){

                            merchantStock.setStock(merchantStock.getStock()-1);
                            merchantStockRepository.save(merchantStock);

                            user.setBalance(user.getBalance()-(
                                    productService.getProduct(productId).getPrice()-
                                            (productService.getProduct(productId).getPrice()*primeDiscount)));
                            userRepository.save(user);

                            return 8;// case 8: Success prime purchase
                        }

                        merchantStock.setStock(merchantStock.getStock()-1);
                        merchantStockRepository.save(merchantStock);
                        user.setBalance(user.getBalance()-product.getPrice());
                        userRepository.save(user);

                        return 0;// case 0: Success
                    }else {
                        return 5;// case 5: Not enough balances
                    }
                }else {
                    return 6;// case 6: Item out of stock
                }
            }
        }
        return 7;//case 7: Merchant doesn't sell the product
    }

    public boolean deleteUser(Integer id) {
        User user = getUser(id);
        if (user == null) {
            return false;
        }
        userRepository.delete(user);
        return true;
    }
}

