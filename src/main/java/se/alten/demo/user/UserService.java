package se.alten.demo.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Boolean registerUser(final UserModel newUser) {
        final User user = new User();

        user.setPassword(passwordEncoder.encode(newUser.getPassword()));
        user.setUsername(newUser.getUsername());
        user.setActive(newUser.getActive());
        user.setRoles(newUser.getRole());

        User exist = userRepository.findByUsername(newUser.getUsername());

        if ( exist == null ) {
            userRepository.save(user);
            return false;
        } else {
            return true;
        }
    }

    public void deleteUser(UserModel userModel) {
        User user = new User();
        user.setUsername(userModel.getUsername());
        userRepository.delete(user);
    }
}