package com.sde101.notificationservice.services;

import com.sde101.notificationservice.models.entity.BlacklistedNumbers;
import com.sde101.notificationservice.models.responses.GetBlacklistedNumbersResponse;
import com.sde101.notificationservice.repositories.BlackListedNumbersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class BlacklistedNumbersService {

    @Autowired
    BlackListedNumbersRepository blackListedNumbersRepository;



    private static final Logger logger = LoggerFactory.getLogger(BlacklistedNumbersService.class);



    public GetBlacklistedNumbersResponse getAllBlacklistedNumbers(){
       Iterable<BlacklistedNumbers> numbers =  blackListedNumbersRepository.findAll();
        ArrayList<String> mylist = new ArrayList<String>();

        for (BlacklistedNumbers number : numbers) {
            mylist.add(number.getPhoneNumber());
        }
        String[] phoneNumbers = new String[mylist.size()];
        for(int i=0;i<mylist.size();i++){
            phoneNumbers[i] = mylist.get(i);
        }
        GetBlacklistedNumbersResponse getBlacklistedNumbersResponse = new GetBlacklistedNumbersResponse(phoneNumbers);
        return getBlacklistedNumbersResponse;
    }

    public void insertInBlacklisted(String[] numbers){
        ArrayList<BlacklistedNumbers> mylist = new ArrayList<BlacklistedNumbers>();
        for (String number:numbers) {
            mylist.add(new BlacklistedNumbers(number));
        }
        blackListedNumbersRepository.saveAll(mylist);
    }



}
