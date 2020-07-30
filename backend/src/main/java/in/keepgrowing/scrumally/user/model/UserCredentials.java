package in.keepgrowing.scrumally.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCredentials {

    @NotBlank(message = "Provide a usernname")
    @Column(unique = true)
    private String username;

    @NotBlank(message = "Provide a password")
    private String password;

    private String role;

    public static UserCredentials fromUserName(String username) {
        UserCredentials credentials = new UserCredentials();
        credentials.username = username;
        return credentials;
    }
}
