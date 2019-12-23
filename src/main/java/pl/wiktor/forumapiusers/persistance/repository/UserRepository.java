package pl.wiktor.forumapiusers.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.wiktor.forumapiusers.persistance.model.UserEntity;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {


    Optional<UserEntity> getByUuid(String uuid);

    Optional<UserEntity> getByEmail(String email);


}
