package in.keepgrowing.scrumally.projects.viewmodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectWithMembersDto {
    private Long id;

    @NotBlank(message = "Provide a project name")
    private String name;

    private String description;

    private Set<ProjectMemberDto> members;
}
