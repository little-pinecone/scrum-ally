package in.keepgrowing.scrumally.user.model;

import in.keepgrowing.scrumally.projects.model.ProjectMember;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="user_data")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private UserCredentials userCredentials;

    @OneToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE},
            fetch = FetchType.LAZY,
            mappedBy = "user")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<ProjectMember> projects;

    protected User() {
    }

    public User(UserCredentials userCredentials) {
        this.userCredentials = userCredentials;
    }

    public static User fromUserName(String userName) {
        UserCredentials credentials = UserCredentials.fromUserName(userName);
        return new User(credentials);
    }

    public void updateFrom(User userDetails) {
        userCredentials = userDetails.userCredentials;
    }

    public Long getId() {
        return id;
    }

    public UserCredentials getUserCredentials() {
        return userCredentials;
    }

    public Set<ProjectMember> getProjects() {
        return projects;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(userCredentials, user.userCredentials) &&
                Objects.equals(projects, user.projects);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userCredentials, projects);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userCredentials=" + userCredentials +
                ", projects=" + projects +
                '}';
    }
}
