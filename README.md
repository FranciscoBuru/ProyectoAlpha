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

## Descripción

El administrador es el manejador del servidor, el encargado de mandar multicast, recibir mensajes con sockets, mandar los datos de conexión con rmi, llevar la cuenta de puntos en la partida y determinar al ganador.

Un jugador gana una partida cuando le pega a 5 topos.

Cuando se hace Login, el cliente usa rmi para obtener los datos de conexión (Multicast y Sockets). En este rmi el serviodor revisa las listas de jugadores y aprueba o no el login del cliente.

Cuando un cliente está adentro, este debe picar el botón (Listo!) para indicarle al servidor que está listo para jugar. Cuando todos los clientes están listos el servidor inicia el milticast de topos. Este multicas continúa hasta que un jugador es el ganador. Los jugadores avisan al serviodr si han ganado un punto con un mensaje via Sockets TCP.

Cuando acaba la partida se despliega a todos los jugadores el ganador de la misma y se habilita el botón (Listo!). Todos los jugadores que estén en el juego deben de ponerse listos para que inicie una nueva ronda.

IMPORTANTE: Si un jugador desea salir del juego debe oprimir el botón Salir y no el tache.
