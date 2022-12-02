package com.CUBank.creditunionbank.controllers;

import com.CUBank.creditunionbank.constants.ErrorCode;
import com.CUBank.creditunionbank.dtos.AccOpenDTO;
import com.CUBank.creditunionbank.enums.AccountStatus;
import com.CUBank.creditunionbank.models.requests.AccOpenReq;
import com.CUBank.creditunionbank.models.responses.AccOpenRes;
import com.CUBank.creditunionbank.models.responses.ApiResponse;
import com.CUBank.creditunionbank.models.responses.ErrorCustom;
import com.CUBank.creditunionbank.services.IAccOpenService;
import com.CUBank.creditunionbank.utils.MapperUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/*
Opening new account related all the APIs available here

        End-Points                              Task                            Authority       Accessible By
 1. openAccount/openNow             --> New Account Opening Request               public           ALL
 2. openAccount/pendingRequests     --> Get the list of Pending Requests          Private          ADMIN
 3. openAccount/approvedRequests    --> Get the list of Approved Requests         Private          ADMIN
 4. openAccount/allRequests         --> Get the list of all Requests              Private          ADMIN
 5. openAccount?{id}&{status}       --> If the Request got approved               Private          ADMIN
                                        (status = approve)
                                        by Admin then new Account will be created
                                        based on Account Type.
                                        If the status = reject then no account will be created

 */
@RestController
@RequestMapping(value = "/openAccount")
@Slf4j
public class AccOpenController {


    @Autowired
    private IAccOpenService accOpenService;

    @PostMapping(value = "/openNow")
    public ResponseEntity<ApiResponse<AccOpenRes>> openNow(
            @Validated @RequestBody final AccOpenReq request) {
        log.info("Received account opening request: ", request.getAccountType());
        AccOpenDTO accountOpenDTO = MapperUtil.convertTo(request, AccOpenDTO.class);
        accountOpenDTO.setAmount(request.getAmount());
        accountOpenDTO = accOpenService.openingForm(accountOpenDTO);
        final AccOpenRes response = MapperUtil.convertTo(accountOpenDTO, AccOpenRes.class);
        log.info("Successful account opening process: ", response);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping(value = "/pendingRequests")
    public ResponseEntity<ApiResponse<List<AccOpenRes>>> pendingRequests() {
        log.info("Received request to fetch all pending requests ");
        List<AccOpenDTO> dtos = accOpenService.getAllPendingRequests();
        if(dtos.size() == 0) return ResponseEntity.badRequest().body(ApiResponse.success(null));
        List<AccOpenRes> lists = new ArrayList<>();
        dtos.forEach((e) -> {
            lists.add(MapperUtil.convertTo(e, AccOpenRes.class));
        });
        return ResponseEntity.ok(ApiResponse.success(lists));
    }

    @GetMapping(value = "/approvedRequests")
    public ResponseEntity<ApiResponse<List<AccOpenRes>>> ApprovedRequests() {
        log.info("Received request to fetch all pending requests ");
        List<AccOpenDTO> dtos = accOpenService.getAllApprovedRequests();
        if(dtos.size() == 0) return ResponseEntity.badRequest().body(ApiResponse.success(null));
        List<AccOpenRes> lists = new ArrayList<>();
        dtos.forEach((e) -> {
            lists.add(MapperUtil.convertTo(e, AccOpenRes.class));
        });
        return ResponseEntity.ok(ApiResponse.success(lists));
    }

    /*
    if the status is approve that means account creation got approved and a new account will be created
    if reject that means don't create account
     */
    @PutMapping(params = {"id", "status"})
    public ResponseEntity<ApiResponse<AccOpenRes>> updateRequest(
            @RequestParam("id") final Long id,
            @RequestParam("status")final String status) {
        log.info("Received Account Opening Status for id: ", id, " current status: ", status);
        Optional<AccOpenDTO> accOpenDTO = accOpenService.getById(id);
        if(accOpenDTO.isPresent()) {
            Boolean isDuplicate = accOpenService
                    .checkByAccTypeAndIdentityNumber(accOpenDTO.get().getAccountType(), accOpenDTO.get().getIdentityNumber());
            if(isDuplicate) {
                return ResponseEntity.badRequest().body(ApiResponse.error(ErrorCustom.create(ErrorCode.DUPLICATE_ACCOUNT, "Account already exists with the Identity Number")));
            }
        }
        AccountStatus accountStatus;
        if(status.equalsIgnoreCase("approve")) {
            accountStatus = AccountStatus.APPROVED;
        } else if(status.toString().equalsIgnoreCase("reject")) {
            accountStatus = AccountStatus.REJECTED;
        } else {
            log.info("Provide valid Response either approve or reject");
            return ResponseEntity.badRequest().body(ApiResponse.error(ErrorCustom.create(ErrorCode.INVALID_ACCOUNT_OPENING_COMMAND,
                    "Invalid command passed in the Request Param, In the request param please type valid id and status (approve or reject)")));
        }


        Optional<AccOpenDTO> accountOpenDto = accOpenService.updateById(id, accountStatus);
        log.info("Updated account opening request: ",accountOpenDto.get().getAccountType());
        return ResponseEntity.ok(ApiResponse.success(MapperUtil
                .convertTo(accountOpenDto.get(), AccOpenRes.class)));
    }


    @GetMapping(value = "/allRequests")
    public ResponseEntity<ApiResponse<List<AccOpenRes>>> getAll() {
        log.info("Received request to fetch all pending requests ");
        List<AccOpenDTO> dtos = accOpenService.getAll();
        if(dtos.size() == 0) return ResponseEntity.ok(ApiResponse.success(null));
        List<AccOpenRes> lists = new ArrayList<>();
        dtos.forEach((e) -> {
            lists.add(MapperUtil.convertTo(e, AccOpenRes.class));
        });
        return ResponseEntity.ok(ApiResponse.success(lists));
    }

}
