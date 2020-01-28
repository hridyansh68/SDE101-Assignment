package com.sde101.notificationservice.controllers;

import com.sde101.notificationservice.models.entity.SmsRequest;
import com.sde101.notificationservice.models.requests.AddNumbertoBlacklistBody;
import com.sde101.notificationservice.models.requests.DateRequestBody;
import com.sde101.notificationservice.models.requests.ElasticRequest;
import com.sde101.notificationservice.models.requests.SmsRequestPostBody;
import com.sde101.notificationservice.models.responses.*;
import com.sde101.notificationservice.repositories.ElasticRepository;
import com.sde101.notificationservice.repositories.RedisRepository;
import com.sde101.notificationservice.services.BlacklistedNumbersService;
import com.sde101.notificationservice.services.SmsRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;


@RestController
@RequestMapping("/v1")
public class MainController {
    private Logger logger = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private SmsRequestService smsRequestService;

    @Autowired
    private RedisRepository redisRepository;

    @Autowired
    private ElasticRepository elasticRepository;

    @Autowired
    private BlacklistedNumbersService blacklistedNumbersService;



    @PostMapping(value = "/sms/send")
    public ResponseEntity<SmsRequestResponseBody> createSmsRequest(@RequestBody SmsRequestPostBody smsRequestPostBody){
        String phone_number = smsRequestPostBody.getPhone_number();
        String message = smsRequestPostBody.getMessage();
        SmsRequest updated = smsRequestService.createSmsRequest(phone_number,message);
        SmsRequestResponseBody response = new SmsRequestResponseBody(updated.getId());
        return new ResponseEntity<SmsRequestResponseBody>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/sms/{id}")
    @Cacheable(value="mycache",key="#id")
    public ResponseEntity<SmsRequestByIdResponse> getSmsRequestById(@PathVariable("id") Integer id)
            throws EntityNotFoundException {
        SmsRequestByIdResponse entity = new SmsRequestByIdResponse(smsRequestService.getSmsRequestById(id));
        return new ResponseEntity<SmsRequestByIdResponse>(entity, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping(value="/blacklist")
    public ResponseEntity<AddNumbertoBlacklistResponseBody> addNumbersToBlacklist(@RequestBody AddNumbertoBlacklistBody addNumbertoBlacklistBody){
        String[] numbers = addNumbertoBlacklistBody.getPhone_numbers();
        redisRepository.insertInBlackListed(numbers);
        AddNumbertoBlacklistResponseBody response = new AddNumbertoBlacklistResponseBody("Successfully Blacklisted");
        return new ResponseEntity<AddNumbertoBlacklistResponseBody>(response, new HttpHeaders(), HttpStatus.OK);
    }


    @GetMapping(value="/blacklist")
    public ResponseEntity<GetBlacklistedNumbersResponse> getAllBlacklistedNumbers(){
        Set<String> numbers = redisRepository.getAllBlackListedNumbers();
        String[] phone_numbers = numbers.toArray(new String[numbers.size()]);
        GetBlacklistedNumbersResponse response = new GetBlacklistedNumbersResponse(phone_numbers);
        return new ResponseEntity<GetBlacklistedNumbersResponse>(response,new HttpHeaders(),HttpStatus.OK);
    }

    @DeleteMapping(value="/blacklist/delete/{phone_number}")
    public String deleteNumberFromBlacklisted(@PathVariable("phone_number") String number){
        redisRepository.removeFromBlacklist(number);
        return "Succesfully deleted the number from Blacklist";
    }

    @GetMapping(value = "/sms/elastic/text")
    public ResponseEntity<ElasticResponse> searchByText(@RequestBody ElasticRequest elasticRequest) throws IOException, EntityNotFoundException {
         ElasticResponse response = elasticRepository.searchByText(elasticRequest.getText(),elasticRequest.getScrollId());
        return new ResponseEntity<ElasticResponse>(response,new HttpHeaders(),HttpStatus.OK);

    }

    @GetMapping(value="/sms/elastic/phonenumber")
    public ArrayList<Map<String, Object>> searchByPhoneNumber(@RequestBody DateRequestBody dateRequestBody) throws IOException, EntityNotFoundException {

        return elasticRepository.searchByPhoneNumber(dateRequestBody);
    }


//    @PostMapping(value="/blacklist")
//    public ResponseEntity<AddNumbertoBlacklistResponseBody> addNumbersToBlacklist(@RequestBody AddNumbertoBlacklistBody addNumbertoBlacklistBody){
//        String[] numbers = addNumbertoBlacklistBody.getPhone_numbers();
//        blacklistedNumbersService.insertInBlacklisted(numbers);
//        AddNumbertoBlacklistResponseBody response = new AddNumbertoBlacklistResponseBody("Successfully Blacklisted");
//        return new ResponseEntity<AddNumbertoBlacklistResponseBody>(response, new HttpHeaders(), HttpStatus.OK);
//    }
//
//    @GetMapping(value="/blacklist")
//    @Cacheable(value = "blacklistednumbers",key="#p0", condition="#p0!=null")
//    public ResponseEntity<GetBlacklistedNumbersResponse> getAllBlacklistedNumbers(){
//        GetBlacklistedNumbersResponse response = blacklistedNumbersService.getAllBlacklistedNumbers();
//        return new ResponseEntity<GetBlacklistedNumbersResponse>(response,new HttpHeaders(),HttpStatus.OK);
//    }

}
