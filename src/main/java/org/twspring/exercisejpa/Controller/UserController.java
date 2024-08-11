package org.twspring.exercisejpa.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.twspring.exercisejpa.Api.ApiResponse;
import org.twspring.exercisejpa.Model.User;
import org.twspring.exercisejpa.Service.UserService;

@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    //=======================================GET=======================================
    @GetMapping("/get/users")
    public ResponseEntity getUsers() {
        if(userService.getUsers().isEmpty()) {
            return ResponseEntity.status(404).body(new ApiResponse("No users found"));
        }
        return ResponseEntity.status(200).body(userService.getUsers());
    }
    @GetMapping("/get/user/{id}")
    public ResponseEntity getUser(@PathVariable Integer id) {
        if (userService.getUser(id) == null) {
            return ResponseEntity.status(404).body(new ApiResponse("No user with ID " + id + " found"));
        }
        return ResponseEntity.status(200).body(userService.getUser(id));
    }
    //=======================================POST=======================================
    @PostMapping("add/user")
    public ResponseEntity addUser(@Valid @RequestBody User user, Errors errors) {
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }
        userService.addUser(user);
        return ResponseEntity.status(201).body(new ApiResponse("User added successfully"));
    }
    @PostMapping("add/users") //FOR TESTS
    public ResponseEntity addUsers() {
        User user1 = new User(10,"user1","Aa123@wqee","UserA@gmail.com"
                ,"Customer",20000.00,false);
        userService.addUser(user1);
        User user2 = new User(11,"user2","Bb123@wqee","UserB@gmail.com"
                ,"Customer",20000.00,false);
        userService.addUser(user2);
        User user3 = new User(12,"user3","Cc123@wqee","UserC@gmail.com"
                ,"Customer",20000.00,false);
        userService.addUser(user3);
        User admin = new User(13,"admin","Dd123@wqee","Admin@this.com"
                ,"Admin",20000.00,false);
        userService.addUser(admin);

        return ResponseEntity.status(201).body(new ApiResponse("Users added successfully"));
    }


    //=======================================UPDATE=======================================
    @PutMapping("/update/user/{id}")
    public ResponseEntity updateUser(@PathVariable Integer id, @Valid @RequestBody User user, Errors errors) {
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }
        boolean isUpdated = userService.updateUser(id, user);
        if (isUpdated) {
            return ResponseEntity.status(201).body(new ApiResponse("User updated successfully"));
        }
        return ResponseEntity.status(404).body(new ApiResponse("No user with ID " + id + " found"));
    }

    @PutMapping("/purchase/{userId}/{merchantId}/{productId}")
    public ResponseEntity purchase(@PathVariable Integer userId, @PathVariable Integer merchantId, @PathVariable Integer productId) {
        int flag = userService.purchaseProduct(userId,merchantId,productId);
        switch (flag){
            case 0:
                return ResponseEntity.status(200).body(new ApiResponse("Product purchased successfully"));
            case 1:
                return ResponseEntity.status(400).body(new ApiResponse("No merchant with ID " + merchantId + " found"));
            case 2:
                return ResponseEntity.status(400).body(new ApiResponse("No product with ID " + productId + " found"));
            case 3:
                return ResponseEntity.status(400).body(new ApiResponse("No user with ID " + userId + " found"));
            case 4:
                return ResponseEntity.status(400).body(new ApiResponse("User is an admin, admins cannot purchase from the website"));
            case 5:
                return ResponseEntity.status(400).body(new ApiResponse("Not enough balance to purchase"));
            case 6:
                return ResponseEntity.status(400).body(new ApiResponse("Item out of stock"));
            case 7:
                return ResponseEntity.status(400).body(new ApiResponse("Merchant doesn't sell the product"));
            case 8:
                return ResponseEntity.status(400).body(new ApiResponse("Product purchased successfully with prime discount"));
            default:
                return ResponseEntity.status(400).body(new ApiResponse("An error has occurred"));
        }
    }

    //EXTRA: PRIME MEMBERSHIP
    @PutMapping("/update/user/subscribe/{id}")
    public ResponseEntity updateUserSubscribe(@PathVariable Integer id) {

        int flag = userService.subscribeToPrime(id);
        switch (flag){
            case 0:
                return ResponseEntity.status(200).body(new ApiResponse("User subscribed successfully"));
            case 1:
                return ResponseEntity.status(400).body(new ApiResponse("User with ID " + id + " doesn't exist"));
            case 2:
                return ResponseEntity.status(400).body(new ApiResponse("Only customers can subscribe to a Prime membership"));
            case 3:
                return ResponseEntity.status(400).body(new ApiResponse("User with ID "+id+" already subscribed"));
            case 4:
                return ResponseEntity.status(400).body(new ApiResponse("User doesn't have enough balance"));
            default:
                return ResponseEntity.status(400).body(new ApiResponse("An error has occurred"));
        }
    }

    //=======================================DELETE=======================================
    @DeleteMapping("/delete/user/{id}")
    public ResponseEntity deleteUser(@PathVariable Integer id) {
        boolean isDeleted = userService.deleteUser(id);
        if (isDeleted) {
            return ResponseEntity.status(201).body(new ApiResponse("User deleted successfully"));
        }
        return ResponseEntity.status(404).body(new ApiResponse("No user with ID " + id + " found"));
    }
}
