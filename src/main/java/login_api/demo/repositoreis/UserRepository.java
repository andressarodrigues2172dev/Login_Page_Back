package login_api.demo.repositoreis;


import jakarta.persistence.Id;
import login_api.demo.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
}
