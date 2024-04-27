package cl.company.center.medical.controller;

import cl.company.center.medical.exception.CenterMedicalNotFoundException;
import cl.company.center.medical.exception.ErrorResponse;
import cl.company.center.medical.model.Doctor;
import cl.company.center.medical.model.Patient;
import cl.company.center.medical.model.User;
import cl.company.center.medical.service.MedicalService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequestMapping("/api")
public class CenterMedicalController {

    private static final Logger log = LoggerFactory.getLogger(CenterMedicalController.class);


    @Autowired
    private MedicalService medicalService;


    //Interceptor para exceptciones
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        List<String> errors = new ArrayList<>();
        bindingResult.getFieldErrors().forEach(error -> {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }

    //Interceptor para exceptciones
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        String errorMessage = "Error de violación de restricción única: " + ex.getMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(errorMessage));
    }

    @ExceptionHandler(CenterMedicalNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRequestException(CenterMedicalNotFoundException ex) {
        return ResponseEntity.status(ex.getStatus()).body(new ErrorResponse(ex.getMessage()));
    }

    @GetMapping("/findUser")
    public EntityModel<?>findUser(@RequestHeader(value = "user",required = false) String user,
                                  @RequestHeader(value = "password",required = false) String password) {

        // Verificar si se proporcionaron los parámetros requeridos
        if (user == null || password == null || user.isEmpty() || password.isEmpty()) {
            log.error("Alguno de los parámetros no se ingresaron");
            return EntityModel.of(new ErrorResponse("Alguno de los parámetros no se ingresaron"));
        }

        // Verificar la validez del usuario ingresado
        boolean userValid = medicalService.findUser(user, password);
        if (!userValid) {
            log.error("Usuario no autorizado: {}", user);
            return EntityModel.of(new ErrorResponse("No está autorizado para ejecutar esta petición"));
        }

        // Buscar al usuario en el servicio de ecommerce
        Optional<User> optionalUser = medicalService.findUser(user);
        if (optionalUser.isPresent()) {
            User foundUser = optionalUser.get();
            log.info("Usuario encontrado: {}", foundUser.getUsuario());
            return EntityModel.of(EntityModel.of(foundUser,
                    linkTo(methodOn(this.getClass()).findUser(user, password)).withSelfRel()));
        } else {
            log.warn("No se encontró información para el usuario: {}", user);
            return EntityModel.of(new ErrorResponse("No se encontró información para el usuario: " + user));
        }
    }


    // Endpoint que busca todos los medicos
    @GetMapping("/findAllDoctor")
    public CollectionModel<EntityModel<Doctor>>findAllDoctor(@RequestHeader(value = "user",required = false) String user,
                                                             @RequestHeader(value = "password",required = false) String password) {

        // Verificar si se proporcionaron los parámetros requeridos
        if (user == null || password == null || user.isEmpty() || password.isEmpty()) {
            log.error("Alguno de los parámetros no se ingresaron");
            throw new CenterMedicalNotFoundException(new ErrorResponse("Algunos de los parámetros no se ingresaron"), HttpStatus.BAD_REQUEST);
        }

        // Verificar la validez del usuario ingresado
        boolean userValid = medicalService.findUser(user, password);
        if (!userValid) {
            log.error("Usuario no autorizado: {}", user);
            throw new CenterMedicalNotFoundException(new ErrorResponse("No está autorizado para ejecutar esta petición"), HttpStatus.UNAUTHORIZED);
        }

        final List<Doctor> doctors = medicalService.getAllDoctor();
        final List<EntityModel<Doctor>> doctorModels = doctors.stream()
                .map(r -> EntityModel.of(r,
                        linkTo(methodOn(CenterMedicalController.class).findDoctor(r.getRun(),user,password)).withSelfRel(),
                        linkTo(methodOn(CenterMedicalController.class).findAllDoctor(user, password)).withRel("usuarios")))
                .collect(Collectors.toList());
        return CollectionModel.of(doctorModels, linkTo(methodOn(CenterMedicalController.class).findAllDoctor(user,password)).withSelfRel());

    }

    // Endpoint que busca un medico por su nombre
    @GetMapping("/findDoctorByRun/{run}")
    public EntityModel<?>findDoctor(@PathVariable String run,
                                      @RequestHeader(value = "user",required = false) String user,
                                      @RequestHeader(value = "password",required = false) String password) {

        if (StringUtils.containsWhitespace(run) || (user == null || password == null) || user.isEmpty() || password.isEmpty()) {
            log.error("Algunos de los parámetros no se ingresaron");
            return EntityModel.of(new ErrorResponse("Alguno de los parámetros no se ingresaron"));
        }

        // Verificar la validez del usuario
        final boolean userValid = medicalService.findUser(user, password);
        if (!userValid) {
            log.error("Usuario no autorizado: {}", user);
            return EntityModel.of(new ErrorResponse("No está autorizado para ejecutar esta petición"));
        }
        final Optional<Doctor> optionalDoctor = medicalService.findDoctorByRun(run);
        if (optionalDoctor.isPresent()) {
            Doctor foundDoctor = optionalDoctor.get();
            log.info("Doctor encontrado: {}", foundDoctor.getNombre());
            return EntityModel.of(EntityModel.of(foundDoctor,
                    linkTo(methodOn(this.getClass()).findUser(user, password)).withSelfRel()));
        } else {
            log.warn("No se encontró información para el doctor: {}", run);
            return EntityModel.of(new ErrorResponse("No se encontró información para el doctor: " + run));
        }

    }

    //Crea un nuevo doctor
    @PostMapping("/createDoctor")
    public EntityModel<?>createDoctor(@Valid @RequestBody Doctor doctor,
                                      @RequestHeader(value = "user",required = false) String user,
                                      @RequestHeader(value = "password",required = false) String password,
                                      BindingResult bindingResult) throws MethodArgumentNotValidException {

        // Verificar si se proporcionaron los parámetros requeridos
        if (user == null || password == null || user.isEmpty() || password.isEmpty()) {
            log.error("Alguno de los parámetros no se ingresaron");
            return EntityModel.of(new ErrorResponse("Alguno de los parámetros no se ingresaron"));
        }

        // validaciones en DTO
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(null, bindingResult);
        }

        // Verificar la validez del usuario ingresado
        boolean userValid = medicalService.findUser(user, password);
        if (!userValid) {
            log.error("Usuario no autorizado: {}", user);
            return EntityModel.of(new ErrorResponse("No está autorizado para ejecutar esta petición"));
        }

        final Doctor createdDoctor = medicalService.createDoctor(doctor);
        return EntityModel.of(createdDoctor,
                linkTo(methodOn(this.getClass()).findDoctor(createdDoctor.getRun(),user,password)).withSelfRel(),
                linkTo(methodOn(this.getClass()).findAllDoctor(user,password)).withRel("all-doctors"));

    }

    //Actualiza un doctor
    @PutMapping("/updateDoctor")
    public EntityModel<?>updateDoctor(@RequestHeader(value = "user",required = false) String user,
                                      @RequestHeader(value = "password",required = false) String password,
                                      @Valid @RequestBody Doctor doctor,
                                      BindingResult bindingResult) throws MethodArgumentNotValidException {

        if ( (user == null || password == null) || user.isEmpty() || password.isEmpty()) {
            log.error("Algunos de los parámetros no se ingresaron");
            return EntityModel.of(new ErrorResponse("Algunos de los parámetros no se ingresaron"));
        }

        // validaciones en DTO
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(null, bindingResult);
        }

        // Verificar la validez del usuario ingresado
        boolean userValid = medicalService.findUser(user, password);
        if (!userValid) {
            log.error("Usuario no autorizado: {}", user);
            return EntityModel.of(new ErrorResponse("No está autorizado para ejecutar esta petición"));
        }

        final boolean existDoctor = medicalService.findDoctorByRun(doctor.getRun()).isPresent();
        if(existDoctor){
            final Doctor updatedDoctor = medicalService.updateDoctor(doctor);
            return EntityModel.of(updatedDoctor,
                    linkTo(methodOn(this.getClass()).findDoctor(updatedDoctor.getRun(),user,password)).withSelfRel(),
                    linkTo(methodOn(this.getClass()).findAllDoctor(user,password)).withRel("all-doctors"));
        }
        return EntityModel.of(new ErrorResponse("No se pudo actualizar el doctor con run:"+doctor.getRun()));
    }

    //Elimina un doctor
    @DeleteMapping("/deleteDoctor/{id}")
    public EntityModel<?>deleteDoctor(@PathVariable String id,
                                      @RequestHeader(value = "user",required = false) String user,
                                      @RequestHeader(value = "password",required = false) String password) {

        if(StringUtils.containsWhitespace(id) || (user == null || password == null)) {
            log.error("Algunos de los parámetros no se ingresaron");
            return EntityModel.of(new ErrorResponse("Algunos de los parámetros no se ingresaron"));
        }

        // Verificar la validez del usuario ingresado
        boolean userValid = medicalService.findUser(user, password);
        if (!userValid) {
            log.error("Usuario no autorizado: {}", user);
            return EntityModel.of(new ErrorResponse("No está autorizado para ejecutar esta petición"));
        }

        final boolean existDoctor = medicalService.findDoctorByRun(id).isPresent();
        if(existDoctor){
            final Optional<Doctor> optionalDoctor = medicalService.findDoctorByRun(id);
            medicalService.deleteDoctor(optionalDoctor.get());
            return EntityModel.of(new ErrorResponse("Doctor eliminado exitosamente"));
        }
        return EntityModel.of(new ErrorResponse("No se pudo eliminar el doctor con run:"+id));
    }

    // Endpoint que busca todos los pacientes
    @GetMapping("/findAllPatient")
    public CollectionModel<EntityModel<Patient>>findAllPatient(@RequestHeader(value = "user",required = false) String user,
                                                               @RequestHeader(value = "password",required = false) String password) {

        // Verificar si se proporcionaron los parámetros requeridos
        if (user == null || password == null || user.isEmpty() || password.isEmpty()) {
            log.error("Alguno de los parámetros no se ingresaron");
            throw new CenterMedicalNotFoundException(new ErrorResponse("Algunos de los parámetros no se ingresaron"), HttpStatus.BAD_REQUEST);
        }

        // Verificar la validez del usuario
        boolean userValid = medicalService.findUser(user, password);
        if (!userValid) {
            log.error("Usuario no autorizado: {}", user);
            throw new CenterMedicalNotFoundException(new ErrorResponse("No está autorizado para ejecutar esta petición"), HttpStatus.UNAUTHORIZED);
        }

        final List<Patient> patients = medicalService.getAllPatient();
        final List<EntityModel<Patient>> patientModels = patients.stream()
                .map(patient -> EntityModel.of(patient,
                        linkTo(methodOn(CenterMedicalController.class).findPatienteByRun(patient.getRun(), user, password)).withSelfRel(),
                        linkTo(methodOn(CenterMedicalController.class).findAllPatient(user, password)).withRel("patients")))
                .collect(Collectors.toList());
        return CollectionModel.of(patientModels, linkTo(methodOn(CenterMedicalController.class).findAllPatient(user, password)).withSelfRel());

    }

    // Busca un paciente por su run
    @GetMapping("/findPatientByRun/{run}")
    public EntityModel<?>findPatienteByRun(@PathVariable String run,
                                           @RequestHeader(value = "user",required = false) String user,
                                           @RequestHeader(value = "password",required = false) String password) {

        if (StringUtils.containsWhitespace(run) || (user == null || password == null) || user.isEmpty() || password.isEmpty()) {
            log.error("Algunos de los parámetros no se ingresaron");
            return EntityModel.of(new ErrorResponse("Alguno de los parámetros no se ingresaron"));
        }

        // Verificar la validez del usuario
        final boolean userValid = medicalService.findUser(user, password);
        if (!userValid) {
            log.error("Usuario no autorizado: {}", user);
            return EntityModel.of(new ErrorResponse("No está autorizado para ejecutar esta petición"));
        }

        final Optional<Patient> optionalPatient = medicalService.findPatientByRun(run);
        if (optionalPatient.isPresent()) {
            Patient foundPatient = optionalPatient.get();
            log.info("Patient encontrado: {}", foundPatient.getNombre());
            return EntityModel.of(EntityModel.of(foundPatient,
                    linkTo(methodOn(this.getClass()).findPatienteByRun(foundPatient.getRun(),user, password)).withSelfRel()));
        } else {
            log.warn("No se encontró información para el patient: {}", run);
            return EntityModel.of(new ErrorResponse("No se encontró información para el patient: " + run));
        }

    }

    //Crea un nuevo paciente
    @PostMapping("/createPatient")
    public EntityModel<?>createPatient(@Valid @RequestBody Patient patient,
                                       @RequestHeader(value = "user",required = false) String user,
                                       @RequestHeader(value = "password",required = false) String password,
                                       BindingResult bindingResult) throws MethodArgumentNotValidException {

        // Verificar si se proporcionaron los parámetros requeridos
        if (user == null || password == null || user.isEmpty() || password.isEmpty()) {
            log.error("Alguno de los parámetros no se ingresaron");
            return EntityModel.of(new ErrorResponse("Alguno de los parámetros no se ingresaron"));
        }

        // validaciones en DTO
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(null, bindingResult);
        }

        // Verificar la validez del usuario ingresado
        boolean userValid = medicalService.findUser(user, password);
        if (!userValid) {
            log.error("Usuario no autorizado: {}", user);
            return EntityModel.of(new ErrorResponse("No está autorizado para ejecutar esta petición"));
        }

        final Patient createdPatient = medicalService.createPatient(patient);
        return EntityModel.of(createdPatient,
                linkTo(methodOn(this.getClass()).findPatienteByRun(createdPatient.getRun(),user,password)).withSelfRel(),
                linkTo(methodOn(this.getClass()).findAllPatient(user,password)).withRel("all-patients"));

    }

    //Actualiza un paciente
    @PutMapping("/updatePatient")
    public EntityModel<?>updatePatient(@RequestHeader(value = "user",required = false) String user,
                                       @RequestHeader(value = "password",required = false) String password,
                                       @Valid @RequestBody Patient patient,
                                       BindingResult bindingResult) throws MethodArgumentNotValidException {

        if ( (user == null || password == null) || user.isEmpty() || password.isEmpty()) {
            log.error("Algunos de los parámetros no se ingresaron");
            return EntityModel.of(new ErrorResponse("Alguno de los parámetros no se ingresaron"));
        }

        // validaciones en DTO
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(null, bindingResult);
        }

        // Verificar la validez del usuario
        final boolean userValid = medicalService.findUser(user, password);
        if (!userValid) {
            log.error("Usuario no autorizado: {}", user);
            return EntityModel.of(new ErrorResponse("No está autorizado para ejecutar esta petición"));
        }

        final Patient updatedPatient = medicalService.updatePatient(patient);
        return EntityModel.of(updatedPatient,
                linkTo(methodOn(this.getClass()).findPatienteByRun(updatedPatient.getRun(),user,password)).withSelfRel(),
                linkTo(methodOn(this.getClass()).findAllPatient(user,password)).withRel("all-patients"));

    }

    //Elimina un paciente
    @DeleteMapping("/deletePatient/{id}")
    public EntityModel<?> deletePatient(@PathVariable String id,
                                                @RequestHeader(value = "user",required = false) String user,
                                                @RequestHeader(value = "password",required = false) String password) {

        if (StringUtils.containsWhitespace(String.valueOf(id)) || (user == null || password == null) || user.isEmpty() || password.isEmpty()) {
            log.error("Algunos de los parámetros no se ingresaron");
            return EntityModel.of(new ErrorResponse("Algunos de los parámetros no se ingresaron"));
        }

        // Verificar la validez del usuario
        final boolean userValid = medicalService.findUser(user, password);
        if (!userValid) {
            log.error("Usuario no autorizado: {}", user);
            return EntityModel.of(new ErrorResponse("No está autorizado para ejecutar esta petición"));
        }
        final boolean existPatient = medicalService.existsPatientById(Long.parseLong(id));
        if(existPatient){
            medicalService.deletePatient(Long.parseLong(id));
            return EntityModel.of(new ErrorResponse("Patient eliminado exitosamente"));
        }
        return EntityModel.of(new ErrorResponse("No se pudo eliminar el patient con id:"+id));
    }


}
