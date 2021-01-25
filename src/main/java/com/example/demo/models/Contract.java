package com.example.demo.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "contracts")
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(name = "number")
    @NotBlank(message = "Номер телефона не может быть пустым")
    private String number;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "tariff_id")
    private Tariff tariff;

    @ManyToMany
    private Set<Option> option;

    public void add(Option o) {
        if(option == null){
            option = new HashSet<>();
        }
        this.option.add(o);
    }

    public void delete(Option o) {
        this.option.removeIf(option -> option.getId() == o.getId());
    }
}
