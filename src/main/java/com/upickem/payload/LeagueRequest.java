package com.upickem.payload;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;
import java.util.List;

public class LeagueRequest {

    @NotBlank
    @Size(min=1, max=50)
    private String name;

    private List<String> usernamesOrEmails;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getUsernamesOrEmails() {
        return usernamesOrEmails;
    }

    public void setUsernamesOrEmails(List<String> usernamesOrEmails) {
        this.usernamesOrEmails = usernamesOrEmails;
    }
}
