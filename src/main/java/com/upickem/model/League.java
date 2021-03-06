package com.upickem.model;

import com.upickem.model.audit.TableAudit;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="league", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"suffix"})
})
public class League extends TableAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @Size(min = 1, max = 50)
    private String name;

    @NotBlank
    private String suffix;

    @OneToOne
    @NotNull
    private User commissioner;

    public League(String name, String suffix, User commissioner) {
        this.name = name;
        this.suffix = suffix;
        this.commissioner = commissioner;
    }

    public League() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public User getCommissioner() {
        return commissioner;
    }

    public void setCommissioner(User commissioner) {
        this.commissioner = commissioner;
    }
}
