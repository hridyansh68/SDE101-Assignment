package com.sde101.notificationservice.repositories;

import com.sde101.notificationservice.models.entity.BlacklistedNumbers;
import org.springframework.data.repository.CrudRepository;

public interface BlackListedNumbersRepository extends CrudRepository<BlacklistedNumbers,Integer> {
}
