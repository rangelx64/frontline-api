package com.ffx64.api.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "access_logs")
public class AccessLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "ip_address", nullable = false)
    private String ip_address;

    @Column(name = "user_agent", columnDefinition = "TEXT")
    private String user_agent;

    @Column(length = 10)
    private String method;

    @Column(columnDefinition = "TEXT")
    private String endpoint;

    @Column(name = "status_code")
    private Integer status_code;

    @Column(name = "response_time_ms")
    private Integer response_time_ms;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime created_at;

    public AccessLogEntity() {
    }

    public AccessLogEntity(
            UserEntity user,
            String ip_address,
            String user_agent,
            String method,
            String endpoint,
            Integer status_code,
            Integer response_time_ms,
            LocalDateTime created_at) {
        this.user = user;
        this.ip_address = ip_address;
        this.user_agent = user_agent;
        this.method = method;
        this.endpoint = endpoint;
        this.status_code = status_code;
        this.response_time_ms = response_time_ms;
        this.created_at = created_at;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getIpAddress() {
        return ip_address;
    }

    public void setIpAddress(String ip_address) {
        this.ip_address = ip_address;
    }

    public String getUserAgent() {
        return user_agent;
    }

    public void setUserAgent(String user_agent) {
        this.user_agent = user_agent;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public Integer getStatusCode() {
        return status_code;
    }

    public void setStatusCode(Integer status_code) {
        this.status_code = status_code;
    }

    public Integer getResponseTimeMs() {
        return response_time_ms;
    }

    public void setResponseTimeMs(Integer response_time_ms) {
        this.response_time_ms = response_time_ms;
    }

    public LocalDateTime getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(LocalDateTime created_at) {
        this.created_at = created_at;
    }
}
