# Ospedale - Refactor MVC

Este es el desarrollo del Parcial 3 de Programación Orientada a Objetos.

### Datos del Estudiante
* **Nombre:** Yuletza Cañizalez
* **NRC:** 2038

---

## Resumen del trabajo realizado

Para este parcial, refactoricé el proyecto original aplicando la arquitectura **MVC (Modelo-Vista-Controlador)** y persistencia de datos reales en archivos JSON.

### 1. Estructura MVC
*   **Modelo:** Mantuve las clases originales de datos (`User`, `Patient`, `Doctor`, etc.), agregando los métodos *getters* y *setters* que hacían falta para poder consultar la información.
*   **Controladores:** Creé controladores individuales para cada módulo (`AuthController`, `PatientController`, `DoctorController`, etc.). Ellos reciben la información de la interfaz, la validan y la guardan.
*   **Vistas:** Limpié las interfaces visuales antiguas (`LoginView`, `PatientView`, `AdminView`, `DoctorView`). Ahora las vistas no validan datos, solo muestran la información en pantalla.

### 2. Validaciones (en los Controladores)
Moví todas las validaciones de datos de la interfaz hacia los controladores:
*   Cédulas de 12 dígitos.
*   Teléfonos de 10 dígitos.
*   Licencias de doctores en formato `L-XXXXXXXXXX MTL`.
*   Consultorios en formato `O-XXX`.
*   Citas médicas en intervalos de 15 minutos.

### 3. Persistencia con JSON
Usé la librería `org.json` para guardar la información del hospital de forma automática en los archivos `users.json`, `appointments.json` y `hospitalizations.json` cada vez que se agrega o edita un registro.

### 4. Patrón Observer (Bono)
Implementé el patrón Observer para que las tablas de las vistas se actualicen automáticamente en tiempo real cada vez que la base de datos tenga cambios.
