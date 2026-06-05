package com.ffx64.api.service.interfaces;

import com.ffx64.api.dto.ClanMemberRequestDTO;
import com.ffx64.api.dto.ClanMemberResponseDTO;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClanMemberServiceInterface {
    ClanMemberResponseDTO save(ClanMemberRequestDTO clanMemberRequestDTO);

    Page<ClanMemberResponseDTO> findAll(Pageable pageable);

    Optional<ClanMemberResponseDTO> findById(UUID id);

    List<ClanMemberResponseDTO> findByClanId(UUID clanId);

    List<ClanMemberResponseDTO> findByUserId(UUID userId);
}
