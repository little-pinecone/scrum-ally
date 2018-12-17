package in.keepgrowing.scrumally.projects.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
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

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setMembers(Set<ProjectMember> members) {
        this.members = members;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return Objects.equals(id, project.id) &&
                Objects.equals(name, project.name) &&
                Objects.equals(description, project.description) &&
                Objects.equals(members, project.members);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, members);
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", members=" + members +
                '}';
    }
}
