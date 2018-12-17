package in.keepgrowing.scrumally.tasks;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TaskRepository extends PagingAndSortingRepository<Task, Long> {

    Page<Task> findByProjectId(Long projectId, Pageable pageable);
}
