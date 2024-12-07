package client;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class User {

    private String email;
    private String password;
    private String name;

    public static User updateName(String name) {
        User user = new User();
        user.setName(name);
        return user;
    }

    public static User updateEmail(String email) {
        User user = new User();
        user.setEmail(email);
        return user;
    }

    }




