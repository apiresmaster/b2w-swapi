B2W - Desafio - Star Wars API
==================================================

Esta API foi desenvolvida par atender ao desafio da B2W, para criação de uma API para um jogo com o tema Star Wars.

Arquitetura
-----------

Esta aplicação foi desenvolvida usando:

* Java 8
* Maven 4.0.0
* Spring Boot 1.4.4
* Spring Security 4
* Apache Commons Lang 3
* Google Guava 21.0


Começando
---------------

Para rodar a aplicação, basta executar a classe `SwapiApplication.java`. Essa classe subirá uma versão do Tomcat e a aplicação estará disponível.

Usuários
------------------

A API REST foi desenvolvida com usando Basic Authentication. Os usuários são:

* Nome: admin / Senha: admin123
  - Permissão em todos os métodos da API (GET, POST e DELETE).
 
* Nome: user / Senha: user123
  - Permissão somente nos métodos GET da API.

Métodos
------------------

* Listar planetas: 
  - GET
  - /b2w-swapi/planets/

* Incluir planeta: 
  - POST
  - /b2w-swapi/planets/

* Obter planeta por ID: 
  - GET
  - /b2w-swapi/planets/{ID}

* Obter planeta por Nome: 
  - GET
  - /b2w-swapi/planets/{NOME}

* Excluir planeta por ID: 
  - DELETE
  - /b2w-swapi/planets/{ID}
