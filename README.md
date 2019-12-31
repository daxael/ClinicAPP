# ClinicAPP

![Screenshot_2019-08-15-19-36-07-981_com example clinicapp](https://user-images.githubusercontent.com/49367885/63115151-d9b49180-bf96-11e9-9039-632db0afb004.png)   ![Screenshot_2019-08-15-19-37-06-455_com example clinicapp](https://user-images.githubusercontent.com/49367885/63115172-e2a56300-bf96-11e9-8fa0-11bd48367bf5.png)



Pequeña aplicación pensada para servir de apoyo a una clínica; está hecha a modo de simulacion mediante una base de datos local.
En ella se pueden administrar datos de los usuarios que forman parte de la clínica, que principalmente
son los pacientes y los médicos.
Cada usuario tiene su propio nombre y contraseña, con la que se podrá acceder desde el login.

He agregado funcionalidades como que el usuario de la aplicación pueda contratar servicios, pueda enviar mensajes
a los distintos doctores (y éstos puedan recibirlos y responderlos), que pueda acceder a los datos generales de la clínica (incluyendo su posición en Google Maps) y que pueda acceder a sus datos personales registrados (peso, altura, foto...).
También tiene la opción de entrar a configuración y cambiar su nombre de usuario y su contraseña.

Por lo tanto, principalmente se verá a nivel de código como manejar datos desde una bbdd SQL (insertar, modificar y borrar) y algunos sistemas como el de mensajería (dar de alta datos y relacionarlos dentro de la BBDD, para posteriormente cargarlos en una listview que contenga la información de dichos mensajes) y contratar servicios (revisar el estado de dichos servicios en la bbdd, y dar la opción de darlos de alta o cancelarlos).

El proyecto está enteramente comentado en castellano.
