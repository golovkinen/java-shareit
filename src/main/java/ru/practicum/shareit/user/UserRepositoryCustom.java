package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
public class UserRepositoryCustom implements IUserRepositoryCustom {
    @Autowired
    private EntityManager entityManager;

    private final IUserRepository iUserRepository;

    public UserRepositoryCustom(IUserRepository iUserRepository) {
        this.iUserRepository = iUserRepository;
    }

    @Override
    public boolean updateUser(User user, int userId) {
        Optional<User> userToUpdate = iUserRepository.findById(userId);

        if (userToUpdate.isEmpty()) {
            return false;
        }

        if (iUserRepository.findByEmail(user.getEmail()).isPresent()) {
            return false;
        }

        if (user.getName() != null) {
            userToUpdate.get().setName(user.getName());
        }
        if (user.getEmail() != null) {
            userToUpdate.get().setEmail(user.getEmail());
        }

        iUserRepository.save(userToUpdate.get());
        return true;
    }

    @Override
    public boolean deleteById(int id) {

        if (iUserRepository.findById(id).isEmpty()) {
            return false;
        }
        iUserRepository.deleteById(id);
        return true;
    }

    @Override
    public void deleteAll() {
        iUserRepository.deleteAll();
    }

}
