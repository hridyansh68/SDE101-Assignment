package com.sde101.notificationservice.services;


import com.sde101.notificationservice.engines.Receiver;
import com.sde101.notificationservice.engines.Sender;

import com.sde101.notificationservice.models.entity.SmsRequest;
import com.sde101.notificationservice.repositories.SmsRequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class SmsRequestService {

    private final Logger logger = LoggerFactory.getLogger(Receiver.class);

    private SmsRequestRepository smsRequestRepository;

    @Autowired
    SmsRequestService(SmsRequestRepository smsRequestRepository){
             this.smsRequestRepository = smsRequestRepository;
    }

    private Sender sender;


    @Autowired
    public void KafkaController(Sender sender) {
        this.sender = sender;
    }

    public SmsRequest createSmsRequest(String phone_number,String message) {
        logger.info(phone_number);
        SmsRequest newSmsRequest = new SmsRequest(phone_number,message);
        newSmsRequest = smsRequestRepository.save(newSmsRequest);
        String request_id = newSmsRequest.getId().toString();
        this.sender.sendMessage(request_id);
        return newSmsRequest;
    }

    public SmsRequest getSmsRequestById(Integer id) throws EntityNotFoundException {
        Optional<SmsRequest> smsRequest = smsRequestRepository.findById(id);
        if(smsRequest.isPresent()) {
            return smsRequest.get();
        }
        else {
            throw new EntityNotFoundException();
        }
    }

}
