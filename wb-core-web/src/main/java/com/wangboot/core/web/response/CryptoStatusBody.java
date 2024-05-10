package com.wangboot.core.web.response;

import com.wangboot.core.crypto.CryptoBody;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import org.springframework.http.HttpStatus;

/**
 * 加密响应体
 *
 * @author wwtg99
 */
@Data
@AllArgsConstructor
public class CryptoStatusBody implements IStatusBody {

  private String jsondata;

  private String id;

  private String mode;

  private int status = HttpStatus.OK.value();

  @Generated
  public CryptoStatusBody(CryptoBody cryptoBody) {
    this.jsondata = cryptoBody.getJsondata();
    this.id = cryptoBody.getId();
    this.mode = cryptoBody.getMode();
  }
}
