package in.keepgrowing.scrumally.projects.model;

import in.keepgrowing.scrumally.user.model.User;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class ProjectMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Project project;

    @ManyToOne
    private User user;

    @Enumerated(EnumType.STRING)
    private ProjectRole projectRole;

    protected ProjectMember() {
    }

    public ProjectMember(User user, Project project, ProjectRole projectRole) {
        this.user = user;
        this.project = project;
        this.projectRole = projectRole;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Project getProject() {
        return project;
    }

    public ProjectRole getProjectRole() {
        return projectRole;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectMember member = (ProjectMember) o;
        return Objects.equals(id, member.id) &&
                projectRole == member.projectRole;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, projectRole);
    }

    @Override
    public String toString() {
        return "ProjectMember{" +
                "id=" + id +
                ", projectRole=" + projectRole +
                '}';
    }
}
