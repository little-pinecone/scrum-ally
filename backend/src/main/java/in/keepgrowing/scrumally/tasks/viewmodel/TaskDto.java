package in.keepgrowing.scrumally.tasks.viewmodel;

import com.fasterxml.jackson.annotation.JsonProperty;
import in.keepgrowing.scrumally.tasks.TaskPriority;
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

    @JsonProperty("priority")
    private TaskPriority taskPriority;
}
