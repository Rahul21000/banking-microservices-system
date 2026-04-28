package com.rbank.transaction_service.service;

import com.rbank.transaction_service.dto.AccountNoStatusDTO;
import com.rbank.transaction_service.dto.RequestTransactionDTO;
import com.rbank.transaction_service.dto.ResponseJson;
import com.rbank.transaction_service.enums.Status;
import com.rbank.transaction_service.model.PaymentMethod;
import com.rbank.transaction_service.enums.TransactionMethod;
import com.rbank.transaction_service.enums.TransactionType;
import com.rbank.transaction_service.exception.InsufficientAmountException;
import com.rbank.transaction_service.model.Transaction;
import com.rbank.transaction_service.repository.TransactionMethodRepository;
import com.rbank.transaction_service.repository.TransactionRepository;
import com.rbank.transaction_service.utils.ResponseUtils;
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

import static com.rbank.transaction_service.enums.TransactionMethod.BANK;

@Service
public class TransactionServiceImpl implements TransactionService{

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private TransactionMethodRepository transactionMethodRepository;
    @Autowired
    private ResponseUtils responseUtils;
    @Autowired
    private AccountServiceClient accountServiceClient;

    @Override
    @Transactional
    public ResponseEntity<ResponseJson> handleInitialOpeningTransaction(Long customerId) {
        BigDecimal initialAmount = BigDecimal.valueOf(0.0);
        BigDecimal initialTotalAmount = BigDecimal.valueOf(0.0);
        Transaction initialTransaction = new Transaction();
        initialTransaction.setAmount(initialAmount);
        initialTransaction.setTransactionId(generateTractionId());
        initialTransaction.setTransactionMethod(handleGetTransactionMethod(BANK));
        initialTransaction.setTransactionType(TransactionType.CREDIT);
        initialTransaction.setTotalBalance(initialTotalAmount);
        Transaction createdTransaction = transactionRepository.save(initialTransaction);
        return responseUtils.handleResponseInPayload(createdTransaction,"amount credited successfully",201, HttpStatus.CREATED);
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseJson> handleDepositAmount(RequestTransactionDTO requestTransactionDTO) throws RuntimeException {
        AccountNoStatusDTO accountNoStatusDTO = handleCheckAccountStatus(requestTransactionDTO.getCustomerId());
        BigDecimal totalBalance = getTotalByAccountNo(accountNoStatusDTO.getAccountNo());
        Transaction transaction = handleCreditAndDebitTransaction(accountNoStatusDTO.getAccountNo(),totalBalance,requestTransactionDTO,"CREDIT");
        Transaction newTransaction = transactionRepository.save(transaction);
        return responseUtils.handleResponseInPayload(newTransaction,"amount credited successfully",200, HttpStatus.OK);
    }


    @Override
    @Transactional
    public ResponseEntity<ResponseJson> handleWithdrawalAmount(RequestTransactionDTO requestTransactionDTO) throws InsufficientAmountException,RuntimeException {
        AccountNoStatusDTO accountNoStatusDTO = handleCheckAccountStatus(requestTransactionDTO.getCustomerId());
        BigDecimal totalBalance = getTotalByAccountNo(accountNoStatusDTO.getAccountNo());
        if(totalBalance.intValue() < requestTransactionDTO.getAmount().intValue()){
            throw new InsufficientAmountException("Balance is insufficient for withdrawal of " + requestTransactionDTO.getCustomerId());
        } else {
            Transaction updatedTransaction = handleCreditAndDebitTransaction(accountNoStatusDTO.getAccountNo(),totalBalance,requestTransactionDTO,"DEBIT");
            return responseUtils.handleResponseInPayload(updatedTransaction,"amount withdrawal successfully",201,HttpStatus.CREATED);
        }
    }

    private AccountNoStatusDTO handleCheckAccountStatus(Long customerId) {
        AccountNoStatusDTO exitingAccountStatus = accountServiceClient.getAccountNoAndStatusByCustomerId(customerId);
        Status status = exitingAccountStatus.getStatus();
        if (status.equals(Status.DE_ACTIVE)) {
            throw new RuntimeException("Account is DE_ACTIVE");
        } else if (status.equals(Status.PENDING)) {
            throw new RuntimeException("Account is Pending");
        } else if (status.equals(Status.IN_PROGRESS)) {
            throw new RuntimeException("Account is IN_PROGRESS");
        } else if (status.equals(Status.CLOSE)) {
            throw new RuntimeException("Account is CLOSE");
        } else {
            return exitingAccountStatus;
        }
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseJson> handleCheckBalance(Long customerId) {
        AccountNoStatusDTO accountNoStatusDTO = handleCheckAccountStatus(customerId);
        BigDecimal getTotal = transactionRepository.findTotalBalanceByAccountNo(accountNoStatusDTO.getAccountNo());
        return responseUtils.handleResponseInPayload(getTotal,"Total Balance",200,HttpStatus.OK);
    }
    
    private Transaction handleCreditAndDebitTransaction(Long accountNo,BigDecimal totalBalance,RequestTransactionDTO requestTransactionDTO, String type) {
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
        transaction.setAccountNo(accountNo);
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

    private Long getAccountByCustomerId(Long customerId){
       return 1L;
    }
}
