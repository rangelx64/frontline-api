package com.ffx64.api.repository.interfaces;

import com.ffx64.api.entity.UserEntity;
import java.util.List;
import java.util.Optional;

public interface UserRepositoryInterface {
    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findActiveByEmail(String email);

    Optional<UserEntity> findActiveByUsername(String username);

    List<UserEntity> findActiveByNameStartingWith(String name);

    Optional<UserEntity> findByBohemia_guid(String bohemia_guid);

    Optional<UserEntity> findActiveByBohemiaGuid(String bohemia_guid);
}
