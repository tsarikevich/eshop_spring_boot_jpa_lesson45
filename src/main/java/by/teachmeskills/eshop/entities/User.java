package by.teachmeskills.eshop.entities;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@Entity
@ToString
@Table(name = "users")
public class User extends BaseEntity {
    @CsvBindByName
    @NotEmpty(message = "Login must not be empty")
    @Size(min = 2, max = 30, message = "Login must be between 2 and 30 characters")
    @Pattern(regexp = "\\S+", message = "Spaces are not allowed")
    @Column(name = "LOGIN")
    private String login;
    @CsvBindByName
    @NotEmpty(message = "Password must not be empty")
    @Pattern(regexp = "\\S+", message = "Spaces are not allowed")
    @Column(name = "PASSWORD")
    private String password;
    @CsvBindByName
    @Column(name = "NAME")
    private String name;
    @CsvBindByName
    @Column(name = "SURNAME")
    private String surname;
    @CsvBindByName
    @Column(name = "EMAIL")
    private String email;
    @CsvBindByName
    @Column(name = "DATE_OF_BIRTH")
    private LocalDate birthDate;
    @CsvBindByName
    @Column(name = "BALANCE")
    private BigDecimal balance;
    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @ToString.Exclude
    private List<Order> orders;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        User user = (User) o;
        return Objects.equals(login, user.login) && Objects.equals(password, user.password) && Objects.equals(name, user.name) && Objects.equals(surname, user.surname) && Objects.equals(email, user.email) && Objects.equals(birthDate, user.birthDate) && Objects.equals(balance, user.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), login, password, name, surname, email, birthDate, balance);
    }
}
