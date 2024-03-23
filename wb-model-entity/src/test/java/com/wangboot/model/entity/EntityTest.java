package com.wangboot.model.entity;

import cn.hutool.core.bean.BeanException;
import cn.hutool.core.util.RandomUtil;
import com.wangboot.core.web.response.DetailBody;
import com.wangboot.core.web.response.ErrorBody;
import com.wangboot.core.web.response.ListBody;
import com.wangboot.core.web.response.MessageBody;
import com.wangboot.model.entity.utils.EntityUtils;
import java.time.OffsetDateTime;
import java.util.Collections;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Entity测试")
public class EntityTest {
  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  static class ObjBody {
    private String name;
    private int number;
    private boolean active;
  }

  @Data
  @AllArgsConstructor
  static class AppendOnlyObject implements IAppendOnlyEntity {
    private String createdBy;
    private OffsetDateTime createdTime;
  }

  @Data
  @AllArgsConstructor
  static class CommonObject implements ICommonEntity {
    private String createdBy;
    private OffsetDateTime createdTime;
    private String updatedBy;
    private OffsetDateTime updatedTime;
    private String remark;
  }

  @Data
  @AllArgsConstructor
  static class ReadOnlyObject implements IEditableEntity {
    private boolean readonly;

    @Override
    public boolean readonly() {
      return this.readonly;
    }
  }

  @Data
  @AllArgsConstructor
  static class TreeObject implements ITreeEntity<String> {
    private String id;
    private String parentId;
    private Integer sort;

    @Override
    public boolean hasChildren() {
      return false;
    }
  }

  @Data
  static class UniqueObject implements IUniqueEntity {
    @Override
    public String[][] getUniqueTogetherFields() {
      return new String[0][];
    }
  }

  @Test
  public void testModels() {
    ObjBody body = new ObjBody("a", 1, true);
    String s = RandomUtil.randomString(6);
    DetailBody<ObjBody> detailBody = DetailBody.created(body);
    Assertions.assertEquals(201, detailBody.getStatus());
    Assertions.assertEquals(body, detailBody.getData());
    detailBody = DetailBody.updated(body);
    Assertions.assertEquals(200, detailBody.getStatus());
    Assertions.assertEquals(body.getName(), detailBody.getData().getName());
    detailBody = DetailBody.ok(body);
    Assertions.assertEquals(200, detailBody.getStatus());
    Assertions.assertEquals(body.getName(), detailBody.getData().getName());
    detailBody = new DetailBody<>(s, body, 200);
    Assertions.assertEquals(s, detailBody.getMessage());
    ErrorBody errorBody = new ErrorBody(RandomUtil.randomString(10), s, 500);
    Assertions.assertEquals(s, errorBody.getMessage());
    Assertions.assertEquals(500, errorBody.getStatus());
    ListBody<ObjBody> listBody = new ListBody<>(Collections.singleton(body), 1L, 1L, 1L, 200);
    Assertions.assertEquals(1, listBody.getData().size());
    Assertions.assertEquals(1, listBody.getPage());
    listBody = ListBody.ok(Collections.singleton(body));
    Assertions.assertEquals(1, listBody.getData().size());
    Assertions.assertEquals(1, listBody.getPage());
    Assertions.assertEquals(1, listBody.getTotal());
    MessageBody messageBody = new MessageBody(s, 200);
    Assertions.assertEquals(s, messageBody.getMessage());
    Assertions.assertEquals(200, messageBody.getStatus());
  }

  @Test
  public void testEntityUtils() {
    Object obj = new Object();
    // isAppendOnlyEntity
    AppendOnlyObject appendOnlyObject =
        new AppendOnlyObject(RandomUtil.randomString(6), OffsetDateTime.now());
    Assertions.assertTrue(EntityUtils.isAppendOnlyEntity(appendOnlyObject));
    Assertions.assertFalse(EntityUtils.isAppendOnlyEntity(obj));
    Assertions.assertTrue(EntityUtils.isAppendOnlyEntity(AppendOnlyObject.class));
    Assertions.assertFalse(EntityUtils.isAppendOnlyEntity(CommonObject.class));
    Assertions.assertFalse(EntityUtils.isAppendOnlyEntity(null));
    // isCommonEntity
    CommonObject commonObject =
        new CommonObject(
            RandomUtil.randomString(6),
            OffsetDateTime.now(),
            RandomUtil.randomString(6),
            OffsetDateTime.now(),
            RandomUtil.randomString(8));
    Assertions.assertTrue(EntityUtils.isCommonEntity(commonObject));
    Assertions.assertFalse(EntityUtils.isCommonEntity(obj));
    Assertions.assertTrue(EntityUtils.isCommonEntity(CommonObject.class));
    Assertions.assertFalse(EntityUtils.isCommonEntity(AppendOnlyObject.class));
    Assertions.assertFalse(EntityUtils.isCommonEntity(null));
    // isReadonly
    ReadOnlyObject readOnlyObject = new ReadOnlyObject(true);
    Assertions.assertTrue(EntityUtils.isReadonly(readOnlyObject));
    readOnlyObject = new ReadOnlyObject(false);
    Assertions.assertFalse(EntityUtils.isReadonly(readOnlyObject));
    Assertions.assertFalse(EntityUtils.isReadonly(obj));
    Assertions.assertFalse(EntityUtils.isReadonly(null));
    // isEditableEntity
    Assertions.assertTrue(EntityUtils.isEditableEntity(readOnlyObject));
    Assertions.assertFalse(EntityUtils.isEditableEntity(obj));
    Assertions.assertTrue(EntityUtils.isEditableEntity(ReadOnlyObject.class));
    Assertions.assertFalse(EntityUtils.isEditableEntity(TreeObject.class));
    Assertions.assertFalse(EntityUtils.isEditableEntity(null));
    // isTreeEntity
    TreeObject treeObject =
        new TreeObject(
            RandomUtil.randomString(4), RandomUtil.randomString(6), RandomUtil.randomInt());
    Assertions.assertTrue(EntityUtils.isTreeEntity(treeObject));
    Assertions.assertFalse(EntityUtils.isTreeEntity(obj));
    Assertions.assertTrue(EntityUtils.isTreeEntity(TreeObject.class));
    Assertions.assertFalse(EntityUtils.isTreeEntity(ReadOnlyObject.class));
    Assertions.assertFalse(EntityUtils.isTreeEntity(null));
    // isUniqueEntity
    UniqueObject uniqueObject = new UniqueObject();
    Assertions.assertTrue(EntityUtils.isUniqueEntity(uniqueObject));
    Assertions.assertFalse(EntityUtils.isUniqueEntity(obj));
    Assertions.assertTrue(EntityUtils.isUniqueEntity(UniqueObject.class));
    Assertions.assertFalse(EntityUtils.isUniqueEntity(TreeObject.class));
    Assertions.assertFalse(EntityUtils.isUniqueEntity(null));
    // getEntityField
    Object val = EntityUtils.getEntityField(commonObject, "remark");
    Assertions.assertEquals(commonObject.getRemark(), val.toString());
    val = EntityUtils.getEntityField(commonObject, "updatedTime");
    Assertions.assertEquals(commonObject.getUpdatedTime(), val);
    val = EntityUtils.getEntityField(commonObject, "notExist");
    Assertions.assertNull(val);
    Assertions.assertThrows(
        BeanException.class,
        () -> EntityUtils.getEntityField(commonObject, "notExist", true, null));
    // setEntityIdentifier
    TreeObject obj1 = new TreeObject(RandomUtil.randomString(6), null, 1);
    String nid = RandomUtil.randomString(5);
    EntityUtils.setEntityIdentifier(obj1, nid);
    Assertions.assertEquals(nid, obj1.getId());
    // getEntityIdentifier
    Assertions.assertEquals(nid, EntityUtils.getEntityIdentifier(obj1));
    Assertions.assertEquals("", EntityUtils.getEntityIdentifier(null));
    Assertions.assertEquals(
        appendOnlyObject.toString(), EntityUtils.getEntityIdentifier(appendOnlyObject));
  }
}
