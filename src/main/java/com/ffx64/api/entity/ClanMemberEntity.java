package com.ffx64.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "clans_members", indexes = {
        @Index(name = "idx_clans_members_clan_id", columnList = "clan_id"),
        @Index(name = "idx_clans_members_user_id", columnList = "user_id"),
        @Index(name = "idx_clans_members_is_owner", columnList = "is_owner"),
}, uniqueConstraints = {
        @UniqueConstraint(columnNames = { "clan_id", "user_id" }),
})
public class ClanMemberEntity {

    @Id
    @GeneratedValue(generator = "uuid4")
    @Column(columnDefinition = "UUID DEFAULT uuid_generate_v4()")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "clan_id")
    private ClanEntity clan;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "is_owner", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean is_owner;

    @CreationTimestamp
    @Column(name = "joined_at", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime joined_at;

    public ClanMemberEntity() {
    }

    public ClanMemberEntity(
            ClanEntity clan,
            UserEntity user,
            Boolean is_owner,
            LocalDateTime joined_at) {
        this.clan = clan;
        this.user = user;
        this.is_owner = is_owner;
        this.joined_at = joined_at;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ClanEntity getClan() {
        return clan;
    }

    public void setClan(ClanEntity clan) {
        this.clan = clan;
    }

    public Boolean getIsOwner() {
        return is_owner;
    }

    public void setIsOwner(Boolean is_owner) {
        this.is_owner = is_owner;
    }

    public LocalDateTime getJoinedAt() {
        return joined_at;
    }

    public void setJoinedAt(LocalDateTime joined_at) {
        this.joined_at = joined_at;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
