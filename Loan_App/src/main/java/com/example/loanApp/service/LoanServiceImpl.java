package com.example.loanApp.service;

import com.example.loanApp.dto.RequestLoanDTO;
import com.example.loanApp.model.Account;
import com.example.loanApp.model.Loan;
import com.example.loanApp.model.Transaction;
import com.example.loanApp.payload.ResponseJSON;
import com.example.loanApp.payload.ResponseUtils;
import com.example.loanApp.repository.LoanRepository;
import com.example.loanApp.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

@Service
public class LoanServiceImpl implements LoanService{
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private ResponseUtils responseUtils;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountService accountService;

    private static final double PAYMENT_HISTORY_WEIGHT = 0.35;
    private static final double CREDIT_UTILIZATION_WEIGHT = 0.30;
    private static final double CREDIT_HISTORY_LENGTH_WEIGHT = 0.15;
    private static final double TYPES_OF_CREDIT_WEIGHT = 0.10;
    private static final double NEW_CREDIT_WEIGHT = 0.10;


//    formula for calculate emi
//   EMI= P⋅r⋅(1+r)^n/(1+r)^n−1

    private static final Logger logger = LoggerFactory.getLogger(LoanServiceImpl.class);

    @Override
    public ResponseEntity<ResponseJSON> createLoan(RequestLoanDTO requestLoanDTO) {
        BigDecimal principalAmount = requestLoanDTO.getPrincipalAmount();
        BigDecimal annualInterestRate = requestLoanDTO.getAnnualInterestRate();
        Integer tenureMonths = requestLoanDTO.getTenureMonths();
        if (principalAmount == null || annualInterestRate == null || tenureMonths == null) {
            throw new RuntimeException("Invalid input parameters for loan prediction.");
        }
        Account account = accountService.getAccountByCustomerId(requestLoanDTO.getCustomerId());
        BigDecimal emiAmount = calculateEMI(principalAmount.intValue(), annualInterestRate.doubleValue(), requestLoanDTO.getTenureMonths());
        Loan loan = new Loan();
        loan.setLoanId(handleGenerateUniqueLoanId());
        loan.setPrincipalAmount(principalAmount);
        loan.setInterestRate(annualInterestRate);
        loan.setTenureMonths(tenureMonths);
        loan.setEmiAmount(emiAmount);
        loan.setAccount(account);
        Loan loanCreate = loanRepository.save(loan);
        return responseUtils.handleResponseInPayload(loanCreate,"loan created successfully",201, HttpStatus.CREATED);
    }

    private static Long handleGenerateUniqueLoanId() {
        Random random = new Random();
        return  10000000L + random.nextInt(90000000);
    }

    @Override
    public ResponseEntity<ResponseJSON> handleLoanPrediction(BigDecimal principalAmount, BigDecimal annualInterestRate, Integer tenureMonths) {
        logger.info("Calculating EMI for a loan of {} with an annual interest rate of {}% for {} months.",
                principalAmount, annualInterestRate, tenureMonths);
        BigDecimal emiAmount = calculateEMI(principalAmount.intValue(), annualInterestRate.doubleValue(), tenureMonths);
        logger.debug("Calculated monthly interest rate: {}", emiAmount);
        Map<String,BigDecimal> map = new HashMap<>();
        map.put("emiAmount",emiAmount);
        map.put("principalAmount",principalAmount);
        map.put("interestRate",annualInterestRate);
        map.put("tenureMonths",BigDecimal.valueOf(tenureMonths));
        return responseUtils.handleResponseInPayload(map,"calculation of emi",200,HttpStatus.OK);
    }



    private BigDecimal calculateEMI(int principalAmount, double annualInterestRate, int tenureMonths) {
        if (annualInterestRate == 0) {
            double result = (double) principalAmount / tenureMonths;
            return BigDecimal.valueOf(result).setScale(2,RoundingMode.CEILING);
        }
        double monthlyInterestRate = annualInterestRate / 12 / 100;
        double result = (principalAmount * monthlyInterestRate * Math.pow(1 + monthlyInterestRate, tenureMonths)) /
                (Math.pow(1 + monthlyInterestRate, tenureMonths) - 1);
        return BigDecimal.valueOf(result).setScale(2,RoundingMode.CEILING);
    }


    @Override
    public ResponseEntity<ResponseJSON> creditNormalization(Long customerId) {
        Map<String,Integer> creditLimit = new HashMap<>();
        creditLimit.put("CreditLimit",determineCreditLimit(customerId));
        return responseUtils.handleResponseInPayload(creditLimit,"Your credit limit",200,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseJSON> CreditScore(Long customerId) {
        double paymentHistory = 95; // Percentage of on-time payments
        double creditUtilization = 30; // Credit utilization as a percentage (0-100)
        double creditHistoryLength = 15; // Length of credit history in years
        double typesOfCredit = 4; // Number of different types of credit (e.g., credit cards, mortgage, loans)
        double newCredit = 2; // Number of recent credit inquiries in the last year

        double creditScore = calculateCreditScore(paymentHistory, creditUtilization, creditHistoryLength, typesOfCredit, newCredit);
        return responseUtils.handleResponseInPayload(creditScore,"creditScore",200,HttpStatus.OK);
    }

    private BigDecimal calculateMonthlyTransactionAmount(Long customerId, LocalDate month) {
        // Calculate the start and end date of the month
        LocalDate startDate = month.withDayOfMonth(1);
        LocalDate endDate = month.withDayOfMonth(month.lengthOfMonth());
        logger.info("Calculating Monthly transaction Amount for a loan from startDate {} to endDate {}",
                startDate, endDate);
        Long accountNo = accountService.getAccountByCustomerId(customerId).getAccountNo();
        List<Transaction> transactions = transactionRepository.findByAccountAccountNoAndTransactionDateBetween(accountNo, startDate, endDate);
        BigDecimal totalTransactionAmount = transactions.stream()
                .map(Transaction::getAmount).reduce(BigDecimal.ZERO,BigDecimal::add);
        logger.debug("Calculated monthly totalTransactionAmount: {}", totalTransactionAmount);
        return  totalTransactionAmount;
    }

    private int determineCreditLimit(Long customerId) {
        LocalDate localDate = LocalDate.now();
        BigDecimal totalTransactionAmount = calculateMonthlyTransactionAmount(customerId, localDate);
        // Here, we calculate the credit limit based on 20% of the total monthly transactions
        int creditLimit = (int) (totalTransactionAmount.intValue() * 0.2);
        int number = creditLimit / 100;
        return number * 100;
    }

    public static double calculateCreditScore(double paymentHistory, double creditUtilization, double creditHistoryLength, double typesOfCredit, double newCredit) {
        // Normalize values to 0-100 scale
        double normalizedPaymentHistory = paymentHistory; // Assume this is already a percentage of on-time payments
        double normalizedCreditUtilization = 100 - creditUtilization; // Lower utilization is better, so subtract from 100
        double normalizedCreditHistoryLength = Math.min(creditHistoryLength, 30); // Max 30 years of credit history for calculation
        double normalizedTypesOfCredit = Math.min(typesOfCredit, 10); // Max 10 types of credit for calculation
        double normalizedNewCredit = Math.max(0, 5 - newCredit); // Lower recent inquiries are better

        // Calculate weighted score for each factor
        double score = (normalizedPaymentHistory * PAYMENT_HISTORY_WEIGHT) +
                (normalizedCreditUtilization * CREDIT_UTILIZATION_WEIGHT) +
                (normalizedCreditHistoryLength * CREDIT_HISTORY_LENGTH_WEIGHT) +
                (normalizedTypesOfCredit * TYPES_OF_CREDIT_WEIGHT) +
                (normalizedNewCredit * NEW_CREDIT_WEIGHT);

        return score;
    }

    private static String determineRisk(double score) {
        if (score >= 750) {
            return "Excellent";
        } else if (score >= 700) {
            return "Good";
        } else if (score >= 650) {
            return "Fair";
        } else if (score >= 600) {
            return "Poor";
        } else {
            return "Very Poor";
        }
    }

}
