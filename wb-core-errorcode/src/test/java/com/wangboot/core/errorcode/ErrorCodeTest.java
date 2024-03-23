package com.wangboot.core.errorcode;

import java.io.Serializable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("错误码测试")
public class ErrorCodeTest {

  @Test
  public void testErrorCode() {
    Assertions.assertEquals("400", HttpErrorCode.BAD_REQUEST.getErrCode());
    Assertions.assertEquals(400, HttpErrorCode.BAD_REQUEST.getStatusCode());
    Assertions.assertEquals("bad_request", HttpErrorCode.BAD_REQUEST.getErrMsg());
    ErrorCodeException e1 =
        new ErrorCodeException(
            HttpErrorCode.BAD_REQUEST.getStatusCode(),
            HttpErrorCode.BAD_REQUEST.getErrCode(),
            HttpErrorCode.BAD_REQUEST.getErrMsg(),
            null,
            null);
    ErrorCodeException e2 = new ErrorCodeException(HttpErrorCode.BAD_REQUEST, null, null);
    Assertions.assertEquals(e1.getErrCode(), e2.getErrCode());
    Assertions.assertEquals(e1.getStatusCode(), e2.getStatusCode());
    Assertions.assertEquals(e1.getErrMsg(), e2.getErrMsg());
    Exception exp = new Exception();
    ErrorCodeException e3 =
        new ErrorCodeException(
            HttpErrorCode.NOT_FOUND.getStatusCode(),
            HttpErrorCode.NOT_FOUND.getErrCode(),
            HttpErrorCode.NOT_FOUND.getErrMsg(),
            exp);
    ErrorCodeException e4 = new ErrorCodeException(HttpErrorCode.NOT_FOUND, exp);
    Assertions.assertEquals(e3.getErrCode(), e4.getErrCode());
    Assertions.assertEquals(e3.getStatusCode(), e4.getStatusCode());
    Assertions.assertEquals(e3.getErrMsg(), e4.getErrMsg());
    Serializable[] serializables = new Serializable[] {"1", "2", "3"};
    ErrorCodeException e5 =
        new ErrorCodeException(
            HttpErrorCode.NOT_FOUND.getStatusCode(),
            HttpErrorCode.NOT_FOUND.getErrCode(),
            HttpErrorCode.NOT_FOUND.getErrMsg(),
            serializables);
    ErrorCodeException e6 = new ErrorCodeException(HttpErrorCode.NOT_FOUND, serializables);
    Assertions.assertEquals(e5.getErrCode(), e6.getErrCode());
    Assertions.assertEquals(e5.getStatusCode(), e6.getStatusCode());
    Assertions.assertEquals(e5.getErrMsg(), e6.getErrMsg());
    Assertions.assertEquals(e5.getArgs(), e6.getArgs());
    ErrorCodeException e7 =
        new ErrorCodeException(
            HttpErrorCode.NOT_FOUND.getStatusCode(),
            HttpErrorCode.NOT_FOUND.getErrCode(),
            HttpErrorCode.NOT_FOUND.getErrMsg());
    ErrorCodeException e8 = new ErrorCodeException(HttpErrorCode.NOT_FOUND);
    Assertions.assertEquals(e7.getErrCode(), e8.getErrCode());
    Assertions.assertEquals(e7.getStatusCode(), e8.getStatusCode());
    Assertions.assertEquals(e7.getErrMsg(), e8.getErrMsg());
    String msg = "msg";
    ErrorCodeException e9 = new ErrorCodeException(HttpErrorCode.BAD_REQUEST, msg);
    Assertions.assertEquals(msg, e9.getErrMsg());
  }
}
