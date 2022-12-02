package com.CUBank.creditunionbank.controllers;

import com.CUBank.creditunionbank.dtos.AdminDTO;
import com.CUBank.creditunionbank.models.responses.AdminRes;
import com.CUBank.creditunionbank.models.responses.ApiResponse;
import com.CUBank.creditunionbank.services.IAdminService;
import com.CUBank.creditunionbank.utils.MapperUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@Slf4j
public class AdminController {

    @Autowired
    private IAdminService adminService;

    @RequestMapping(value = "admin/{accountNumber}", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse<AdminRes>> getByAccountNumber(
            @PathVariable("accountNumber") final String accNum) {
        log.info("Received request to get Admin by account number: ", accNum);
        Optional<AdminDTO> adminDTOOptional = adminService.getByAccountNumber(accNum);
        if(adminDTOOptional.isPresent()) {
            log.info("Received Admin: ", adminDTOOptional.get());
            return ResponseEntity.ok(ApiResponse.success(MapperUtil.convertTo(adminDTOOptional.get(), AdminRes.class)));
        }
        log.info("Admin not found with account number: ", accNum);
        return ResponseEntity.badRequest().body(ApiResponse.success(null));
    }
}
