package id.timesheet.api.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "tb_m_user_account")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class UserAccount extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(name = "username", length = 100, unique = true)
    private String username;

    @Column(name = "email", length = 100, unique = true, nullable = false)
    private String email;

    @Column(name = "password", length = 100, nullable = false)
    private String password;

    @OneToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    private Employee employee;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<Role> myRoles = List.of(role);
        return myRoles.stream().map(userRole -> new SimpleGrantedAuthority("ROLE_" + role.getName())).toList();
    }

    public String getUsername() {
        return email;
    }

    public String getUsernameField() {
        return username;
    }

    public void setUsernameField(String username) {
        this.username = username;
    }
}
