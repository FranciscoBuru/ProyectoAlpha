# ProyectoAlpha
Primer proyecto de Sistemas Distribuidos

Juego Distribuido de WackAMole.

## Descargar

Al descargar repositorio, lo primero que se debe hacer es cambiar en el código las rutas de los .policy, 
poner las nuevas (donde se encuenran en el equipo). 

Para ejecutar localmente no se modificará nada más. Correr los main en Login.java y Administrador.java

Si se quiere jugar desde otro equipo se debe cambiar la dirección IP del lookup de rmi del cliente.

## Estresador
Para correr el estresador se deben modificar cirtos archivos en el servidor.
Los comentarios que digan estresador se deben des-comentar y poner que el método revListos() en partida.java siempre
regrese true. 
Ya que se quitaron los comentarios, correr el main en el paquete Estresador y el main de Administrador.java
