package in.keepgrowing.scrumally.tasks.viewmodel;

import in.keepgrowing.scrumally.tasks.Task;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class TaskEntityDtoConverter {

    private final ModelMapper modelMapper;

    public TaskEntityDtoConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Task toEntity(TaskDto taskDto) {
        return modelMapper.map(taskDto, Task.class);
    }

    public TaskDto toDto(Task task) {
        return modelMapper.map(task, TaskDto.class);
    }
}
