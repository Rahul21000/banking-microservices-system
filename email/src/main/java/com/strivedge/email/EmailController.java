package com.strivedge.email;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/email")
@Validated
public class EmailController {
    @Autowired
    private EmailService emailService;
    
    @PostMapping("/winning-amount")
    public String email_controller(@Valid @RequestBody Community community){
        try{
            emailService.winningAmountEmailSentService(community);
            return "Email sent successfully!";
        } catch (MessagingException e){
            e.printStackTrace();
            return "Error sending email: " + e.getMessage();
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleValidationException(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        StringBuilder errorMessage = new StringBuilder("Validation errors: ");
        for (FieldError fieldError : fieldErrors) {
            errorMessage.append(fieldError.getField()).append(": ")
                    .append(fieldError.getDefaultMessage()).append("; ");
        }
        return errorMessage.toString();
    }
}
