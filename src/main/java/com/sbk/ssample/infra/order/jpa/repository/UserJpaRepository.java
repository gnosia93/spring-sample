package com.sbk.ssample.infra.order.jpa.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sbk.ssample.infra.order.jpa.entity.UserEntity;

public interface UserJpaRepository extends MongoRepository<UserEntity, String> {

}
