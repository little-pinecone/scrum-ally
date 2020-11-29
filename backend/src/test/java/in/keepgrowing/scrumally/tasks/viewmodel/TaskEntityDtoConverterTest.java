package in.keepgrowing.scrumally.tasks.viewmodel;

import in.keepgrowing.scrumally.tasks.Task;
import in.keepgrowing.scrumally.tasks.TaskPriority;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class TaskEntityDtoConverterTest {

    private static final ModelMapper modelMapper = new ModelMapper();

    private TaskEntityDtoConverter entityDtoConverter;

    @BeforeEach
    void setUp() {
        entityDtoConverter = new TaskEntityDtoConverter(modelMapper);
    }

    @Test
    void shouldConvertEntityToDto() {
        var entity = getTask();
        var dto = entityDtoConverter.toDto(entity);

        assertAll(
                () -> assertEquals(entity.getName(), dto.getName()),
                () -> assertEquals(entity.getDescription(), dto.getDescription()),
                () -> assertEquals(entity.getProject().getId(), dto.getProjectId()),
                () -> assertEquals(entity.getId(), dto.getId())
        );
    }

    private Task getTask() {
        var task = new Task("task", "description", TaskPriority.CRITICAL);
        task.setProjectFromId(1L);
        return task;
    }

    @Test
    void shouldConvertToEntity() {
        var dto = getTaskDto();
        var entity = entityDtoConverter.toEntity(dto);

        assertAll(
                () -> assertEquals(dto.getName(), entity.getName()),
                () -> assertEquals(dto.getDescription(), entity.getDescription()),
                () -> assertEquals(dto.getProjectId(), entity.getProject().getId()),
                () -> assertEquals(dto.getId(), entity.getId())
        );
    }

    private TaskDto getTaskDto() {
        return new TaskDto(1L, "task", "description", 2L);
    }
}