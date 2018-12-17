package in.keepgrowing.scrumally.security.WebSecurityExpression;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
public class AccessPolicy {

    public static User getCurrentUser() throws UserUnauthorisedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new UserUnauthorisedException();
        }
        return (User) authentication.getPrincipal();
    }
}