package com.fombico.sameauthandresourceserver.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountRecord, Long> {
    Optional<AccountRecord> findByUsername(String username);
}
