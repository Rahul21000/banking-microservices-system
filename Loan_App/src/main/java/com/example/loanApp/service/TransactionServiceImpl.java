package com.example.loanApp.service;

import com.example.loanApp.enums.Status;
import com.example.loanApp.dto.transaction.RequestTransactionDTO;
import com.example.loanApp.model.PaymentMethod;
import com.example.loanApp.payload.ResponseJSON;
import com.example.loanApp.enums.TransactionMethod;
import com.example.loanApp.enums.TransactionType;
import com.example.loanApp.exception.InsufficientAmountException;
import com.example.loanApp.model.Account;
import com.example.loanApp.model.Transaction;
import com.example.loanApp.payload.ResponseUtils;
import com.example.loanApp.repository.TransactionMethodRepository;
import com.example.loanApp.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static com.example.loanApp.enums.TransactionMethod.BANK;

@Service
public class TransactionServiceImpl implements TransactionService{

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private TransactionMethodRepository transactionMethodRepository;
    @Autowired
    private ResponseUtils responseUtils;
    @Autowired
    private AccountService accountService;

    @Override
    @Transactional
    public Transaction handleInitialOpeningTransaction(Account account) {
        BigDecimal initialAmount = BigDecimal.valueOf(0.0);
        BigDecimal initialTotalAmount = BigDecimal.valueOf(0.0);
        Transaction initialTransaction = new Transaction();
        initialTransaction.setAmount(initialAmount);
        initialTransaction.setTransactionId(generateTractionId());
        initialTransaction.setTransactionMethod(handleGetTransactionMethod(BANK));
        initialTransaction.setTransactionType(TransactionType.CREDIT);
        initialTransaction.setAccount(account);
        initialTransaction.setTotalBalance(initialTotalAmount);
        transactionRepository.save(initialTransaction);
        return  initialTransaction;
    }

    @Override
    public ResponseEntity<ResponseJSON> handleCloseAccount(Long customerID) {
        accountService.changeAccountStatus(customerID,Status.CLOSE);
        return responseUtils.handleResponseInPayload(null,"amount close successfully",204, HttpStatus.NO_CONTENT);
    }


    @Override
    @Transactional
    public ResponseEntity<ResponseJSON> handleDepositAmount(RequestTransactionDTO requestTransactionDTO) throws RuntimeException {
        Account account = handleAccountStatus(requestTransactionDTO.getCustomerId());
        BigDecimal totalBalance = getTotalByAccountNo(account.getAccountNo());
        Transaction transaction = handleCreditAndDebitTransaction(account,totalBalance,requestTransactionDTO,"CREDIT");
        Transaction newTransaction = transactionRepository.save(transaction);
        return responseUtils.handleResponseInPayload(newTransaction,"amount credited successfully",200, HttpStatus.OK);
    }


    @Override
    @Transactional
    public ResponseEntity<ResponseJSON> handleWithdrawalAmount(RequestTransactionDTO requestTransactionDTO) throws InsufficientAmountException,RuntimeException {
        Account account = handleAccountStatus(requestTransactionDTO.getCustomerId());
        BigDecimal totalBalance = getTotalByAccountNo(account.getAccountNo());
        if(totalBalance.intValue() < requestTransactionDTO.getAmount().intValue()){
            throw new InsufficientAmountException("Balance is insufficient for withdrawal of " + requestTransactionDTO.getCustomerId());
        } else {
            Transaction updatedTransaction = handleCreditAndDebitTransaction(account,totalBalance,requestTransactionDTO,"DEBIT");
            return responseUtils.handleResponseInPayload(updatedTransaction,"amount withdrawal successfully",201,HttpStatus.CREATED);
        }
    }

    private Account handleAccountStatus(Long customerId) {
        Account existAccount = accountService.getAccountByCustomerId(customerId);
        Status status = existAccount.getAccountStatus();
        if (status.equals(Status.DE_ACTIVE)) {
            throw new RuntimeException("Account is DE_ACTIVE");
        } else if (status.equals(Status.PENDING)) {
            throw new RuntimeException("Account is Pending");
        } else if (status.equals(Status.IN_PROGRESS)) {
            throw new RuntimeException("Account is IN_PROGRESS");
        } else if (status.equals(Status.CLOSE)) {
            throw new RuntimeException("Account is CLOSE");
        } else {
            return existAccount;
        }
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseJSON> handleCheckBalance(Long customerId) {
        Account existAccount = accountService.getAccountByCustomerId(customerId);
        BigDecimal getTotal = transactionRepository.findTotalBalanceByAccountNo(existAccount.getAccountNo());
        return responseUtils.handleResponseInPayload(getTotal,"Total Balance",200,HttpStatus.OK);
    }
    
    private Transaction handleCreditAndDebitTransaction(Account account,BigDecimal totalBalance,RequestTransactionDTO requestTransactionDTO, String type) {
        Transaction transaction = new Transaction();
        if (type.equalsIgnoreCase(String.valueOf(TransactionType.CREDIT))) {
            transaction.setAmount(requestTransactionDTO.getAmount());
            totalBalance = totalBalance.add(requestTransactionDTO.getAmount());
            transaction.setTransactionType(TransactionType.CREDIT);
        }
        if (type.equalsIgnoreCase(String.valueOf(TransactionType.DEBIT))) {
            transaction.setAmount(requestTransactionDTO.getAmount());
            totalBalance = totalBalance.subtract(requestTransactionDTO.getAmount());
            transaction.setTransactionType(TransactionType.DEBIT);
        }
        switch (handleGetTransactionMethod(requestTransactionDTO.getTransactionMethod())){
            case UPI:
                transaction.setTransactionMethod(TransactionMethod.UPI);
                break;
            case ONLINE:
                transaction.setTransactionMethod(TransactionMethod.ONLINE);
                break;
            case DEBIT_CARD:
                transaction.setTransactionMethod(TransactionMethod.DEBIT_CARD);
                break;
            case CREDIT_CARD:
                transaction.setTransactionMethod(TransactionMethod.CREDIT_CARD);
                break;
            default:
                throw new IllegalArgumentException("Invalid method: ");
        }
        transaction.setTransactionId(generateTractionId());
        transaction.setTotalBalance(totalBalance);
        transaction.setAccount(account);
        return transactionRepository.save(transaction);
    }

    private TransactionMethod  handleGetTransactionMethod(TransactionMethod transactionMethodName) {
        Optional<PaymentMethod> transactionMethod = transactionMethodRepository.getTransactionMethodByName(transactionMethodName);
        if(transactionMethod.isEmpty()){
            throw new RuntimeException("Transaction Method not found");
        }
        return transactionMethod.get().getName();
    }

    public BigDecimal getTotalByAccountNo(Long accountNo){
        return transactionRepository.findTotalBalanceByAccountNo(accountNo);
    }

    private static String generateTractionId() {
        long timestamp = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String date = dateFormat.format(new Date());
        String randomUUID = UUID.randomUUID().toString().substring(0, 8);
        return date + "-" + randomUUID + "-" + timestamp;
    }
}
