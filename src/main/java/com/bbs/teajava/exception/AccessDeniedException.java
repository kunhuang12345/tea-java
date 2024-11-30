package com.bbs.teajava.exception;

import lombok.Getter;

/**
 * @author kunhuang
 */
@Getter
public class AccessDeniedException extends BusinessException{
    public AccessDeniedException(String message) {
        super(403, message);
    }
}
