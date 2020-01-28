package com.sde101.notificationservice.repositories;

import com.sde101.notificationservice.models.entity.SmsRequest;
import org.springframework.data.repository.CrudRepository;

public interface SmsRequestRepository extends CrudRepository<SmsRequest, Integer> {

}
