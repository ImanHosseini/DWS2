# DWS2: Dev Warz Server 
![alt text](https://github.com/ImanHosseini/DWS2/blob/master/logo.png)

This is course project for DB. It was to implement a database for a massively-multiplayer game, 
for which we also implemented the game itself. The server communicates with the clients via http protocol, and with postgres
via the java driver (JDBC). The server can handle multiple clients, as it makes a new thread for every TCP connection. <br />
It has been tried to do things the right way, from an engineering point of view, and the code is scalable as a result of following 
OOP concepts. For example, there is a class hierarchy between different entities. On top there is a Drawable class which is an 
abstract class, and forces the object to be something the GUI on the client side can draw. Then there is a Resource class
extending drawable, and finally any Resource, like Wood is a class extending that. <br />
![alt text](https://github.com/ImanHosseini/DWS2/blob/master/world1%40localhost.png)
This is the UML of the World1 database. The game also has two other databases, accounts (for account bookkeeping) and chat (for player chats).
