package com.wangboot.core.test.auth;

import com.wangboot.core.auth.context.AuthContextHolder;
import com.wangboot.core.auth.context.IAuthContext;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Objects;
import java.util.function.Supplier;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.Nullable;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

/**
 * 单元测试认证注入监听器
 *
 * @author wwtg99
 */
@Slf4j
public class WithAuthContextTestExecutionListener extends AbstractTestExecutionListener {

  @Override
  public void beforeTestExecution(TestContext testContext) {
    Supplier<IAuthContext> supplier =
        this.createTestAuthContext(testContext.getTestMethod(), testContext);
    if (Objects.nonNull(supplier)) {
      AuthContextHolder.setContext(supplier.get());
    }
  }

  @Nullable
  private Supplier<IAuthContext> createTestAuthContext(
      @NonNull AnnotatedElement annotated, @NonNull TestContext context) {
    WithAuthContext withAuthContext =
        AnnotatedElementUtils.findMergedAnnotation(annotated, WithAuthContext.class);
    return this.createTestAuthContext(annotated, withAuthContext, context);
  }

  @Nullable
  @SuppressWarnings("unchecked")
  private Supplier<IAuthContext> createTestAuthContext(
      @NonNull AnnotatedElement annotated,
      @Nullable WithAuthContext withAuthContext,
      @NonNull TestContext context) {
    if (Objects.isNull(withAuthContext)) {
      return null;
    } else {
      withAuthContext = AnnotationUtils.synthesizeAnnotation(withAuthContext, annotated);
      WithAuthContextFactory factory = this.createFactory(withAuthContext, context);
      Annotation annotation = this.findAnnotation(annotated);
      if (Objects.isNull(annotation)) {
        return null;
      }
      log.info("Inject {} to test context", annotation);
      return () -> {
        try {
          return factory.createContext(annotation);
        } catch (RuntimeException e) {
          throw new IllegalStateException("Unable to create AuthContext using " + annotation, e);
        }
      };
    }
  }

  @NonNull
  private WithAuthContextFactory<? extends Annotation> createFactory(
      @NonNull WithAuthContext withAuthContext, @NonNull TestContext testContext) {
    Class<?> clazz = withAuthContext.factory();
    try {
      return (WithAuthContextFactory<? extends Annotation>)
          testContext.getApplicationContext().getAutowireCapableBeanFactory().createBean(clazz);
    } catch (IllegalStateException var5) {
      return (WithAuthContextFactory<? extends Annotation>) BeanUtils.instantiateClass(clazz);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Nullable
  private Annotation findAnnotation(@Nullable AnnotatedElement annotated) {
    if (Objects.isNull(annotated)) {
      return null;
    }
    WithAdminUser withAdminUser = annotated.getDeclaredAnnotation(WithAdminUser.class);
    if (Objects.nonNull(withAdminUser)) {
      return withAdminUser;
    }
    WithStaffUser withStaffUser = annotated.getDeclaredAnnotation(WithStaffUser.class);
    if (Objects.nonNull(withStaffUser)) {
      return withStaffUser;
    }
    return annotated.getDeclaredAnnotation(WithMockUser.class);
  }
}
