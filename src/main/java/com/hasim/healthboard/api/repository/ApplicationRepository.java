package com.hasim.healthboard.api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.hasim.healthboard.api.entity.Application;

@Repository
public interface ApplicationRepository extends CrudRepository<Application, Long>{

    Application findByAppId(String appId);
}
