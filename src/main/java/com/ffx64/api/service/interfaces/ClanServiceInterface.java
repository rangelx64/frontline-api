package com.ffx64.api.service.interfaces;

import com.ffx64.api.dto.ClanRequestDTO;
import com.ffx64.api.dto.ClanResponseDTO;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClanServiceInterface {
    List<ClanResponseDTO> findAll();

    Optional<ClanResponseDTO> findById(UUID id);

    ClanResponseDTO save(ClanRequestDTO clanRequestDTO);

    Optional<ClanResponseDTO> findByTag(String tag);

    Optional<ClanResponseDTO> findByName(String name);

    Boolean deleteById(UUID id);
}
