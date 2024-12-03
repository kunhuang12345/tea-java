package com.bbs.teajava.exception;

import com.bbs.teajava.utils.ApiResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * @author kunhuang
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    // 处理业务异常
    @ExceptionHandler(BusinessException.class)
    public ApiResultUtils handleBusinessException(BusinessException e) {
        log.warn("业务异常: {}", e.getMessage());
        return ApiResultUtils.error(e.getCode(), e.getMessage());
    }

    // 处理参数校验异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResultUtils handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        log.warn("参数校验失败: {}", message);
        return ApiResultUtils.error(400, message);
    }

    // 处理参数类型错误
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ApiResultUtils handleTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.warn("参数类型错误: {}", e.getMessage());
        return ApiResultUtils.error(400, "参数类型错误");
    }

    // 身份验证失败
    @ExceptionHandler(UnauthorizedException.class)
    public ApiResultUtils handleUnauthorizedException(UnauthorizedException e) {
        log.warn("身份验证失败: {}", e.getMessage());
        return ApiResultUtils.error(401, "未登录");
    }


    // 处理权限不足
    @ExceptionHandler(AccessDeniedException.class)
    public ApiResultUtils handleAccessDeniedException(AccessDeniedException e) {
        log.warn("权限不足: {}", e.getMessage());
        return ApiResultUtils.error(403, e.getMessage());
    }

    // 处理资源未找到
    @ExceptionHandler(NoSuchElementException.class)
    public ApiResultUtils handleNotFoundException(NoSuchElementException e) {
        log.warn("资源未找到: {}", e.getMessage());
        return ApiResultUtils.error(404, "资源未找到");
    }

    // 处理其它未知异常
    @ExceptionHandler(Exception.class)
    public ApiResultUtils handleException(Exception e) {
        log.error(e.getMessage(), e);
        return ApiResultUtils.error(500, e.getMessage());
    }
}
