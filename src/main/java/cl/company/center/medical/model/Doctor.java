package cl.company.center.medical.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


@Entity(name = "Doctor")
public class Doctor {
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
    @Column(name = "especializacion")
    @NotBlank(message = "No puede ingresar una especializacion vacio")
    @NotNull(message = "No puede ingresar una especializacion nulo")
    private String especializacion;
    @Column(name = "celular")
    @NotBlank(message = "No puede ingresar un celular vacio")
    @NotNull(message = "No puede ingresar un celular nulo")
    private String celular;
    @Column(name = "email")
    @NotBlank(message = "No puede ingresar un email vacio")
    @NotNull(message = "No puede ingresar un email nulo")
    @Email(message = "No puede ingresar un email invalido")
    private String email;
    @Column(name = "horaconsulta")
    @NotBlank(message = "No puede ingresar un horaConsulta vacio")
    @NotNull(message = "No puede ingresar un horaConsulta nulo")
    private String horaConsulta;
    @Column(name = "hospital")
    @NotBlank(message = "No puede ingresar un hospital vacio")
    @NotNull(message = "No puede ingresar un hospital nulo")
    private String hospital;

    @Column(name = "run")
    @NotBlank(message = "No puede ingresar un run vacio")
    @NotNull(message = "No puede ingresar un run nulo")
    private String run;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEspecializacion() {
        return especializacion;
    }

    public void setEspecializacion(String especializacion) {
        this.especializacion = especializacion;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHoraConsulta() {
        return horaConsulta;
    }

    public void setHoraConsulta(String horaConsulta) {
        this.horaConsulta = horaConsulta;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getRun() {
        return run;
    }

    public void setRun(String run) {
        this.run = run;
    }
}