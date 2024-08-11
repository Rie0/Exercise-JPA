package org.twspring.exercisejpa.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.twspring.exercisejpa.Model.MerchantStock;
import org.twspring.exercisejpa.Repository.MerchantStockRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MerchantStockService {

    private final MerchantStockRepository merchantStockRepository;
    private final ProductService productService;
    private final MerchantService merchantService;


    public List<MerchantStock> getMerchantStocks() {
        return merchantStockRepository.findAll();
    }

    public MerchantStock getMerchantStock(Integer id) {
        if (merchantStockRepository.findById(id).isPresent()) {
            return merchantStockRepository.getById(id);
        }
        return null;
    }

    public Integer addMerchantStock(MerchantStock merchantStock) {
        //check of merchant exists
        if (merchantService.getMerchant(merchantStock.getMerchantId())==null) {
            return 1; //case 1: no merchant with ID found
        }
        if (productService.getProduct(merchantStock.getProductId())==null) {
            return 2; //case 2: no product with ID found
        }
        merchantStockRepository.save(merchantStock);
        return 0; //success
    }


    public boolean updateMerchantStock(Integer id, MerchantStock merchantStock) {
        MerchantStock updatedMerchantStock = getMerchantStock(id);
        if (updatedMerchantStock==null) {
            return false;
        }
        updatedMerchantStock.setMerchantId(merchantStock.getMerchantId());
        updatedMerchantStock.setProductId(merchantStock.getProductId());
        updatedMerchantStock.setStock(merchantStock.getStock());
        merchantStockRepository.save(updatedMerchantStock);
        return true;
    }

    public int updateStock(Integer id, Integer productId, Integer merchantId, Integer amount) {
        //1- Not found cases

        //Check if a product with the given id exists, if not return 1

        if (productService.getProduct(productId) == null) {
            return 1; //Case 1: no product with ID found
        }

        //Check if a merchant with the given id exists, if not return 2
        if (merchantService.getMerchant(merchantId) == null) {
            return 2; //Case 2: no product with ID found
        }

        //2- Mismatch cases

        for (MerchantStock merchantStock : merchantStockRepository.findAll()) {
            if (merchantStock.getId() == id) {
                if (merchantStock.getProductId() != productId) {
                    return 3; //Case 3: The product with ID is not the product of this stock
                }
                if (merchantStock.getMerchantId() != merchantId) {
                    return 4; //Case 4: The merchant with ID is not the owner of this stock
                }
                //3- Amount Input validation failure case
                if (amount < 0) {
                    return 5; //Case 5: The amount cannot be less than 1
                }
                //if passed all conditions
                merchantStock.setStock(merchantStock.getStock() + amount);
                merchantStockRepository.save(merchantStock);
                return 0; //case 0: success
            }
        }
        return 6; //case 6: no merchant stock with ID found
    }



    public boolean deleteMerchantStock(Integer id) {
        MerchantStock merchantStock = getMerchantStock(id);
        if (merchantStock==null) {
        return false;
        }
        merchantStockRepository.delete(merchantStock);
        return true;
    }
}
