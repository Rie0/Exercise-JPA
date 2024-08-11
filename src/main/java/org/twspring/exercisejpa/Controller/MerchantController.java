package org.twspring.exercisejpa.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.twspring.exercisejpa.Api.ApiResponse;
import org.twspring.exercisejpa.Model.Merchant;
import org.twspring.exercisejpa.Service.MerchantService;

@RestController
@RequestMapping("api/v1/merchant")
@RequiredArgsConstructor
public class MerchantController {
    private final MerchantService merchantService;

    //=======================================GET=======================================
    @GetMapping("/get/merchants")
    public ResponseEntity getMerchants() {
        if (merchantService.getMerchants().isEmpty()) {
            return ResponseEntity.status(404).body(new ApiResponse("No merchants found"));
        }
        return ResponseEntity.status(200).body(merchantService.getMerchants());
    }

    @GetMapping("/get/merchant/{id}")
    public ResponseEntity getMerchant(@PathVariable Integer id) {
        if (merchantService.getMerchant(id)==null){
            return ResponseEntity.status(404).body(new ApiResponse("No merchant with ID "+id+" found"));
        }
        return ResponseEntity.status(200).body(merchantService.getMerchant(id));
    }
    //=======================================POST=======================================
    @PostMapping("add/merchant")
    public ResponseEntity addMerchant(@Valid @RequestBody Merchant merchant, Errors errors) {
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }
        merchantService.addMerchant(merchant);
        return ResponseEntity.status(201).body(new ApiResponse("Merchant added successfully"));
    }

    //FOR TESTS
    @PostMapping("add/merchants")
    public ResponseEntity addMerchants() {
        Merchant merchant1 = new Merchant(1,"Merchant 1",false);
        Merchant merchant2 = new Merchant(2,"Merchant 2",false);
        merchantService.addMerchant(merchant1);
        merchantService.addMerchant(merchant2);
        return ResponseEntity.status(201).body(new ApiResponse("Merchants added successfully"));
    }
    //=======================================UPDATE=======================================
    @PutMapping("update/merchant/{id}")
    public ResponseEntity updateMerchant(@PathVariable Integer id,@Valid @RequestBody Merchant merchant, Errors errors) {
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }
        boolean isUpdated = merchantService.updateMerchant(id, merchant);
        if (isUpdated) {
            return ResponseEntity.status(200).body(new ApiResponse("Merchant updated successfully"));
        }
        return ResponseEntity.status(400).body(new ApiResponse("No merchant with ID "+id+" found"));
    }

    @PutMapping("/update/merchant/certify/{adminId}/{merchantId}")
    public ResponseEntity certifyMerchant(@PathVariable Integer adminId, @PathVariable Integer merchantId) {
        int flag = merchantService.certifyMerchant(adminId, merchantId);
        switch (flag) {
            case 0:
                return ResponseEntity.status(200).body(new ApiResponse("Merchant successfully certified"));
            case 1:
                return ResponseEntity.status(404).body(new ApiResponse("Admin with ID "+adminId+" not found"));
            case 2:
                return ResponseEntity.status(404).body(new ApiResponse("Merchant with ID "+merchantId+" not found"));
            case 3:
                return ResponseEntity.status(400).body(new ApiResponse("Certification must be done by an Administrator"));
            case 4:
                return ResponseEntity.status(400).body(new ApiResponse("Merchant already certified"));
            default:
                return ResponseEntity.status(400).body(new ApiResponse("Merchant successfully certified"));
        }

    }
    //=======================================DELETE=======================================
    @DeleteMapping("delete/merchant/{id}")
    public ResponseEntity deleteMerchant(@PathVariable Integer id) {
        boolean isDeleted = merchantService.deleteMerchant(id);
        if (isDeleted) {
            return ResponseEntity.status(200).body(new ApiResponse("Merchant deleted successfully"));
        }
        return ResponseEntity.status(400).body(new ApiResponse("No merchant with ID "+id+" found"));
    }
}
