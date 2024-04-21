package cl.company.center.medical.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity(name = "Paciente")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "nombre")
    @NotBlank(message = "No puede ingresar un nombre vacio")
    @NotNull(message = "No puede ingresar un nombre nulo")
    private String nombre;
    @Column(name = "apellido")
    @NotBlank(message = "No puede ingresar un apellido vacio")
    @NotNull(message = "No puede ingresar un apellido nulo")
    private String apellido;
    @Column(name = "edad")
    @Positive(message = "La edad debe ser mayor a cero")
    private int edad;
    @Column(name = "direccion")
    @NotBlank(message = "No puede ingresar una direccion vacio")
    @NotNull(message = "No puede ingresar una direccion nulo")
    private String direccion;
    @Column(name = "celular")
    @NotBlank(message = "No puede ingresar un celular vacio")
    @NotNull(message = "No puede ingresar un celular nulo")
    private String celular;
    @Column(name = "run")
    @NotBlank(message = "No puede ingresar un run vacio")
    @NotNull(message = "No puede ingresar un run nulo")
    private String run;


    public Long getId() {
        return id;
    }

    public Patient setId(Long id) {
        this.id = id;
        return this;
    }

    public String getNombre() {
        return nombre;
    }

    public Patient setNombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public String getApellido() {
        return apellido;
    }

    public Patient setApellido(String apellido) {
        this.apellido = apellido;
        return this;
    }

    public int getEdad() {
        return edad;
    }

    public Patient setEdad(int edad) {
        this.edad = edad;
        return this;
    }

    public String getDireccion() {
        return direccion;
    }

    public Patient setDireccion(String direccion) {
        this.direccion = direccion;
        return this;
    }

    public String getRun() {
        return run;
    }

    public Patient setRun(String run) {
        this.run = run;
        return this;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }
}