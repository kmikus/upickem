package com.upickem.payload;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

public class LeagueRequest {

    @NotBlank
    @Size(min=1, max=50)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
