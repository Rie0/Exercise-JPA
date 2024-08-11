package org.twspring.exercisejpa.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.twspring.exercisejpa.Model.MerchantStock;
import org.twspring.exercisejpa.Service.MerchantStockService;
import org.twspring.exercisejpa.Api.ApiResponse;

@RestController
@RequestMapping("api/v1/merchant_stock")
@RequiredArgsConstructor
public class MerchantStockController {

    private final MerchantStockService merchantStockService;

    //=======================================GET=======================================
    @GetMapping("/get/merchant_stocks")
    public ResponseEntity getMerchantStocks() {
        if (merchantStockService.getMerchantStocks().isEmpty()) {
            return ResponseEntity.status(404).body(new ApiResponse("No merchant stocks found"));
        }
        return ResponseEntity.status(200).body(merchantStockService.getMerchantStocks());
    }

    @GetMapping("/get/merchant_stock/{id}")
    public ResponseEntity getMerchantStock(@PathVariable Integer id) {
        if (merchantStockService.getMerchantStock(id)==null) {
            return ResponseEntity.status(404).body(new ApiResponse("No merchant stock with ID "+id+" found"));
        }
        return ResponseEntity.status(200).body(merchantStockService.getMerchantStock(id));
    }
    //=======================================POST=======================================
    @PostMapping("/add/merchant_stock")
    public ResponseEntity addMerchantStock(@Valid @RequestBody MerchantStock merchantStock, Errors errors) {
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }
        int flag = merchantStockService.addMerchantStock(merchantStock);
        switch (flag) {
            case 0:
                return ResponseEntity.status(201).body(new ApiResponse("Merchant stock added successfully"));
            case 1:
                return ResponseEntity.status(400).body(new ApiResponse("No merchant with ID "+merchantStock.getMerchantId()+" was found"));
            case 2:
                return ResponseEntity.status(400).body(new ApiResponse("No product with ID "+merchantStock.getProductId()+" was found"));
            default:
                return ResponseEntity.status(400).body(new ApiResponse("An error occurred."));

        }
    }

    //FOR TESTS
    @PostMapping("/add/merchant_stocks")
    public ResponseEntity addMerchantStocks() {
        //merchant1
        MerchantStock merchant1Stock1 = new MerchantStock(1,3,1,11);
        MerchantStock merchant1Stock2 = new MerchantStock(2,4,1,11);

        //merchant2
        //beauty
        MerchantStock merchant2Stock1 = new MerchantStock(3,1,2,11);
        MerchantStock merchant2Stock2 = new MerchantStock(4,2,2,11);
        //clothes
        MerchantStock merchant2Stock3 = new MerchantStock(5,5,2,11);
        MerchantStock merchant2Stock4 = new MerchantStock(6,6,2,11);
        merchantStockService.addMerchantStock(merchant1Stock1);
        merchantStockService.addMerchantStock(merchant1Stock2);
        merchantStockService.addMerchantStock(merchant2Stock1);
        merchantStockService.addMerchantStock(merchant2Stock2);
        merchantStockService.addMerchantStock(merchant2Stock3);
        merchantStockService.addMerchantStock(merchant2Stock4);
        return ResponseEntity.status(201).body(new ApiResponse("Merchant stock added successfully"));
    }
    //=======================================UPDATE=======================================
    @PutMapping("/update/merchant_stock/{id}")
    public ResponseEntity updateMerchantStock(@PathVariable Integer id, @Valid @RequestBody MerchantStock merchantStock, Errors errors) {
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }
        boolean isUpdated = merchantStockService.updateMerchantStock(id, merchantStock);
        if (isUpdated) {
            return ResponseEntity.status(201).body(new ApiResponse("Merchant stock updated successfully"));
        }
        return ResponseEntity.status(404).body(new ApiResponse("No merchant stock with ID "+id+" found"));
    }


    @PutMapping("/update/merchant_stock/add_stock/{id}/{productId}/{merchantId}/{amount}")
    public ResponseEntity updateStock(@PathVariable Integer id, @PathVariable Integer productId, @PathVariable Integer merchantId, @PathVariable int amount) {
        int flag = merchantStockService.updateStock(id,productId,merchantId,amount);
        switch (flag) {
            case 0:
                return ResponseEntity.status(200).body(new ApiResponse("Stock updated successfully."));
            case 1:
                return ResponseEntity.status(404).body(new ApiResponse("No product with ID "+productId+" found"));
            case 2:
                return ResponseEntity.status(404).body(new ApiResponse("No merchant with ID "+merchantId+" found"));
            case 3:
                return ResponseEntity.status(404).body(new ApiResponse("The product with ID "+productId+" is not the product of this stock"));
            case 4:
                return ResponseEntity.status(404).body(new ApiResponse("The merchant with ID "+merchantId+" is not the merchant of this stock"));
            case 5:
                return ResponseEntity.status(404).body(new ApiResponse("The amount cannot be less than 1"));
            case 6:
                return ResponseEntity.status(404).body(new ApiResponse("No merchant stock with ID "+id+" found"));
            default:
                return ResponseEntity.status(400).body(new ApiResponse("An error has occurred"));
        }
    }

    //=======================================DELETE=======================================
    @DeleteMapping("/delete/merchant_stock/{id}")
    public ResponseEntity deleteMerchantStock(@PathVariable Integer id) {
        boolean isDeleted = merchantStockService.deleteMerchantStock(id);
        if (isDeleted) {
            return ResponseEntity.status(201).body(new ApiResponse("Merchant stock deleted successfully"));
        }
        return ResponseEntity.status(404).body(new ApiResponse("No merchant stock with ID "+id+" found"));
    }
}

