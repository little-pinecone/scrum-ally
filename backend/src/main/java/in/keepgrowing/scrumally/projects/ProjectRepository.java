package in.keepgrowing.scrumally.projects;

import in.keepgrowing.scrumally.projects.model.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Optional;

public interface ProjectRepository extends PagingAndSortingRepository<Project, Long> {

    @Query(value = "SELECT p" +
            " FROM Project p" +
            " LEFT JOIN FETCH p.members m" +
            " LEFT JOIN m.user u" +
            " WHERE u.userCredentials.username = ?#{principal?.username}",
            countQuery = "SELECT COUNT(p)" +
            " FROM Project p" +
            " LEFT JOIN p.members m" +
            " LEFT JOIN m.user u" +
            " WHERE u.userCredentials.username = ?#{principal?.username}")
    Page<Project> findAllForCurrentUser(Pageable pageable);

    @Query("SELECT p" +
            " FROM Project p" +
            " LEFT JOIN FETCH p.members m" +
            " LEFT JOIN m.user u" +
            " WHERE u.userCredentials.username = ?#{principal?.username}" +
            " AND p.id = :projectId")
    Optional<Project> findOneForCurrentUser(@Param("projectId") Long projectId);

    @Modifying
    @Transactional
    @Query("DELETE" +
            " FROM Project p" +
            " WHERE p.id IN" +
            "   (SELECT pr.id FROM Project pr" +
            "   LEFT JOIN pr.members m" +
            "   LEFT JOIN m.user u" +
            "   WHERE u.userCredentials.username = ?#{principal?.username}" +
            "   AND pr.id = :projectId)")
    void deleteProjectOwnedByCurrentUser(@Param("projectId") Long projectId);
}
