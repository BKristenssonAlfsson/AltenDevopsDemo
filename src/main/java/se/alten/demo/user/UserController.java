package se.alten.demo.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class UserController {

    @Autowired
    private UserService userService = new UserService();

    @PostMapping("/add")
    public ResponseEntity<HttpStatus> addUser(@RequestBody UserModel user) {
        Boolean userAdded = userService.registerUser(user);

        if (userAdded) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } else {
            return ResponseEntity.ok(HttpStatus.OK);
        }
    }

    @DeleteMapping(path = "/delete")
    public ResponseEntity<HttpStatus> deleteUser(@RequestBody UserModel userModel) {
        userService.deleteUser(userModel);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}