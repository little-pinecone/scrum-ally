package in.keepgrowing.scrumally.user.model;

import in.keepgrowing.scrumally.projects.model.ProjectMember;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "user_data")
@Data
@AllArgsConstructor
@NoArgsConstructor
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
}
