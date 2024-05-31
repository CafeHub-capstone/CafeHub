package com.cafehub.cafehub.bookmark.handler;
import com.cafehub.cafehub.bookmark.exception.MemberBookmarkAlreadyExistException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = Controller.class)
public class BookmarkExceptionHandler {

//    @ExceptionHandler(MemberBookmarkAlreadyExistException.class)
//    public OOErrorResponse (){
//
//        errMessage = "이미 북마크 있음 "
//        return;
//    }

}
