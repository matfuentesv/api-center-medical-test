# API del Centro Médico

Esta API proporciona funcionalidades para gestionar médicos, pacientes y historiales médicos en un centro médico.

## Endpoints Disponibles

### Usuarios

- **Buscar Usuario**
  - Método: GET
  - Ruta: `/api/findUser`
  - Descripción: Busca un usuario por nombre de usuario y contraseña.
  - Ejemplo de Ejecución:
    - **Entrada:**
      - Cabeceras:
        - user: "usuario123"
        - password: "contraseña123"
    - **Salida Exitosa (200 OK):**
      - Cuerpo de la Respuesta:
        ```json
        {
          "id": 123,
          "nombre": "Usuario de Ejemplo",
          "email": "usuario@example.com",
          ...
        }
        ```
    - **Salida Errónea (400 Bad Request):**
      - Cuerpo de la Respuesta:
        ```json
        {
          "error": "Algunos de los parámetros no se ingresaron"
        }
        ```

### Médicos

- **Buscar Todos los Médicos**
  - Método: GET
  - Ruta: `/api/findAllDoctor`
  - Descripción: Busca todos los médicos disponibles en el sistema.
  - Ejemplo de Ejecución: (mismo formato que el anterior)

- **Buscar Médico por Run**
  - Método: GET
  - Ruta: `/api/findDoctorByRun/{run}`
  - Descripción: Busca un médico por su Run.
  - Ejemplo de Ejecución: (mismo formato que el anterior)

- **Crear Médico**
  - Método: POST
  - Ruta: `/api/createDoctor`
  - Descripción: Crea un nuevo médico en el sistema.
  - Ejemplo de Ejecución: (mismo formato que el anterior)

- **Actualizar Médico**
  - Método: PUT
  - Ruta: `/api/updateDoctor/{id}`
  - Descripción: Actualiza un médico existente en el sistema.
  - Ejemplo de Ejecución: (mismo formato que el anterior)

- **Eliminar Médico**
  - Método: DELETE
  - Ruta: `/api/deleteDoctor/{id}`
  - Descripción: Elimina un médico del sistema.
  - Ejemplo de Ejecución: (mismo formato que el anterior)

### Pacientes

- **Buscar Todos los Pacientes**
  - Método: GET
  - Ruta: `/api/findAllPatient`
  - Descripción: Busca todos los pacientes disponibles en el sistema.
  - Ejemplo de Ejecución: (mismo formato que el anterior)

- **Buscar Paciente por Run**
  - Método: GET
  - Ruta: `/api/findPatientByRun/{run}`
  - Descripción: Busca un paciente por su Run.
  - Ejemplo de Ejecución: (mismo formato que el anterior)

- **Crear Paciente**
  - Método: POST
  - Ruta: `/api/createPatient`
  - Descripción: Crea un nuevo paciente en el sistema.
  - Ejemplo de Ejecución: (mismo formato que el anterior)

- **Actualizar Paciente**
  - Método: PUT
  - Ruta: `/api/updatePatient/{id}`
  - Descripción: Actualiza un paciente existente en el sistema.
  - Ejemplo de Ejecución: (mismo formato que el anterior)

- **Eliminar Paciente**
  - Método: DELETE
  - Ruta: `/api/deletePatient/{id}`
  - Descripción: Elimina un paciente del sistema.
  - Ejemplo de Ejecución: (mismo formato que el anterior)
