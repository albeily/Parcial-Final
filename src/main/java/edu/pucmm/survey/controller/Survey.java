package edu.pucmm.survey.controller;

import edu.pucmm.survey.entity.Form;
import edu.pucmm.survey.entity.User;
import edu.pucmm.survey.service.FormService;
import edu.pucmm.survey.service.UserService;
import org.jasypt.util.text.AES256TextEncryptor;

import java.util.List;

public class Survey {

    private final AES256TextEncryptor encryptor;
    private UserService userService;
    private FormService formService;

    public Survey(AES256TextEncryptor encryptor) {
        this.encryptor = encryptor;
        this.userService = new UserService();
        this.formService = new FormService();
    }

    public boolean submit(User user) {
        boolean ok = false;
        User xUser = userService.find(user.getUsername());

        user.setPassword(encryptor.encrypt(user.getPassword()));
        ok = xUser != null ? userService.edit(user) : userService.create(user);

        return ok;
    }

    public boolean submit(Form form) {
        boolean ok = false;
        Form xForm = formService.find(form.getId());

        ok = xForm != null ? formService.edit(form) : formService.create(new Form(form));

        return ok;
    }

    public boolean submit(List<Form> forms) {
        boolean ok = false;

        for (Form form : forms) {
            ok = submit(form);
        }

        return ok;
    }

    public boolean erase(User user) {
        boolean ok = false;
        User xUser = userService.find(user.getUsername());

        ok = xUser != null ? userService.delete(user.getUsername()) : false;

        return ok;
    }

    public boolean erase(Form form) {
        boolean ok = false;
        Form xForm = formService.find(form.getId());

        ok = xForm != null ? formService.delete(form.getId()) : false;

        return ok;
    }

    public User findUser(String username) {
        User user = userService.find(username);

        return user;
    }

    public boolean existUser(String username) {
        boolean ok = false;
        User user = findUser(username);

        ok = user != null ? true : false;

        return ok;
    }

    public List<User> getUsers() {
        return userService.findAll();
    }

    public List<Form> getForms() {
        return formService.findAll();
    }

    public List<User> getUsersDecrypted() {
        List<User> users = userService.findAll();

        for (User user : users) {
            user.setPassword(encryptor.decrypt(user.getPassword()));
        }

        return users;
    }

    public User auth(String username, String password, boolean encrypted) {
        User user = userService.find(username);

        if (user != null) {
            if (encrypted) {
                if (!(encryptor.decrypt(password).equals(encryptor.decrypt(user.getPassword())))) {
                    user = null;
                }
            } else {
                if (!(password.equals(encryptor.decrypt(user.getPassword())))) {
                    user = null;
                }
            }
        }

        return user;
    }
}
