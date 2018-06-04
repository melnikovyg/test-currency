package model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "users")
public class User extends BaseEntity implements Serializable{

    @Column(name = "full_name", nullable = false)
    @NotNull
    private String fullName;

    @Column(name = "login", nullable = false)
    @NotBlank
    private String login;

    @Column(name = "password", nullable = false)
    @NotBlank
    @Size(min=4, max=50)
    private String password;

    @Column(name = "enabled", nullable = false, columnDefinition = "bool default true")
    private boolean enabled;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "roles", joinColumns = @JoinColumn(name = "user_id"))
    @ElementCollection(fetch = FetchType.EAGER, targetClass = Role.class)
    private Set<Role> roles;

    public User() {
    }

    public User(Integer id, @NotNull String fullName, @NotBlank String login, @NotBlank @Size(min = 4, max = 50) String password, boolean enabled, Role... roles) {
        super(id);
        this.fullName = fullName;
        this.login = login;
        this.password = password;
        this.enabled = enabled;
        setRoles(roles);
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Role... roles) {
        this.roles = roles.length==0 ? Collections.emptySet() : EnumSet.copyOf(Arrays.asList(roles));
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }


    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "fullName='" + fullName + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", enabled=" + enabled +
                ", roles=" + roles +
                '}';
    }
}
