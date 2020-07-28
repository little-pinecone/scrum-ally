package in.keepgrowing.scrumally.projects.viewmodel;

import in.keepgrowing.scrumally.projects.model.ProjectRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectMemberDto {

    private Long id;
    private Long userId;
    private ProjectRole projectRole;
}
