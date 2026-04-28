package com.example.loanApp.service;

import com.example.loanApp.enums.TransactionMethod;
import com.example.loanApp.model.PaymentMethod;
import com.example.loanApp.payload.ResponseJSON;
import com.example.loanApp.payload.ResponseUtils;
import com.example.loanApp.repository.TransactionMethodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SuperAdminServiceImpl implements SuperAdminService{

    @Autowired
    private TransactionMethodRepository transactionMethodRepository;

    @Autowired
    private ResponseUtils responseUtils;
    @Override
    public ResponseEntity<ResponseJSON> handleCreateTransactionMethod(TransactionMethod paymentMethodName) {
        if(transactionMethodRepository.existsByName(paymentMethodName)){
            throw new RuntimeException("Name already exist");
        }
        PaymentMethod paymentMethod = handlePaymentMethod(paymentMethodName);

        return responseUtils.handleResponseInPayload(transactionMethodRepository.save(paymentMethod),"Transaction Type Created Successful",201, HttpStatus.CREATED);
    }

    private PaymentMethod handlePaymentMethod(TransactionMethod paymentMethodName) {
        PaymentMethod paymentMethod = new PaymentMethod();
        switch (paymentMethodName){
            case UPI:
                paymentMethod.setName(TransactionMethod.UPI);
                break;
            case ONLINE:
                paymentMethod.setName(TransactionMethod.ONLINE);
                break;
            case DEBIT_CARD:
                paymentMethod.setName(TransactionMethod.DEBIT_CARD);
                break;
            case CREDIT_CARD:
                paymentMethod.setName(TransactionMethod.CREDIT_CARD);
                break;
            default:
                throw new IllegalArgumentException("Invalid method: ");
        }
        return paymentMethod;
    }

    @Override
    public ResponseEntity<ResponseJSON> handleGetTransactionMethod(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseJSON> handleUpdateTransactionMethod(Long id, TransactionMethod paymentMethodName) {
        Optional<PaymentMethod> existTransactionMethod = transactionMethodRepository.findById(id);
        if(existTransactionMethod.isEmpty()){
            throw new RuntimeException("Transaction Method not found");
        }
        PaymentMethod paymentMethod = handlePaymentMethod(paymentMethodName);
        paymentMethod.setId(id);
        return responseUtils.handleResponseInPayload(transactionMethodRepository.save(existTransactionMethod.get()),"Transaction Method(type) Created Successful",200,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseJSON> handleDeleteTransactionMethod(Long id) {
        if(id == null){
            throw new IllegalArgumentException("Id should not be null");
        } else if (!transactionMethodRepository.existsById(id)) {
            throw new RuntimeException("Id does not exist");
        }
        else{
            transactionMethodRepository.deleteById(id);
           return responseUtils.handleResponseInPayload(null,"Transaction Method deleted Successfully",200,HttpStatus.OK);
        }
    }
}
