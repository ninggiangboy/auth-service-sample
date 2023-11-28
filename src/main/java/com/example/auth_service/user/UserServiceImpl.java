package com.example.auth_service.user;

import com.example.auth_service.exception.DuplicateException;
import com.example.auth_service.registration.RegistrationRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper mapper;
    private final PasswordEncoder encoder;

    @Override
    public User createUser(RegistrationRequest registrationRequest) throws MethodArgumentNotValidException {
        if (userRepository.existsByEmail(registrationRequest.getEmail())) {
            throw new DuplicateException("Email already exists");
        }
        if (userRepository.existsByUsername(registrationRequest.getUsername())) {
            throw new DuplicateException("Username already exists");
        }
        User user = mapUser(registrationRequest);
        user.setPassword(encoder.encode(registrationRequest.getPassword()));
        user.getRoles().add(roleRepository.findByName("USER")
                .orElse(roleRepository.save(Role.builder().name("USER").build())));
        return userRepository.save(user);
    }

    private User mapUser(Object source) {
        return mapper.map(source, User.class);
    }
}
