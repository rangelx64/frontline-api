package com.ffx64.api.repository.interfaces;

import com.ffx64.api.entity.ClanEntity;
import java.util.Optional;

public interface ClanRepositoryInterface {
    Optional<ClanEntity> findByTag(String tag);

    Optional<ClanEntity> findByName(String name);
}
