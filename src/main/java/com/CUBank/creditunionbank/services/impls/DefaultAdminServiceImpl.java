package com.CUBank.creditunionbank.services.impls;

import com.CUBank.creditunionbank.dtos.AdminDTO;
import com.CUBank.creditunionbank.entitites.AdminEntity;
import com.CUBank.creditunionbank.enums.AccountStatus;
import com.CUBank.creditunionbank.enums.AccountType;
import com.CUBank.creditunionbank.repositories.AdminRepo;
import com.CUBank.creditunionbank.services.IAdminService;
import com.CUBank.creditunionbank.utils.MapperUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional(readOnly = true)
public class DefaultAdminServiceImpl implements IAdminService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AdminRepo adminRepo;

    @Override
    @Transactional
    public AdminDTO create(AdminDTO adminDTO) {
        log.info("Creating Admin with: ", adminDTO.getFirstName());
        AdminEntity adminEntity = MapperUtil.convertTo(adminDTO, AdminEntity.class);
        String accNum = String.valueOf(Long.valueOf(adminRepo.findMaxAccountNum()) + 2);
        adminEntity.setAccountNumber(accNum);
        adminEntity.setPassword(passwordEncoder.encode(adminDTO.getPassword()));
        adminEntity.setAccountOpened(0);
        adminEntity.setAccountType(AccountType.ADMIN);
        adminEntity.setStatus(AccountStatus.ACTIVE);
        adminEntity = adminRepo.save(adminEntity);
        log.info("Saved Admin in DB: {} ", adminEntity.getAccountNumber());
        return MapperUtil.convertTo(adminEntity, AdminDTO.class);
    }

    @Override
    public Optional<AdminDTO> getByAccountNumber(final String accountNumber) {
        log.info("Retrieving Admin by accountNumber: ", accountNumber);
        Optional<AdminEntity> adminEntity = adminRepo.findByAccountNumber(accountNumber);
        if(adminEntity.isPresent()) {
            log.info("Retrieved Admin from DB: {} ", adminEntity.get().getAccountNumber());
            return Optional.of(MapperUtil.convertTo(adminEntity.get(), AdminDTO.class));
        }
        return Optional.empty();
    }

    @Override
    public List<AdminDTO> getAll() {
        return null;
    }
}
