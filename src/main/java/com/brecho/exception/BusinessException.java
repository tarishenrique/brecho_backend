package com.brecho.exception;

public class BusinessException extends RuntimeException{

    public BusinessException(String mensagem){
        super(mensagem);
    }

}
