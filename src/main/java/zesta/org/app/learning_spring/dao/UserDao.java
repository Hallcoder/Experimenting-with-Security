package zesta.org.app.learning_spring.dao;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDao {
    private final static List<UserDetails> APPLICATION_USERS = Arrays.asList(
            new User(
                    "hallcoder25@gmail.com",
                    "password",
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN"))
            ),
            new User(
                    "userone@gmail.com",
                    "password",
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
            )
    );

    public UserDetails findUserByEmail(String email){
        System.out.println("user here " + email);
        Optional<UserDetails> us = APPLICATION_USERS.stream().filter(u -> u.getUsername() == email).findFirst();
        System.out.println(us.toString());
        return APPLICATION_USERS
                .stream()
                .filter(u -> u.getUsername().strip().equals(email.strip()))
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("No user found!"));
    }
}
