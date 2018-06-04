package services;

import Utils.UserAuthenticated;
import Utils.NotFoundException;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import repository.UserRepository;

import java.util.List;

import static Utils.Checkers.checkNonNull;

@Service("userService")
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    UserRepository repository;

    @Override
    public User create(User user) {
        return repository.save(user);
    }

    @Override
    public User get(int id) {
        return repository.findById(id).orElseThrow(()-> new NotFoundException("Get, id:"+id));
    }

    @Override
    public List<User> getAll() {
        return checkNonNull(repository.findAll(), "Get all");
    }

    @Override
    public User update(User user) {
        return repository.save(user);
    }

    @Override
    public boolean delete(int id) {
        return checkNonNull(repository.delete(id), "Delete, id:"+id);
    }

    @Override
    public UserAuthenticated loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = repository.findByLogin(login);
        if (user == null) {
            throw new UsernameNotFoundException("User " + login + " is not found");
        }
        return new UserAuthenticated(user);
    }

}
