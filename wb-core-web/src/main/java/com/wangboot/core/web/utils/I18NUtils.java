package com.wangboot.core.web.utils;

import cn.hutool.core.exceptions.UtilException;
import cn.hutool.extra.spring.SpringUtil;
import lombok.Generated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * 国际化工具类
 *
 * @author wwtg99
 */
@Slf4j
public class I18NUtils {
  private I18NUtils() {}

  /**
   * 根据消息键和参数 获取消息 委托给spring messageSource
   *
   * @param code 消息键
   * @param args 参数
   * @return 获取国际化翻译值
   */
  @Generated
  public static String message(String code, Object... args) {
    try {
      MessageSource messageSource = SpringUtil.getBean(MessageSource.class);
      return messageSource.getMessage(code, args, code, LocaleContextHolder.getLocale());
    } catch (UtilException | NoSuchBeanDefinitionException e) {
      log.error(e.getMessage());
      return code;
    }
  }
}
