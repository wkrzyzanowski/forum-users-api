package pl.wiktor.forumapiusers.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wiktor.forumapiusers.management.model.entity.RoleEntity;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    Optional<RoleEntity> getByName(String name);

}
