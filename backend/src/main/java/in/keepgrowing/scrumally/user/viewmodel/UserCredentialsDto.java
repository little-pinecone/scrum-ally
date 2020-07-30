package in.keepgrowing.scrumally.user.viewmodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCredentialsDto {

    private String username;

    @NotBlank(message = "Provide a password")
    private String password;

    private String role;
}
