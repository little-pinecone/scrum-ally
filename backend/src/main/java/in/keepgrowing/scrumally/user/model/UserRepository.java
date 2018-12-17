package in.keepgrowing.scrumally.user.model;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    Optional<User> findByUserCredentialsUsername(String username);
}
