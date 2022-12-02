package com.CUBank.creditunionbank;

import com.CUBank.creditunionbank.entitites.CoDEntity;
import com.CUBank.creditunionbank.entitites.MoneyMarketEntity;
import com.CUBank.creditunionbank.repositories.CoDRepo;
import com.CUBank.creditunionbank.repositories.MoneyMarketRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;
import java.util.Optional;

@SpringBootApplication
public class CreditUnionBankApplication {

//	@Autowired
//	private IAdminService adminService;

//	@Autowired
//	private MoneyMarketRepo moneyMarketRepo;
//
//	@Autowired
//	private CoDRepo coDRepo;
//
//	@Autowired
//	private PasswordEncoder passwordEncoder;


	public static void main(String[] args) {
		SpringApplication.run(CreditUnionBankApplication.class, args);
	}
//
//	@PostConstruct
//	public void init() {
//		Optional<MoneyMarketEntity> mme = moneyMarketRepo.findByAccountNumber("77700000001");
//		MoneyMarketEntity m = mme.get();
//		m.setPassword(passwordEncoder.encode("ramesh12345"));
//		m = moneyMarketRepo.save(m);
//
//		Optional<MoneyMarketEntity> mme2 = moneyMarketRepo.findByAccountNumber("77700000003");
//		MoneyMarketEntity m2 = mme2.get();
//		m2.setPassword(passwordEncoder.encode("rajesh12345"));
//		m2 = moneyMarketRepo.save(m2);
//
//		Optional<CoDEntity> coDOp = coDRepo.findByAccountNumber("11100000001");
//		CoDEntity cod = coDOp.get();
//		cod.setPassword(passwordEncoder.encode("raja12345"));
//		cod = coDRepo.save(cod);
//
//
//		Optional<CoDEntity> coDOp2 = coDRepo.findByAccountNumber("11100000003");
//		CoDEntity cod2 = coDOp2.get();
//		cod2.setPassword(passwordEncoder.encode("ranjit12345"));
//		cod2 = coDRepo.save(cod2);
//
//		Optional<CoDEntity> coDOp3 = coDRepo.findByAccountNumber("11100000005");
//		CoDEntity cod3 = coDOp3.get();
//		cod3.setPassword(passwordEncoder.encode("ranjan12345"));
//		cod3 = coDRepo.save(cod3);
//
//	}

}
