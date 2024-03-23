package com.wangboot.core.auth.testcom;

import com.wangboot.core.auth.frontend.IFrontendModel;
import com.wangboot.core.auth.frontend.IFrontendService;
import com.wangboot.core.auth.frontend.impl.MockFrontend;
import java.util.Collections;
import java.util.List;
import org.assertj.core.util.Lists;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public class TestFrontendService implements IFrontendService {

  private IFrontendModel getWeb() {
    return new MockFrontend("1", "web", "WEB", false, true);
  }

  private IFrontendModel getAdmin() {
    return new MockFrontend("2", "admin", "ADMIN", true, false);
  }

  private IFrontendModel getApp() {
    return new MockFrontend("3", "app", "APP", false, false);
  }

  @Nullable
  @Override
  public IFrontendModel getFrontendModelById(String id) {
    switch (id) {
      case "1":
        return getWeb();
      case "2":
        return getAdmin();
      case "3":
        return getApp();
      default:
        return null;
    }
  }

  @Nullable
  @Override
  public IFrontendModel getFrontendModelByName(String name) {
    switch (name) {
      case "web":
        return getWeb();
      case "admin":
        return getAdmin();
      case "app":
        return getApp();
      default:
        return null;
    }
  }

  @NonNull
  @Override
  public List<IFrontendModel> getFrontendModelsByType(String type) {
    switch (type) {
      case "WEB":
        return Lists.list(getWeb());
      case "ADMIN":
        return Lists.list(getAdmin());
      case "APP":
        return Lists.list(getApp());
      default:
        return Collections.emptyList();
    }
  }
}
