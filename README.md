# Microblogging
A basic microblogging system implemented with Java RMI
# Specifications
This system is composed by 3 different entities.
1. Servidor is responsible for controlling the authentication process of users of the system and managing their messages.
2. Base de Datos is responsible for storing all system data: Users, Followers...; only Servidor can use the service that supplies this entity
3. Cliente allows the the users to interact with each other by sending messages and becoming followers of each other. Implements a callback to receive the posts of those users they follow.
# Software used
This application was created with Eclipse IDE: 2022-12
# Setup
There are 3 bat files allowing the user to launch the application.
It is important to launch first basededatos.bet, then servidor.bat and later cliente.bat since the first gives service to the next one.
