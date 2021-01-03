package com.spring.SpringInAction.tacos.domain.user;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TacoUserRepository extends CrudRepository<TacoUser, Long> {
    Optional<TacoUser> findByUsername(String username);
}
