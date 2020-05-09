package com.sakhri.userService.exceptions;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

public class ValidationError {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> errors = new ArrayList<>();
    
    public void addValidationError(String error) {
        errors.add(error);
    }
    
    public List<String> getErrors() {
        return errors;
    }
}