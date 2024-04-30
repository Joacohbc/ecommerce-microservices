package com.microecommerce.usersauthservice.repository;

import com.microecommerce.utilitymodule.interfaces.CustomJPARepository;
import com.microecommerce.utilitymodule.models.users.UserCredentials;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserCredentialsRepository extends CustomJPARepository<UserCredentials, Long> {
    Optional<UserCredentials> findByUsername(String username);
}
