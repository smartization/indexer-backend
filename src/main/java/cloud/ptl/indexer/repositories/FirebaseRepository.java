package cloud.ptl.indexer.repositories;

import cloud.ptl.indexer.model.FirebaseToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface FirebaseRepository extends CrudRepository<FirebaseToken, Long> {
    Optional<FirebaseToken> findByToken(String token);
}
