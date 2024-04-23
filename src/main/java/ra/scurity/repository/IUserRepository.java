package ra.scurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ra.scurity.model.entity.Users;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<Users,Long> {
    Optional<Users> findByEmail(String email);
    boolean existsByEmail(String email);
}
