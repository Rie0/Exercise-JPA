package org.twspring.exercisejpa.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.twspring.exercisejpa.Model.Merchant;
import org.twspring.exercisejpa.Model.User;
import org.twspring.exercisejpa.Repository.MerchantRepository;
import org.twspring.exercisejpa.Repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MerchantService {
    private final MerchantRepository merchantRepository;
    private final UserRepository userRepository;

    public List<Merchant> getMerchants() {
        return merchantRepository.findAll();
    }

    public Merchant getMerchant(Integer id) {
        //Makes sure it returns null if no object with the id exists
        if (merchantRepository.findById(id).isPresent()) {
            return merchantRepository.getById(id);
        }
        return null;
    }

    public void addMerchant(Merchant merchant) {
       merchantRepository.save(merchant);
    }

    public boolean updateMerchant(Integer id, Merchant merchant) {
        Merchant updatedMerchant = getMerchant(id);
        if (updatedMerchant == null) {
            return false;
        }
        updatedMerchant.setName(merchant.getName());
        merchantRepository.save(updatedMerchant);
        return true;
    }

    public int certifyMerchant(Integer adminId, Integer merchantId) {
        User user = userRepository.getById(adminId);
        Merchant merchant = getMerchant(merchantId);
        //check if exists
        if (!userRepository.findById(adminId).isPresent()){
            return 1; //case: Admin doesn't exist
        }
        if (merchant == null){
            return 2; //case: merchant doesn't exist
        }
        //check if is admin
        if (!user.getRole().equalsIgnoreCase("Admin")){
            return 3; //case: Certification must be done by an admin
        }
        if (merchant.isCertified()){
            return 4; //case: Merchant already certified
        }
        merchant.setCertified(true);
        merchantRepository.save(merchant);
        return 0; //success
    }

    public boolean deleteMerchant(Integer id) {
        Merchant merchant = getMerchant(id);
        if (merchant == null) {
            return false;
        }
        merchantRepository.delete(merchant);
        return true;
    }
}
