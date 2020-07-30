package in.keepgrowing.scrumally.user.viewmodel;

import in.keepgrowing.scrumally.projects.viewmodel.ProjectMemberDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long id;

    private UserCredentialsDto userCredentials;

    private Set<ProjectMemberDto> projects;
}
