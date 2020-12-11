package com.hand.hand;

import com.hand.hand.controller.ContactPersonController;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import sun.font.Script;

import java.io.IOException;
import java.util.*;

import static org.elasticsearch.index.query.QueryBuilders.*;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

/***
 * *   ____  ___________  ___________           ________   ____  __.  _____ _____.___.
 * *   \   \/  /\______ \ \_   _____/           \_____  \ |    |/ _| /  _  \\__  |   |
 * *    \     /  |    |  \ |    __)     ______   /   |   \|      <  /  /_\  \/   |   |
 * *    /     \  |    `   \|     \     /_____/  /    |    \    |  \/    |    \____   |
 * *   /___/\  \/_______  /\___  /              \_______  /____|__ \____|__  / ______|
 * *    	 \_/        \/     \/                       \/        \/       \/\/
 * *
 * *   功能描述：
 * *
 * *   @DATE    2020-12-11
 * *   @AUTHOR  lijianghu
 ***/
@Component
public class EsUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(EsUtils.class);

    private static final int CONNECT_TIME_OUT = 30000;
    private static final int SOCKET_TIME_OUT = 300000;
    private static final int CONNECTION_REQUEST_TIME_OUT = 5000;
    private static final int MAX_CONNECT_NUM = 200;
    private static final int MAX_CONNECT_PER_ROUTE = 200;
    public static RestHighLevelClient client ;
    public static Map m = new HashMap();
    static {
        RestClientBuilder builder = RestClient.builder(new HttpHost("10.60.0.81", 9200, "http"));
        setConnectTimeOutConfig(builder);
        setMutiConnectConfig(builder);
        client = new RestHighLevelClient(builder);
        m.put(19,"小学语文");
        m.put(20,"小学数学");
        m.put(21,"小学英语");
        m.put(24,"小学家庭教育");
        m.put(29,"小学科学");
        m.put(31,"小学信息技术");
        m.put(32,"小学音乐");
        m.put(33,"小学美术");
        m.put(34,"小学思想品德");
        m.put(35,"小学okay学科测试");
        m.put(37,"小学易视腾测试学科");
        m.put(1,"初中语文");
        m.put(3,"初中数学");
        m.put(5,"初中英语");
        m.put(7,"初中物理");
        m.put(9,"初中化学");
        m.put(11,"初中生物");
        m.put(17,"初中政治");
        m.put(13,"初中历史");
        m.put(15,"初中地理");
        m.put(25,"初中科学");
        m.put(26,"初中社会");
        m.put(27,"初中信息技术");
        m.put(22,"初中家庭教育");
        m.put(2,"高中语文");
        m.put(4,"高中数学");
        m.put(6,"高中英语");
        m.put(8,"高中物理");
        m.put(10,"高中化学");
        m.put(12,"高中生物");
        m.put(18,"高中政治");
        m.put(14,"高中历史");
        m.put(16,"高中地理");
        m.put(28,"高中信息技术");
        m.put(23,"高中家庭教育");
        m.put(36,"高中日语");
    }

    public static void setConnectTimeOutConfig(RestClientBuilder builder) {
        builder.setRequestConfigCallback(requestConfigBuilder -> {
            requestConfigBuilder.setConnectTimeout(CONNECT_TIME_OUT);
            requestConfigBuilder.setSocketTimeout(SOCKET_TIME_OUT);
            requestConfigBuilder.setConnectionRequestTimeout(CONNECTION_REQUEST_TIME_OUT);
            return requestConfigBuilder;
        });
    }
    /**
     * 异步httpclient的连接数配置
     */
    public static void setMutiConnectConfig(RestClientBuilder builder) {
        builder.setHttpClientConfigCallback(httpClientBuilder -> {
            httpClientBuilder.setMaxConnTotal(MAX_CONNECT_NUM);
            httpClientBuilder.setMaxConnPerRoute(MAX_CONNECT_PER_ROUTE);
            return httpClientBuilder;
        });
    }
    public SearchResponse search(String indexName, QueryBuilder queryBuilder) throws Exception {
        SearchRequest search = new SearchRequest();
        search.indices(indexName);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(queryBuilder);
        search.source(sourceBuilder);
        SearchResponse searchResponse = client.search(search, RequestOptions.DEFAULT);
        return searchResponse;
    }

    public List searchKey(String subjectId,String key){
        BoolQueryBuilder questionRecomDsl = QueryBuilders.boolQuery();
        if(!StringUtils.isEmpty(subjectId) && !"undefined".equals(subjectId) && !"0".equals(subjectId)){
            questionRecomDsl.filter(termQuery("subjectId",subjectId));
        }
        questionRecomDsl.must(
                matchQuery("name.simple_pin_yin_ik",key)
                        .analyzer("pinyiSimple_ik")
                        .fuzziness("auto")
                        .operator(Operator.OR)
                        .minimumShouldMatch("60%"));
        questionRecomDsl.should().add(termQuery("level",3).boost(100));
        questionRecomDsl.should().add(termQuery("level",2).boost(50));
        questionRecomDsl.should().add(matchPhraseQuery("name.simple_pin_yin_ik",key).boost(100));
        //
        // SearchSourceBuilder
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(questionRecomDsl);
        sourceBuilder.trackTotalHits(false);
        sourceBuilder.sort(ScoreSortBuilder.NAME, SortOrder.DESC);
        //SearchRequest
        SearchRequest searchRequest = new SearchRequest("pin_yin_knowledge_2");
        searchRequest.source(sourceBuilder);
        //
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("name.simple_pin_yin_ik");
        highlightBuilder.preTags("<font color='#ef6e41'>");   //高亮设置
        highlightBuilder.postTags("</font>");
        sourceBuilder.highlighter(highlightBuilder);
        LOGGER.info(sourceBuilder.toString());
        //
        SearchResponse search = null;
        try {
            search = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits searchHits = search.getHits();
            //result
            List list = builderResult(searchHits);
            return list;

        } catch (IOException e) {
            e.printStackTrace();
        }
    return null;
    }
    private List builderResult(SearchHits searchHits){
            SearchHit[] hits = searchHits.getHits();
            List resultList = new ArrayList();
            for(SearchHit param:hits){
                Map<String, Object> sourceAsMap = param.getSourceAsMap();

                Map<String, HighlightField> highlightFields = param.getHighlightFields();
                HighlightField highlightField = highlightFields.get("name.simple_pin_yin_ik");
                String name = sourceAsMap.get("name").toString();
                if(highlightField.getFragments()!=null && highlightField.getFragments().length>0){
                    name = highlightField.getFragments()[0].toString();
                }
                Object subject = "";
                try {
                    subject = "-【"+m.get(Integer.parseInt(sourceAsMap.get("subjectId").toString()))+"】";
                }catch (Exception e){
                    subject = "";
                }
                Map<String,Object> mm= new HashMap<>();
                mm.put("NAME",name+subject);
                resultList.add(mm);
            }
            return resultList;
        }
    }
