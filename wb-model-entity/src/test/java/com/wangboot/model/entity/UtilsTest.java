package com.wangboot.model.entity;

import cn.hutool.core.bean.BeanException;
import cn.hutool.core.util.RandomUtil;
import com.wangboot.core.web.response.DetailBody;
import com.wangboot.core.web.response.ErrorBody;
import com.wangboot.core.web.response.ListBody;
import com.wangboot.core.web.response.MessageBody;
import com.wangboot.model.entity.event.EnableOperationLog;
import com.wangboot.model.entity.request.*;
import com.wangboot.model.entity.utils.EntityUtils;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.wangboot.model.entity.utils.RequestUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

@DisplayName("Entity测试")
public class UtilsTest {
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

  @EnableOperationLog
  static class Obj1 {
    private String name;
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
    // getEntityIdentifier
    String nid = RandomUtil.randomString(5);
    TreeObject obj1 = new TreeObject(nid, null, 1);
    Assertions.assertEquals(nid, EntityUtils.getEntityIdentifier(obj1));
    Assertions.assertEquals("", EntityUtils.getEntityIdentifier(null));
    Assertions.assertEquals(
        appendOnlyObject.toString(), EntityUtils.getEntityIdentifier(appendOnlyObject));
    // isOperationLogEnabled
    Assertions.assertTrue(EntityUtils.isOperationLogEnabled(Obj1.class));
    Assertions.assertFalse(EntityUtils.isOperationLogEnabled(ObjBody.class));
    // getOperationLogAnnotation
    Assertions.assertNotNull(EntityUtils.getOperationLogAnnotation(Obj1.class));
    Assertions.assertThrows(IllegalArgumentException.class, () -> EntityUtils.getOperationLogAnnotation(ObjBody.class));
  }

  @Test
  public void testBuildSortFilter() {
    // no input
    Assertions.assertNull(RequestUtils.buildSortFilter("", null, null));
    // only sort
    String field1 = "field1";
    String field2 = "field2";
    String sort = field1;
    SortFilter[] sortFilters1 = RequestUtils.buildSortFilter(sort, null, null);
    Assertions.assertNotNull(sortFilters1);
    Assertions.assertEquals(1, sortFilters1.length);
    Assertions.assertEquals(field1, sortFilters1[0].getField());
    Assertions.assertTrue(sortFilters1[0].isAsc());
    sort = "field1-";
    SortFilter[] sortFilters2 = RequestUtils.buildSortFilter(sort, null, null);
    Assertions.assertNotNull(sortFilters2);
    Assertions.assertEquals(1, sortFilters2.length);
    Assertions.assertEquals(field1, sortFilters2[0].getField());
    Assertions.assertFalse(sortFilters2[0].isAsc());
    sort = "field1,field2-";
    SortFilter[] sortFilters3 = RequestUtils.buildSortFilter(sort, null, null);
    Assertions.assertNotNull(sortFilters3);
    Assertions.assertEquals(2, sortFilters3.length);
    Assertions.assertEquals(field1, sortFilters3[0].getField());
    Assertions.assertTrue(sortFilters3[0].isAsc());
    Assertions.assertEquals(field2, sortFilters3[1].getField());
    Assertions.assertFalse(sortFilters3[1].isAsc());
    // with default
    SortFilter[] defaultSorts = new SortFilter[]{new SortFilter(field1)};
    sort = field2;
    SortFilter[] sortFilters4 = RequestUtils.buildSortFilter(sort, defaultSorts, null);
    Assertions.assertNotNull(sortFilters4);
    Assertions.assertEquals(1, sortFilters4.length);
    Assertions.assertEquals(field2, sortFilters4[0].getField());
    Assertions.assertTrue(sortFilters4[0].isAsc());
    SortFilter[] sortFilters5 = RequestUtils.buildSortFilter("", defaultSorts, null);
    Assertions.assertNotNull(sortFilters5);
    Assertions.assertEquals(1, sortFilters5.length);
    Assertions.assertEquals(field1, sortFilters5[0].getField());
    Assertions.assertTrue(sortFilters5[0].isAsc());
    // with sortable fields
    sort = "field2-";
    String[] sortableFields = new String[]{field1};
    SortFilter[] sortFilters6 = RequestUtils.buildSortFilter(sort, defaultSorts, sortableFields);
    Assertions.assertNotNull(sortFilters6);
    Assertions.assertEquals(0, sortFilters6.length);
    SortFilter[] sortFilters7 = RequestUtils.buildSortFilter(sort, null, sortableFields);
    Assertions.assertNotNull(sortFilters7);
    Assertions.assertEquals(0, sortFilters7.length);
    sort = "field1-,field2";
    SortFilter[] sortFilters8 = RequestUtils.buildSortFilter(sort, defaultSorts, sortableFields);
    Assertions.assertNotNull(sortFilters8);
    Assertions.assertEquals(1, sortFilters8.length);
    Assertions.assertEquals(field1, sortFilters8[0].getField());
    Assertions.assertFalse(sortFilters8[0].isAsc());
  }

  @Test
  public void testBuildParamFieldFilter() {
    Assertions.assertEquals(0, RequestUtils.buildParamFieldFilter(null, null).length);
    String field1 = "field1";
    String field2 = "field2";
    String field3 = "field3";
    // add filter not in definition
    ParamFilterDefinition definition1 = ParamFilterDefinition.newInstance().addFilter(field1);
    Map<String, String> params = new HashMap<>();
    params.put(field2, RandomUtil.randomString(6));
    FieldFilter[] filters1 = RequestUtils.buildParamFieldFilter(definition1, params);
    Assertions.assertEquals(0, filters1.length);
    // add filter in definition
    params.put(field1, RandomUtil.randomString(6));
    FieldFilter[] filters2 = RequestUtils.buildParamFieldFilter(definition1, params);
    Assertions.assertEquals(1, filters2.length);
    Assertions.assertEquals(field1, filters2[0].getField());
    Assertions.assertEquals(FilterOperator.EQ, filters2[0].getOperator());
    Assertions.assertEquals(ParamValType.STR, filters2[0].getType());
    Assertions.assertEquals(params.get(field1), filters2[0].getVal());
    definition1.addFilter(field3);
    params.put(field3, "");
    FieldFilter[] filters3 = RequestUtils.buildParamFieldFilter(definition1, params);
    Assertions.assertEquals(1, filters3.length);
    Assertions.assertEquals(field1, filters3[0].getField());
    // add gt definition
    ParamFilterDefinition definition2 = ParamFilterDefinition.newInstance()
      .addFilter(field1, new ParamFilterDefinition.Definition(field1, FilterOperator.GT, ParamValType.INT))
      .addFilter(field2)
      .addFilter("");
    params.clear();
    params.put(field1, "1");
    params.put(field2, "a");
    FieldFilter[] filters4 = RequestUtils.buildParamFieldFilter(definition2, params);
    Assertions.assertEquals(2, filters4.length);
    Assertions.assertEquals(field1, filters4[0].getField());
    Assertions.assertEquals(FilterOperator.GT, filters4[0].getOperator());
    Assertions.assertEquals(ParamValType.INT, filters4[0].getType());
    Assertions.assertEquals(params.get(field1), filters4[0].getVal());
  }

  @Test
  public void testBuildSearchFilter() {
    String query = RandomUtil.randomString(6);
    String field1 = "field1";
    String field2 = "field2";
    // search fields
    String[] searchFields1 = new String[]{field1, field2};
    FieldFilter[] filters1 = RequestUtils.buildSearchFilter(query, searchFields1, SearchStrategy.LEFT_LIKE);
    Assertions.assertEquals(2, filters1.length);
    Assertions.assertEquals(field1, filters1[0].getField());
    Assertions.assertEquals(FilterOperator.STARTSWITH, filters1[0].getOperator());
    Assertions.assertEquals(field2, filters1[1].getField());
    Assertions.assertEquals(FilterOperator.STARTSWITH, filters1[1].getOperator());
    // no searchable fields
    String[] searchFields2 = new String[]{};
    FieldFilter[] filters2 = RequestUtils.buildSearchFilter(query, searchFields2, SearchStrategy.LEFT_LIKE);
    Assertions.assertEquals(0, filters2.length);
    // other strategy
    String[] searchFields3 = new String[]{field1};
    FieldFilter[] filters3 = RequestUtils.buildSearchFilter(query, searchFields3, SearchStrategy.RIGHT_LIKE);
    Assertions.assertEquals(1, filters3.length);
    Assertions.assertEquals(field1, filters3[0].getField());
    Assertions.assertEquals(FilterOperator.ENDSWITH, filters3[0].getOperator());
    FieldFilter[] filters4 = RequestUtils.buildSearchFilter(query, searchFields3, SearchStrategy.BOTH_LIKE);
    Assertions.assertEquals(1, filters4.length);
    Assertions.assertEquals(field1, filters4[0].getField());
    Assertions.assertEquals(FilterOperator.CONTAINS, filters4[0].getOperator());
  }

  @Test
  public void testParameters() {
    // getParametersMap
    String field1 = "field1";
    String field2 = "field2";
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setParameter(field1, RandomUtil.randomString(6));
    request.setParameter(field2, RandomUtil.randomString(8));
    Map<String, String> param1 = RequestUtils.getParametersMap(request);
    Assertions.assertEquals(2, param1.size());
    Assertions.assertTrue(param1.containsKey(field1));
    Assertions.assertTrue(param1.containsKey(field2));
    // getSearchParam
    String query = RandomUtil.randomString(6);
    request.setParameter(RequestConstants.REQUEST_PARAM_SEARCH, query);
    String s1 = RequestUtils.getSearchParam(request);
    Assertions.assertEquals(query, s1);
    // getSortParam
    String sort = RandomUtil.randomString(8);
    request.setParameter(RequestConstants.REQUEST_PARAM_SORT, sort);
    String s2 = RequestUtils.getSortParam(request);
    Assertions.assertEquals(sort, s2);
    // getPageParam
    Assertions.assertEquals(RequestConstants.REQUEST_PARAM_PAGE_DEFAULT, RequestUtils.getPageParam(request));
    long page = RandomUtil.randomInt(1, 100);
    request.setParameter(RequestConstants.REQUEST_PARAM_PAGE, String.valueOf(page));
    long s3 = RequestUtils.getPageParam(request);
    Assertions.assertEquals(page, s3);
    // getPageSizeParam
    Assertions.assertEquals(RequestConstants.REQUEST_PARAM_PAGE_SIZE_DEFAULT, RequestUtils.getPageSizeParam(request, 100L));
    long pageSize = RandomUtil.randomInt(1, 1000);
    request.setParameter(RequestConstants.REQUEST_PARAM_PAGE_SIZE, String.valueOf(pageSize));
    long s4 = RequestUtils.getPageSizeParam(request, 10000L);
    Assertions.assertEquals(pageSize, s4);
    long s5 = RequestUtils.getPageSizeParam(request, 100L);
    Assertions.assertEquals(100L, s5);
  }
}
