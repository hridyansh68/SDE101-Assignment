package com.sde101.notificationservice.engines;

import com.sde101.notificationservice.models.entity.SmsRequest;
import com.sde101.notificationservice.repositories.ElasticRepository;
import com.sde101.notificationservice.repositories.RedisRepository;
import com.sde101.notificationservice.services.SmsRequestService;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;

import org.json.JSONObject;
import org.json.JSONArray;


public class Receiver {
    private final Logger logger = LoggerFactory.getLogger(Receiver.class);
    final String key = "7b73f76d-369e-11ea-9e4e-025282c394f2";

    RestTemplate restTemplate = new RestTemplate();
    final String baseUrl = "https://api.imiconnect.in/resources/v1/messaging";

    private CountDownLatch latch = new CountDownLatch(1);

    public CountDownLatch getLatch() {
        return latch;
    }

    @Autowired
    SmsRequestService smsRequestService;

    @Autowired
    ElasticRepository elasticRepository;

    @Autowired
    RedisRepository redisRepository;

    @KafkaListener(containerFactory = "kafkaListenerContainerFactory", topics = "send_sms")
    public void consume(String message) throws IOException, URISyntaxException, JSONException {
        logger.info(String.format("#### -> Consumed message -> %s", message));
        Integer requestId = Integer.parseInt(message);
        logger.info("message received by the consumer" + requestId.toString());
        SmsRequest smsRequest = smsRequestService.getSmsRequestById(requestId);
        logger.info("id of the retrieved smsrequest from sql db:" + smsRequest.getId());
        smsRequest = elasticRepository.insertSmsRequest(smsRequest);
        logger.info("elastic insert " + smsRequest.getId().toString());

        Boolean isBlacklisted = redisRepository.ifNumberBlackListed(smsRequest.getPhoneNumber()).getBlacklisted();
        if (isBlacklisted) {
            logger.info("Blacklisted number");
        } else {
            URI uri = new URI(baseUrl);
            HttpHeaders headers = new HttpHeaders();

            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("key", key);
            JSONObject requestObject = new JSONObject();

            JSONArray requestArray = new JSONArray();

            requestObject.put("deliverychannel", "sms");

            JSONObject channelsObject = new JSONObject();
            JSONObject smsObject = new JSONObject();
            smsObject.put("text", smsRequest.getMessage());
            channelsObject.put("sms", smsObject);
            requestObject.put("channels", channelsObject);

            JSONArray destinationArray = new JSONArray();
            JSONObject destinationObject = new JSONObject();
            destinationObject.put("correlationid", "some_unique_id");
            logger.info(smsRequest.getPhoneNumber());
            JSONArray numberArray = new JSONArray();
            numberArray.put(smsRequest.getPhoneNumber());
            destinationObject.put("msisdn", numberArray);
            destinationArray.put(destinationObject);

            requestObject.put("destination", destinationArray);

            requestArray.put(requestObject);


            HttpEntity<String> requestBody =
                    new HttpEntity<String>(requestArray.toString(), headers);

            logger.info("request: " + requestBody.toString());

//            String result = restTemplate.postForObject(uri, requestBody , String.class);
//
//            logger.info(result);


        }

        latch.countDown();


    }
}
