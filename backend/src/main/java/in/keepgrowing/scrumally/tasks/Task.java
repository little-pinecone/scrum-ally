package in.keepgrowing.scrumally.tasks;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import in.keepgrowing.scrumally.projects.model.Project;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Data
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Provide a task name")
    private String name;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private Project project;

    protected Task() {
    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void updateFrom(Task taskDetails) {
        name = taskDetails.getName();
        description = taskDetails.getDescription();
        project = taskDetails.getProject();
    }

    public void setProjectFromId(Long projectId) {
        project = Project.fromId(projectId);
    }
}
