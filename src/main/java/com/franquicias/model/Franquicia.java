package com.franquicias.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "franquicia")
@Getter
@Setter
@NoArgsConstructor
public class Franquicia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

//    public Franquicia(Long id, String nombre) {
//        super();
//        this.id = id;
//        this.nombre = nombre;
//    }
}
