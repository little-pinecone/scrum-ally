package in.keepgrowing.scrumally.tasks.viewmodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {

    private Long id;

    @NotBlank(message = "Provide a task name")
    private String name;

    private String description;

    private Long projectId;
}
