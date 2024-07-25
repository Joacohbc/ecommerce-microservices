package com.microecommerce.usersauthservice.repository;

import com.microecommerce.utilitymodule.interfaces.CustomJPARepository;
import com.microecommerce.utilitymodule.models.users.CUserCredentials;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserCredentialsRepository extends CustomJPARepository<CUserCredentials, Long> {
    Optional<CUserCredentials> findByUsernameIgnoreCase(String username);
}
