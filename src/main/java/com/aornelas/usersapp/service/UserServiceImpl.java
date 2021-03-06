package com.aornelas.usersapp.service;

import com.aornelas.usersapp.domain.User;
import com.aornelas.usersapp.exception.*;
import com.aornelas.usersapp.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService {
    private final UserRepository userRepo;
    @Autowired
    public UserServiceImpl(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public List<User> findAll() {
        return userRepo.findAll();
    }

    @Override
    public User create(User newUser) {

        Optional<String> currentEmail = userRepo
            .findByEmail(newUser.getEmail());

        Optional<String> currentPhoneNumber = userRepo
            .findByPhoneNumber(newUser.getPhoneNumber());

        if (currentEmail.isPresent()) {
            throw new EmailTakenException(
                String.format("The email %s is already taken by another user",
                    newUser.getEmail()));
        }

        if (currentPhoneNumber.isPresent()) {
            throw new PhoneNumberTakenException(
                String.format("The phone number %s is already taken by another user",
                    newUser.getPhoneNumber()));
        }

        try {
            User userSaved = userRepo.save(newUser);
            return userSaved;
        }catch (NotValidUserException e) {
            throw new NotValidUserException();
        }
    }

    @Override
    public User getUser(Long id) {
        Optional<User> userOptional = userRepo.findById(id);
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new NotFoundUserException(
                String.format("The user ID=%s was not found!", id)
            );
        }
    }

    @Override
    public boolean deleteUser(Long id) {
        Optional<User> userOptional = userRepo.findById(id);

        if (userOptional.isPresent()){
            userRepo.deleteById(id);
            return true;
        }else{
            throw new NotFoundUserException(
                String.format("The user ID=%s was not found!", id)
            );
        }
    }

    @Override
    public User updateUser(User user) {
        if (user.getId() == null) {
            throw new NotNullIdException();
        }

        Optional optional = userRepo.findById(user.getId());

        if (optional.isPresent()) {
            try {
                User userToUpdate = (User) optional.get();
                userToUpdate.setFirstName(user.getFirstName());
                userToUpdate.setLastName(user.getLastName());
                userToUpdate.setEmail(user.getEmail());
                userToUpdate.setDob(user.getDob());
                userToUpdate.setPassword(user.getPassword());

                User updatedUser = userRepo.save(userToUpdate);

                return updatedUser;

            } catch (NotValidUserException e){
                throw new NotValidUserException();
            }
        }else{
            throw new NotFoundUserException(
                String.format("User with ID=%s was not found!", user.getId())
            );
        }
    }
}
