package com.junt.appchat;


import com.junt.appchat.model.User;
import com.junt.appchat.repository.UserRepository;
import com.junt.appchat.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DataInitializer implements CommandLineRunner {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            User user = new User();
            user.setUsername("admin");
            user.setPassword("123");
            userService.saveUser(user);
            User user2 = new User();
            user2.setUsername("user");

            user2.setPassword("123");
            userService.saveUser(user2);
        }
    }
}
