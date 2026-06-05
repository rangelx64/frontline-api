package com.ffx64.api.repository;

import com.ffx64.api.entity.UserEntity;
import com.ffx64.api.repository.interfaces.UserRepositoryInterface;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository
        extends JpaRepository<UserEntity, UUID>, UserRepositoryInterface {
    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByUsername(String username);

    @Query("SELECT u FROM UserEntity u WHERE u.email = :email AND u.active = true AND u.deleted_at IS NULL")
    Optional<UserEntity> findActiveByEmail(String email);

    @Query("SELECT u FROM UserEntity u WHERE u.username = :username AND u.active = true AND u.deleted_at IS NULL")
    Optional<UserEntity> findActiveByUsername(String username);

    @Query("SELECT u FROM UserEntity u WHERE LOWER(u.username) LIKE LOWER(CONCAT(:name, '%')) AND u.active = true AND u.deleted_at IS NULL")
    List<UserEntity> findActiveByNameStartingWith(String name);

    @Query("SELECT u FROM UserEntity u WHERE u.bohemia_guid = :bohemia_guid")
    Optional<UserEntity> findByBohemia_guid(String bohemia_guid);

    @Query("SELECT u FROM UserEntity u WHERE u.bohemia_guid = :bohemia_guid AND u.active = true AND u.deleted_at IS NULL")
    Optional<UserEntity> findActiveByBohemiaGuid(String bohemia_guid);

    default UserDetails findByEmailDetails(String email) {
        Optional<UserEntity> userEntity = findActiveByEmail(email);
        return userEntity
                .map(u -> new org.springframework.security.core.userdetails.User(
                        u.getEmail(),
                        u.getPassword(),
                        u.getAuthorities()))
                .orElse(null);
    }
}
