package services;


import model.User;

import java.util.List;

public interface UserService {

    User create(User user);

    User get(int id);

    List<User> getAll();

    User update(User user);

    boolean delete(int id);



}
