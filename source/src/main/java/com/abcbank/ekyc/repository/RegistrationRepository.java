package com.abcbank.ekyc.repository;

import com.abcbank.ekyc.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, String> {

    boolean existsByCitizenId(String citizenId);

    boolean existsByPhone(String phone);

    boolean existsByEmail(String email);

    Optional<Registration> findByCitizenId(String citizenId);

    Optional<Registration> findByPhone(String phone);

    Optional<Registration> findByEmail(String email);
}
