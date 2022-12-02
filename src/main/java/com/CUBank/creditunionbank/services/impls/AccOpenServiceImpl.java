package com.CUBank.creditunionbank.services.impls;

import com.CUBank.creditunionbank.dtos.AccOpenDTO;
import com.CUBank.creditunionbank.dtos.CoDDTO;
import com.CUBank.creditunionbank.dtos.MoneyMarketDTO;
import com.CUBank.creditunionbank.entitites.AccOpenEntity;
import com.CUBank.creditunionbank.enums.AccountStatus;
import com.CUBank.creditunionbank.enums.AccountType;
import com.CUBank.creditunionbank.enums.IdentityType;
import com.CUBank.creditunionbank.repositories.AccOpenRepo;
import com.CUBank.creditunionbank.services.IAccOpenService;
import com.CUBank.creditunionbank.services.ICodService;
import com.CUBank.creditunionbank.services.IMoneyMarketService;
import com.CUBank.creditunionbank.utils.LoggedInContext;
import com.CUBank.creditunionbank.utils.MapperUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AccOpenServiceImpl implements IAccOpenService {

    @Autowired
    private AccOpenRepo accOpenRepo;

    @Autowired
    private ICodService codService;
    @Autowired
    private IMoneyMarketService mmService;

    @Override
    @Transactional
    public AccOpenDTO openingForm(final AccOpenDTO openingDTO) {
        log.info("Received account opening request: ", openingDTO.getAccountType());
        if(openingDTO.getAccountType().equals(AccountType.ADMIN)) {
            log.info("Invalid Account type");
            return null;
        }
        AccOpenEntity accountOpening = MapperUtil.convertTo(openingDTO, AccOpenEntity.class);
        accountOpening.setAmount(openingDTO.getAmount());
        accountOpening.setRequestedOn(LocalDateTime.now());
        accountOpening.setAccountStatus(AccountStatus.PENDING);
        accountOpening = accOpenRepo.save(accountOpening);
        log.info("Customer details added to the db: ", accountOpening.getAccountType());
        return MapperUtil.convertTo(accountOpening, AccOpenDTO.class);
    }

    @Override
    public Optional<AccOpenDTO> getById(final Long id) {
        log.info("Retrieving Account Opening Details by id: ", id);
        Optional<AccOpenEntity> entities = accOpenRepo.findById(id);
        if(entities.isPresent()) {
            log.info("Retrieved Account Opening Details by id: ", entities.get().getAccountType());
            return Optional.of(MapperUtil.convertTo(entities.get(), AccOpenDTO.class));
        }
        return Optional.empty();
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Optional<AccOpenDTO> updateById(final Long id, final AccountStatus status) throws DataIntegrityViolationException {
        log.info("Retrieving Account Opening Details by id: ", id);
        Optional<AccOpenEntity> entities = accOpenRepo.findById(id);
        if(entities.isPresent() && entities.get().getAccountStatus().equals(AccountStatus.PENDING)) {
            log.info("Retrieved Account Opening Details by id: ", entities.get().getAccountType());
            AccOpenEntity accOpnEntity = entities.get();
            accOpnEntity.setAccountStatus(status);
            accOpnEntity.setCreatedBy(Long.valueOf(LoggedInContext.getCurrentUser()));
            if(status.equals(AccountStatus.APPROVED)) {
//
                // If Request get Approval and Account type is Consumer than a new account will be created
                if(accOpnEntity.getAccountType().equals(AccountType.CONSUMER) ||
                        accOpnEntity.getAccountType().equals(AccountType.COMMERCIAL)) {
                    mmService.create(MapperUtil.convertTo(accOpnEntity, MoneyMarketDTO.class));

                } else if(accOpnEntity.getAccountType().equals(AccountType.CERTIFICATE_OF_DEPOSIT)) {
                    CoDDTO dto = MapperUtil.convertTo(accOpnEntity, CoDDTO.class);
                    dto.setBalance(accOpnEntity.getAmount());
                    codService.create(dto);
                } else {
                    log.info("Met some error account Opening status not updated");
                    return null;
                }

                accOpnEntity.setPassword("xxxxxxxxxx");
                accOpnEntity.setMaturityDate(null);
                accOpnEntity.setAmount(00.00);
                accOpnEntity.setCreatedOn(LocalDateTime.now());
            }
            accOpnEntity = accOpenRepo.save(accOpnEntity);
            log.info("Updated Data in Account Opening Request Table: ", accOpnEntity);
            return Optional.of(MapperUtil.convertTo(accOpnEntity, AccOpenDTO.class));
        }
        log.info("Account already Approved or Rejected");
        return Optional.empty();
    }

    @Override
    public List<AccOpenDTO> getAllPendingRequests() {
        log.info("Retrieving all Pending Requests");
        List<AccOpenEntity> entities = accOpenRepo.findByAccountStatus(AccountStatus.PENDING);
        List<AccOpenDTO> dtos = new ArrayList<>();
        entities.forEach((e) -> {
            dtos.add(MapperUtil.convertTo(e, AccOpenDTO.class));
        });
        return dtos;
    }

    @Override
    public List<AccOpenDTO> getAllApprovedRequests() {
        log.info("Retrieving all Approved Requests");
        List<AccOpenEntity> entities = accOpenRepo.findByAccountStatus(AccountStatus.APPROVED);
        List<AccOpenDTO> dtos = new ArrayList<>();
        entities.forEach((e) -> {
            dtos.add(MapperUtil.convertTo(e, AccOpenDTO.class));
        });
        return dtos;
    }

    @Override
    public List<AccOpenDTO> getAll() {
        log.info("Retrieving all the requests");
        List<AccOpenEntity> entities = accOpenRepo.findAll();
        List<AccOpenDTO> dtos = new ArrayList<>();
        entities.forEach((e) -> {
            dtos.add(MapperUtil.convertTo(e, AccOpenDTO.class));
        });
        return dtos;
    }

    @Override
    public Boolean checkByAccTypeAndIdentityNumber(AccountType accountType, String identityNumber) {
        log.info("Retrieving accounts by accountType and identity number: ");
        List<AccOpenEntity> accOpenEntities = accOpenRepo.findByAccountTypeAndIdentityNumber(accountType, identityNumber);
        if(accOpenEntities.size() == 0) {
            return false;
        } else {
            for(AccOpenEntity ae: accOpenEntities) {
                if(ae.getAccountStatus().equals(AccountStatus.APPROVED)) {
                    return true;
                }
            }
            return false;
        }
    }


}
