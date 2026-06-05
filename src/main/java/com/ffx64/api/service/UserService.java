package com.ffx64.api.service;

import com.ffx64.api.dto.PlayerStatsResponseDTO;
import com.ffx64.api.dto.UserRequestDTO;
import com.ffx64.api.dto.UserResponseDTO;
import com.ffx64.api.entity.UserEntity;
import com.ffx64.api.exception.UserNotFoundException;
import com.ffx64.api.repository.UserRepository;
import com.ffx64.api.service.interfaces.UserServiceInterface;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService implements UserServiceInterface {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlayersService playersService;

    public UserResponseDTO get() {
        UserEntity user = userRepository.findActiveByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new UserNotFoundException("Você não está autenticado."));
        return toResponseDTO(user);
    }

    @Transactional
    public UserResponseDTO update(UserRequestDTO userRequestDTO) {
        UserEntity user = userRepository.findActiveByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new UserNotFoundException("Você não está autenticado."));

        user.setUsername(userRequestDTO.username());
        user.setBohemia_guid(userRequestDTO.bohemia_guid());
        user.setEmail(userRequestDTO.email());
        user.setAccept_terms(userRequestDTO.accept_terms());
        user.setPhoto(userRequestDTO.photo());
        user.setBanner_photo(userRequestDTO.banner_photo());
        user.setDescription(userRequestDTO.description());
        user.setUpdatedAt(LocalDateTime.now());

        UserEntity updated = userRepository.save(user);

        return toResponseDTO(updated);
    }

    @Transactional
    public void delete() {
        UserEntity user = userRepository.findActiveByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new UserNotFoundException("Você não está autenticado."));

        user.setDeletedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setActive(false);
        userRepository.save(user);
    }

    public PlayerStatsResponseDTO stats() {
        UserEntity user = userRepository.findActiveByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new UserNotFoundException("Você não está autenticado."));

        return playersService.getByGuid(user.getBohemia_guid());
    }

    private UserResponseDTO toResponseDTO(UserEntity user) {
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getBohemia_guid(),
                user.getEmail(),
                user.getAcceptTerms(),
                user.getPhoto(),
                user.getBanner_photo(),
                user.getDescription(),
                user.getIs_admin(),
                user.getDonator(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getDeletedAt());
    }
}
