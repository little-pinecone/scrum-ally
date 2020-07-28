package in.keepgrowing.scrumally.projects.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Provide a project name")
    private String name;

    private String description;

    @OneToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE},
            fetch = FetchType.LAZY,
            mappedBy = "project")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotEmpty
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private Set<ProjectMember> members;

    protected Project() {
        this.members = new HashSet<>();
    }

    public Project(String name, String description) {
        this();
        this.name = name;
        this.description = description;
    }

    public static Project fromId(Long projectId) {
        Project project = new Project();
        project.id = projectId;
        return project;
    }

    public void updateFrom(Project projectDetails) {
        name = projectDetails.name;
        description = projectDetails.description;
    }
}
