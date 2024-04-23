package ra.scurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ra.scurity.constants.RoleName;
import ra.scurity.model.entity.Roles;

import java.util.Optional;


public interface IRoleRepository extends JpaRepository<Roles,Long> {
    Optional<Roles> findByRoleName(RoleName roleName);
}
