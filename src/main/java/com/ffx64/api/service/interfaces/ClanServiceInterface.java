package com.ffx64.api.service.interfaces;

import com.ffx64.api.dto.ClanRequestDTO;
import com.ffx64.api.dto.ClanResponseDTO;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClanServiceInterface {
    Page<ClanResponseDTO> findAll(Pageable pageable);

    Optional<ClanResponseDTO> findById(UUID id);

    ClanResponseDTO save(ClanRequestDTO clanRequestDTO);

    Optional<ClanResponseDTO> findByTag(String tag);

    Optional<ClanResponseDTO> findByName(String name);

    Boolean deleteById(UUID id);
}
