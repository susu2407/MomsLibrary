package com.susuproject.MomsLibrary.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
 
// 프로젝트 전체에서 발생하는 특정 예외들을 한 곳에서 처리
// (컨트롤러마다 try-catch를 반복하지 않기 위함)
// @ControllerAdvice 는 " 이 예외가 어디서 터지든, 이 한 곳에서 잡아서 처리한다"는 걸 의미
@ControllerAdvice
public class GlobalExceptionHandler {
 
    // DocumentNotFoundException이 발생하면 이 메서드가 대신 처리함
    @ExceptionHandler(DocumentNotFoundException.class)
    public String handleDocumentNotFound(DocumentNotFoundException ex,
                                          RedirectAttributes redirectAttributes) {
 
        // 목록 화면으로 돌아갈 때 에러 메시지를 함께 전달 (새로고침해도 사라지지 않는 1회성 메시지)
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
 
        // 목록 화면으로 리다이렉트
        return "redirect:/documents";
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public String handleCategoryNotFound(CategoryNotFoundException ex,
                                        RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        return "redirect:/categories";
    }

    @ExceptionHandler(TagNotFoundException.class)
    public String handleTagNotFound(TagNotFoundException ex,
                                    RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        return "redirect:/categories";
    }   
}