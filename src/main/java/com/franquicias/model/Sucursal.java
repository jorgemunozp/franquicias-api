package com.franquicias.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "sucursal",
        uniqueConstraints = {
            @UniqueConstraint(
                    columnNames = {"id_franquicia", "nombre"}
            )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Sucursal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @ManyToOne
    @JoinColumn(
            name = "id_franquicia",
            nullable = false
    )
    private Franquicia franquicia;

//    public Sucursal(String nombre, Franquicia franquicia) {
//        super();
//        this.nombre = nombre;
//        this.franquicia = franquicia;
//    }

//    @Override
//    public String toString() {
//        return "Sucursal{" +
//                "id_sucursal=" + id_sucursal +
//                ", nombre='" + nombre + '\'' +
//                ", franquicia=" + franquicia +
//                '}';
//    }
}
