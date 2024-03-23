package com.wangboot.core.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 路径工具类
 *
 * @author wwtg99
 */
public class PathUtils {
  private PathUtils() {}

  /**
   * 遍历文件夹
   *
   * @param path 文件夹路径
   * @param recursive 是否递归
   * @return 文件列表
   */
  public static List<File> listDirectory(String path, boolean recursive) {
    File dir = new File(path);
    List<File> finds = new ArrayList<>();
    if (dir.exists() && dir.isDirectory()) {
      File[] files = dir.listFiles();
      if (Objects.nonNull(files)) {
        for (File f : files) {
          if (recursive) {
            if (f.isDirectory()) {
              finds.addAll(listDirectory(f.getAbsolutePath(), true));
            } else {
              finds.add(f);
            }
          } else {
            finds.add(f);
          }
        }
      }
    }
    return finds;
  }
}
