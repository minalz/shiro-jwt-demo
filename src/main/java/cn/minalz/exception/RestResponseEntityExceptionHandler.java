package cn.minalz.exception;

import cn.minalz.common.ResponseData;
import cn.minalz.utils.Common;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(Exception.class)
  @ResponseBody
  public ResponseEntity<ResponseData> privateHandleException(Exception ex, WebRequest request) {
    switch (ex.getClass().getName()) {
    case "cn.minalz.exception.CoreException":
      return new ResponseEntity<ResponseData>(ResponseData.oferror(ex.getMessage()), HttpStatus.OK);
    default:
      log.error(ex.getMessage(),ex);
      return new ResponseEntity<ResponseData>(ResponseData.oferror(Common.NORMAL_ERROR), HttpStatus.OK);
    }
  }

}