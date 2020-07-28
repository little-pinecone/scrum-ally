package in.keepgrowing.scrumally.projects.viewmodel;

import in.keepgrowing.scrumally.projects.model.Project;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class ProjectEntityDtoConverter {

    private final ModelMapper modelMapper;

    public ProjectEntityDtoConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Project toEntity(ProjectDto projectDto) {
        return modelMapper.map(projectDto, Project.class);
    }

    public ProjectDto toDto(Project project) {
        return modelMapper.map(project, ProjectDto.class);
    }
}
