package com.test;

import com.imooc.Application;
import com.imooc.es.pojo.Stu;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;

// @RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ESTest {
  @Autowired private ElasticsearchRestTemplate esTemplate;
  @Autowired private ElasticsearchOperations elasticsearchOperations;
  /**
   * 不建议使用 ElasticsearchTemplate 对索引进行管理（创建索引，更新映射，删除索引）
   * 索引就像是数据库或者数据库中的表，我们平时是不会是通过java代码频繁的去创建修改删除数据库或者表的 我们只会针对数据做CRUD的操作 在es中也是同理，我们尽量使用
   * ElasticsearchTemplate 对文档数据做CRUD的操作 1. 属性（FieldType）类型不灵活 2. 主分片与副本分片数无法设置
   */
  @Test
  public void createIndexStu() {
    Stu stu = new Stu();
    stu.setStuId(1005L);
    stu.setName("iron man");
    stu.setAge(54);
    stu.setMoney(1999.8f);
    stu.setSign("I am iron man");
    stu.setDescription("I have a iron army");
    IndexCoordinates indexCoordinates =
        elasticsearchOperations.getIndexCoordinatesFor(stu.getClass());
    IndexQuery indexQuery = new IndexQueryBuilder().withObject(stu).build();
    esTemplate.index(indexQuery, indexCoordinates);
  }

  @Test
  void function() {
    System.out.println("test function");
  }

  @Test
  public void deleteIndexStu() {
    esTemplate.delete(Stu.class);
  }

  //    ------------------------- 我是分割线 --------------------------------
  //  @Test
  //  public void updateStuDoc() {
  //
  //    Map<String, Object> sourceMap = new HashMap<>();
  //    //        sourceMap.put("sign", "I am not super man");
  //    sourceMap.put("money", 99.8f);
  //    //        sourceMap.put("age", 33);
  //
  //    IndexRequest indexRequest = new IndexRequest();
  //    indexRequest.source(sourceMap);
  //
  //    UpdateQuery updateQuery =
  //        new UpdateQueryBuilder()
  //            .withClass(Stu.class)
  //            .withId("1004")
  //            .withIndexRequest(indexRequest)
  //            .build();
  //
  //    //        update stu set sign='abc',age=33,money=88.6 where docId='1002'
  //
  //    esTemplate.update(updateQuery);
  //  }
  //
  //  @Test
  //  public void getStuDoc() {
  //
  //    GetQuery query = new GetQuery();
  //    query.setId("1002");
  //    Stu stu = esTemplate.queryForObject(query, Stu.class);
  //
  //    System.out.println(stu);
  //  }
  //
  //  @Test
  //  public void deleteStuDoc() {
  //    esTemplate.delete(Stu.class, "1002");
  //  }
  //
  //  //    ------------------------- 我是分割线 --------------------------------
  //
  //  @Test
  //  public void searchStuDoc() {
  //
  //    Pageable pageable = PageRequest.of(0, 2);
  //
  //    SearchQuery query =
  //        new NativeSearchQueryBuilder()
  //            .withQuery(QueryBuilders.matchQuery("description", "save man"))
  //            .withPageable(pageable)
  //            .build();
  //    AggregatedPage<Stu> pagedStu = esTemplate.queryForPage(query, Stu.class);
  //    System.out.println("检索后的总分页数目为：" + pagedStu.getTotalPages());
  //    List<Stu> stuList = pagedStu.getContent();
  //    for (Stu s : stuList) {
  //      System.out.println(s);
  //    }
  //  }
  //
  //  @Test
  //  public void highlightStuDoc() {
  //
  //    String preTag = "<font color='red'>";
  //    String postTag = "</font>";
  //
  //    Pageable pageable = PageRequest.of(0, 10);
  //
  //    SortBuilder sortBuilder = new FieldSortBuilder("money").order(SortOrder.DESC);
  //    SortBuilder sortBuilderAge = new FieldSortBuilder("age").order(SortOrder.ASC);
  //
  //    SearchQuery query =
  //        new NativeSearchQueryBuilder()
  //            .withQuery(QueryBuilders.matchQuery("description", "save man"))
  //            .withHighlightFields(
  //                new HighlightBuilder.Field("description").preTags(preTag).postTags(postTag))
  //            .withSort(sortBuilder)
  //            .withSort(sortBuilderAge)
  //            .withPageable(pageable)
  //            .build();
  //    AggregatedPage<Stu> pagedStu =
  //        esTemplate.queryForPage(
  //            query,
  //            Stu.class,
  //            new SearchResultMapper() {
  //              @Override
  //              public <T> AggregatedPage<T> mapResults(
  //                  SearchResponse response, Class<T> clazz, Pageable pageable) {
  //
  //                List<Stu> stuListHighlight = new ArrayList<>();
  //
  //                SearchHits hits = response.getHits();
  //                for (SearchHit h : hits) {
  //                  HighlightField highlightField = h.getHighlightFields().get("description");
  //                  String description = highlightField.getFragments()[0].toString();
  //
  //                  Object stuId = (Object) h.getSourceAsMap().get("stuId");
  //                  String name = (String) h.getSourceAsMap().get("name");
  //                  Integer age = (Integer) h.getSourceAsMap().get("age");
  //                  String sign = (String) h.getSourceAsMap().get("sign");
  //                  Object money = (Object) h.getSourceAsMap().get("money");
  //
  //                  Stu stuHL = new Stu();
  //                  stuHL.setDescription(description);
  //                  stuHL.setStuId(Long.valueOf(stuId.toString()));
  //                  stuHL.setName(name);
  //                  stuHL.setAge(age);
  //                  stuHL.setSign(sign);
  //                  stuHL.setMoney(Float.valueOf(money.toString()));
  //
  //                  stuListHighlight.add(stuHL);
  //                }
  //
  //                if (stuListHighlight.size() > 0) {
  //                  return new AggregatedPageImpl<>((List<T>) stuListHighlight);
  //                }
  //
  //                return null;
  //              }
  //            });
  //    System.out.println("检索后的总分页数目为：" + pagedStu.getTotalPages());
  //    List<Stu> stuList = pagedStu.getContent();
  //    for (Stu s : stuList) {
  //      System.out.println(s);
  //    }
  //  }
}
