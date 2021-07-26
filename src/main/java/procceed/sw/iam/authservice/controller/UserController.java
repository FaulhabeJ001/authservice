package procceed.sw.iam.authservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import procceed.sw.iam.authservice.entities.MyUser;
import procceed.sw.iam.authservice.services.UserService;

import javax.validation.Valid;
import java.util.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    // todos: exception handling

    @PostMapping
    public ResponseEntity createUser(UriComponentsBuilder uriComponentsBuilder,
                                     @Valid @RequestBody MyUser user) {
        userService.createUser(user);

        UriComponents uriComponents = uriComponentsBuilder.path("/users/" + user.getId()).build();

        return ResponseEntity.created(uriComponents.toUri()).build();
    }

    @GetMapping
    public ResponseEntity<List<MyUser>> getAllUsers() {

        List<MyUser> users = userService.getAllUsers();

        return ResponseEntity.ok(users);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return errors;
    }
}
