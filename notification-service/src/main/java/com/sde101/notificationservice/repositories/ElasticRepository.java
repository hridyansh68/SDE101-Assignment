package com.sde101.notificationservice.repositories;

import com.sde101.notificationservice.models.requests.DateRequestBody;
import com.sde101.notificationservice.models.responses.ElasticResponse;
import com.sde101.notificationservice.models.entity.SmsRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.*;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

@Repository
public class ElasticRepository {
    private final Logger logger = LoggerFactory.getLogger(ElasticRepository.class);
    private final String INDEX = "smsrequestdata";
    private final String TYPE = "smsrequest";
    private RestHighLevelClient restHighLevelClient;


    public ElasticRepository(RestHighLevelClient restHighLevelClient) {
        this.restHighLevelClient = restHighLevelClient;
    }

    public SmsRequest insertSmsRequest(SmsRequest smsRequest) throws IOException {
        IndexRequest indexRequest = new IndexRequest(INDEX, TYPE, smsRequest.getId().toString()).source(
                jsonBuilder()
                        .startObject()
                        .field("id", smsRequest.getId())
                        .field("phoneNumber", smsRequest.getPhoneNumber())
                        .field("message", smsRequest.getMessage())
                        .field("status", smsRequest.getStatus())
                        .field("failureCode", smsRequest.getFailureCode())
                        .field("failureComments", smsRequest.getFailureCode())
                        .field("createdAt", smsRequest.getCreatedAt())
                        .field("updatedAt", smsRequest.getUpdatedAt())
                        .endObject()
        );
        IndexResponse response = restHighLevelClient.index(indexRequest);
        return smsRequest;
    }

    public ElasticResponse searchByText(String text, String scrollID) throws IOException, EntityNotFoundException {
        final Scroll scroll = new Scroll(TimeValue.timeValueMinutes(1L));
        SearchRequest searchRequest = new SearchRequest("smsrequestdata");
        searchRequest.scroll(scroll);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.queryStringQuery(text).lenient(true).field("message"));
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(10);
        SearchResponse response;
        String scrollId;
        if (scrollID == "") {
            searchRequest.source(searchSourceBuilder);
            response = restHighLevelClient.search(searchRequest);
            scrollId = response.getScrollId();
        } else {
            SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollID);
            scrollRequest.scroll(scroll);
            response = restHighLevelClient.searchScroll(scrollRequest);
            scrollId = response.getScrollId();

        }

        List<SearchHits> searchHits = Arrays.asList(response.getHits());
        SearchHit[] matchHits = searchHits.get(0).getHits();
        if (matchHits.length > 0) {
            int i;
            ArrayList<Map<String, Object>> responseList = new ArrayList<>();
            for (i = 0; i < matchHits.length; i++) {
                responseList.add(matchHits[i].getSourceAsMap());
            }

            return new ElasticResponse(responseList, scrollId);
        } else {
            throw new EntityNotFoundException("No more record matching your request");
        }

    }

    public ArrayList<Map<String, Object>> searchByPhoneNumber(DateRequestBody dateRequestBody) throws IOException {
        LocalDateTime myFDateObj = LocalDateTime.of(dateRequestBody.getFyear(), dateRequestBody.getFmonth(), dateRequestBody.getFday()
                , 0, 0, 0);
        LocalDateTime myTDateObj = LocalDateTime.of(dateRequestBody.getTyear(), dateRequestBody.getTmonth(), dateRequestBody.getTday()
                , 0, 0, 0);

        SearchRequest searchRequest = new SearchRequest();

        BoolQueryBuilder first = QueryBuilders.boolQuery()
                .must(termQuery("phoneNumber", dateRequestBody.getPhoneNumber()))
                .must(rangeQuery("createdAt").gt(myFDateObj).lt(myTDateObj));

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(first);
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(5);

        searchRequest.source(searchSourceBuilder);
        SearchResponse response = restHighLevelClient.search(searchRequest);
        logger.info(response.toString());
        List<SearchHits> searchHits = Arrays.asList(response.getHits());
        SearchHit[] matchHits = searchHits.get(0).getHits();
        int i;
        ArrayList<Map<String, Object>> responseList = new ArrayList<>();
        for (i = 0; i < matchHits.length; i++) {
            responseList.add(matchHits[i].getSourceAsMap());
        }
        return responseList;
    }

}
