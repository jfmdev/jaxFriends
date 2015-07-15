jaxFriends
==========

**jaxFriends** is a _Contact REST API_, developed as an example for learning how to develop _RESTful web services_ using Java and JAX-RS.

Features
--------

Standard's user can:

 * Login into the application.
 * Get his list of friends.
 * Create a new friend.
 * Edit or delete an existing friend.
 * Change his username or password.
     
Administrator's users can (additionally) manage the application's users (display the list of users, create a new user or edit an existing user).

Usage
-----

The application should be able to run in any valid _Java EE_ container, such as Tomcat, WildFly, WebLogic, Jetty, GlassFish, etc.

The application makes uses of a MySQL database, whose parameters must be defined in the file `config.properties` (located in the folder `source\WebContent\WEB-INF`). The database is created and populated in the first run, so no SQL scripts needs to be executed in the database.

After installation, two default accounts can be used to login: "admin" and "demo" (whose passwords are respectively "admin" and "demo").

Optionally, you can configure the path in which the log files are going to be created. In order to do it, you must edit the file `tinylog.properties` located in the folder `source\WebContent\WEB-INF`).

Stack
-----

The application was developed using:

 * _Java_ as programming language.
 * _Jersey_ as RESTful framework.
 * _ORMLite_ as ORM library for interact with the database (along with _JDBC_).
 * _Tinylog_ as logger library.
 * _Gson_ for JSON serialization.

License
-------

This application is free software; you can redistribute it and/or
modify it under the terms of the GNU Affero General Public
License as published by the Free Software Foundation; either
version 3 of the License, or (at your option) any later version.

This application is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public
License along with this application; If not, see <http://www.gnu.org/licenses/>.