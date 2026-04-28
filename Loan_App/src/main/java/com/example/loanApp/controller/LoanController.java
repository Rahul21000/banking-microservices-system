package com.example.loanApp.controller;

import com.example.loanApp.dto.RequestLoanDTO;
import com.example.loanApp.payload.ResponseJSON;
import com.example.loanApp.payload.ResponseUtils;
import com.example.loanApp.service.LoanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.math.BigDecimal;

@RestController
@RequestMapping("/api")
public class LoanController {
    @Autowired
    private LoanService loanService;
    @Autowired
    private ResponseUtils responseUtils;

    @GetMapping("/prediction")
    ResponseEntity<ResponseJSON> predictLoan(@RequestParam("principalAmount") BigDecimal principalAmount,
                                             @RequestParam("annualInterestRate") BigDecimal annualInterestRate,
                                             @RequestParam("tenureMonths") Integer tenureMonths){
        try{
            return loanService.handleLoanPrediction(principalAmount,annualInterestRate,tenureMonths);
        } catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @PostMapping("/create/loan")
    ResponseEntity<ResponseJSON> createLoan(@RequestBody RequestLoanDTO requestLoanDTO){
        try{
            return loanService.createLoan(requestLoanDTO);
        } catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }


    @GetMapping("/credit_normalization/{customerId}")
    ResponseEntity<ResponseJSON>creditNormalization (@PathVariable("customerId") Long customerId){
        try{
            return loanService.creditNormalization(customerId);
        } catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }


    @GetMapping("/credit_civil_score/{customerId}")
    ResponseEntity<ResponseJSON> civil_credit_Score(@PathVariable("customerId") Long customerId) {
        try{
            return loanService.CreditScore(customerId);
        } catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class)) }),
            @ApiResponse(responseCode = "404", description = "API not found",
                    content = @Content) })
    @GetMapping("/get-api-link")
    public String getApiLink() {
        return "Call the API link: http://localhost:8080/trigger-method/123";
    }

    @Operation(summary = "Trigger Method", description = "Trigger method using the provided ID")
    @GetMapping("/trigger-method/{id}")
    public String triggerMethod(@PathVariable String id) {
        return "Processing method triggered with ID: " + id;
    }

}
