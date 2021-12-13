package sojka.maciej.projektPAI.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 45)
    private String email;

    @Column(nullable = false, length = 64)
    private String password;

    @Column(name = "first_name", nullable = false, length = 20)
    @Pattern(regexp = "[a-zA-Z]{2,30}", message="Podaj poprawnie imię")
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 20)
    @Pattern(regexp = "[a-zA-Z]{2,30}", message="Podaj poprawnie nazwisko")
    private String lastName;

    @Column(name = "verification_code", length = 64)
    private String verificationCode;

    private boolean enabled;

    public User(
            String e, String p, String fn, String ln, String vc, boolean en) {
        this.email = e;
        this.password = p;
        this.firstName = fn;
        this.lastName = ln;
        this.verificationCode = "";
        this.enabled = true;
    }
}
