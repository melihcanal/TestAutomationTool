package com.testautomationtool.service.dto;

import com.testautomationtool.domain.User;
import java.io.Serializable;

/**
 * A DTO representing a user, with only the public attributes.
 */
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String login;

    public UserDTO() {}

    public UserDTO(User user) {
        this.id = user.getId();
        this.login = user.getLogin();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public String toString() {
        return "UserDTO{" + "id='" + id + '\'' + ", login='" + login + '\'' + "}";
    }
}
