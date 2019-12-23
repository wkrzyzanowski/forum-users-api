package pl.wiktor.forumapiusers.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wiktor.forumapiusers.persistance.model.RoleEntity;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    Optional<RoleEntity> getByName(String name);

}
