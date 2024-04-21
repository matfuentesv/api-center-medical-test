package cl.company.center.medical.controller;

import cl.company.center.medical.exception.ErrorResponse;
import cl.company.center.medical.model.Doctor;
import cl.company.center.medical.model.Patient;
import cl.company.center.medical.service.LoginService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api")
public class CenterMedicalController {

    private static final Logger log = LoggerFactory.getLogger(CenterMedicalController.class);

    @Autowired
    LoginService loginService;


    //Interceptor
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
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        String errorMessage = "Error de violación de restricción única: " + ex.getMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(errorMessage));
    }

    @GetMapping("/findUser")
    public ResponseEntity<Object> findUser(@RequestHeader(value = "user",required = false) String user,
                                           @RequestHeader(value = "password",required = false) String password) {
        if ((user == null || password == null) || (user.isEmpty() || password.isEmpty())) {
            log.error("Algunos de los parámetros no se ingresaron");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Algunos de los parámetros no se ingresaron"));
        }
        return ResponseEntity.ok(loginService.findUser(user,password));
    }


    // Endpoint que busca el historial medico de un paciente por su run
    @GetMapping("/findHistoryPatientByRun/{run}")
    public ResponseEntity<Object> findHistoryPatientByRun(@PathVariable String run,
                                                          @RequestHeader(value = "user",required = false) String user,
                                                          @RequestHeader(value = "password",required = false) String password) {

        if(StringUtils.containsWhitespace(run) || (user == null || password == null) || (user.isEmpty() || password.isEmpty())) {
            log.error("Algunos de los parámetros no se ingresaron");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Algunos de los parámetros no se ingresaron"));
        }
        return ResponseEntity.ok(loginService.getHistoryPatientByRun(user,password,run));
    }

    // Endpoint que busca todos los medicos
    @GetMapping("/findAllDoctor")
    public ResponseEntity<Object> findAllDoctor(@RequestHeader(value = "user",required = false) String user,
                                                @RequestHeader(value = "password",required = false) String password) {
        if ((user == null || password == null) || (user.isEmpty() || password.isEmpty())) {
            log.error("Algunos de los parámetros no se ingresaron");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Algunos de los parámetros no se ingresaron"));
        }
        return ResponseEntity.ok(loginService.findAllDoctor(user,password));
    }

    // Endpoint que busca un medico por su nombre
    @GetMapping("/findDoctorByRun/{run}")
    public ResponseEntity<Object> findDoctor(@PathVariable String run,
                                             @RequestHeader(value = "user",required = false) String user,
                                             @RequestHeader(value = "password",required = false) String password) {

        if (StringUtils.containsWhitespace(run) || (user == null || password == null) || user.isEmpty() || password.isEmpty()) {
            log.error("Algunos de los parámetros no se ingresaron");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Algunos de los parámetros no se ingresaron"));
        }
        return ResponseEntity.ok(loginService.findDoctor(user, password, run));
    }

    //Crea un nuevo doctor
    @PostMapping("/createDoctor")
    public ResponseEntity<Object> createStudent(@Valid @RequestBody Doctor doctor,
                                                @RequestHeader(value = "user",required = false) String user,
                                                @RequestHeader(value = "password",required = false) String password,
                                                BindingResult bindingResult) throws MethodArgumentNotValidException {

        if ((user == null || password == null) || user.isEmpty() || password.isEmpty()) {
            log.error("Algunos de los parámetros no se ingresaron");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Algunos de los parámetros no se ingresaron"));
        }

        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(null, bindingResult);
        }

        return ResponseEntity.ok(loginService.createDoctor(user,password,doctor));
    }

    //Actualiza un doctor
    @PutMapping("/updateDoctor/{id}")
    public ResponseEntity<Object> updateDoctor(@PathVariable String id,
                                               @RequestHeader(value = "user",required = false) String user,
                                               @RequestHeader(value = "password",required = false) String password,
                                               @Valid @RequestBody Doctor doctor,
                                               BindingResult bindingResult) throws MethodArgumentNotValidException {

        if (StringUtils.containsWhitespace(String.valueOf(id)) || (user == null || password == null) || user.isEmpty() || password.isEmpty()) {
            log.error("Algunos de los parámetros no se ingresaron");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Algunos de los parámetros no se ingresaron"));
        }

        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
        return ResponseEntity.ok(loginService.updateDoctor(user, password, Long.parseLong(id), doctor));
    }

    //Elimina un doctor
    @DeleteMapping("/deleteDoctor/{id}")
    public ResponseEntity<Object> deleteDoctor(@PathVariable String id,
                                               @RequestHeader(value = "user",required = false) String user,
                                               @RequestHeader(value = "password",required = false) String password) {

        if (StringUtils.containsWhitespace(String.valueOf(id)) || (user == null || password == null) || user.isEmpty() || password.isEmpty()) {
            log.error("Algunos de los parámetros no se ingresaron");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Algunos de los parámetros no se ingresaron"));
        }

        return ResponseEntity.ok(loginService.deleteDoctor(user, password, Long.parseLong(id)));
    }

    // Endpoint que busca todos los pacientes
    @GetMapping("/findAllPatient")
    public ResponseEntity<Object> findAllPatient(@RequestHeader(value = "user",required = false) String user,
                                                 @RequestHeader(value = "password",required = false) String password) {
        if ((user == null || password == null) || (user.isEmpty() || password.isEmpty())) {
            log.error("Algunos de los parámetros no se ingresaron");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Algunos de los parámetros no se ingresaron"));
        }
        return ResponseEntity.ok(loginService.findAllPatient(user,password));
    }

    // Busca un paciente por su run
    @GetMapping("/findPatientByRun/{run}")
    public ResponseEntity<Object>findPatienteByRun(@PathVariable String run,
                                             @RequestHeader(value = "user",required = false) String user,
                                             @RequestHeader(value = "password",required = false) String password) {

        if (StringUtils.containsWhitespace(run) || (user == null || password == null) || user.isEmpty() || password.isEmpty()) {
            log.error("Algunos de los parámetros no se ingresaron");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Algunos de los parámetros no se ingresaron"));
        }
        return ResponseEntity.ok(loginService.findPatientByRun(user,password,run));
    }

    //Crea un nuevo paciente
    @PostMapping("/createPatient")
    public ResponseEntity<Object>createPatient(@Valid @RequestBody Patient patient,
                                               @RequestHeader(value = "user",required = false) String user,
                                               @RequestHeader(value = "password",required = false) String password,
                                               BindingResult bindingResult) throws MethodArgumentNotValidException {

        if ((user == null || password == null) || user.isEmpty() || password.isEmpty()) {
            log.error("Algunos de los parámetros no se ingresaron");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Algunos de los parámetros no se ingresaron"));
        }

        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(null, bindingResult);
        }

        return ResponseEntity.ok(loginService.createPatient(user,password,patient));
    }

    //Actualiza un paciente
    @PutMapping("/updatePatient/{id}")
    public ResponseEntity<Object> updatePatient(@PathVariable String id,
                                                @RequestHeader(value = "user",required = false) String user,
                                                @RequestHeader(value = "password",required = false) String password,
                                                @Valid @RequestBody Patient patient,
                                                BindingResult bindingResult) throws MethodArgumentNotValidException {

        if (StringUtils.containsWhitespace(String.valueOf(id)) || (user == null || password == null) || user.isEmpty() || password.isEmpty()) {
            log.error("Algunos de los parámetros no se ingresaron");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Algunos de los parámetros no se ingresaron"));
        }

        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
        return ResponseEntity.ok(loginService.updatePatient(user,password,Long.parseLong(id),patient));
    }

    //Elimina un paciente
    @DeleteMapping("/deletePatient/{id}")
    public ResponseEntity<Object> deletePatient(@PathVariable String id,
                                                @RequestHeader(value = "user",required = false) String user,
                                                @RequestHeader(value = "password",required = false) String password) {

        if (StringUtils.containsWhitespace(String.valueOf(id)) || (user == null || password == null) || user.isEmpty() || password.isEmpty()) {
            log.error("Algunos de los parámetros no se ingresaron");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Algunos de los parámetros no se ingresaron"));
        }
        return ResponseEntity.ok(loginService.deletePatient(user, password, Long.parseLong(id)));
    }


}
