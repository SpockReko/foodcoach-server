# FoodCoach-server

> Backend server for FoodCoach platform.

This is the backend part of the FoodCoach platform (the amazing server).

This is an application that responds to HTTP requests that is sent to it. These requests can come
from a number of different sources, a web browser or a mobile app for example.

## SBT

SBT stands for Scala Build Tool and that is just what it is. This tool compiles and
packages our code so that it can run as a server. We need this to compile our project and host a
local server (see below). We also run some separate tasks using this.

## Java Play

The code is written in Java and uses a web application framework called
[Java Play](https://github.com/playframework/playframework). It is actually called just
_"Play framework"_ but the Java implementation is called Java Play. The framework core is written
in a different programming language called _Scala_ but we don't need to worry about that
since we are only use the Java API that it provides.

This framework contains all sorts of fancy stuff. It can be used as a _full-stack_ framework for
rendering an entire web application. We however only use it to serve a backend server that contain
all our business-logic as well as talk our database. All of the stuff that actually renders a
webpage (frontend client) is instead written in a separate application that communicates with this
application via HTTP requests.

Besides the classes and the structure that belongs to the framework the application will consist of
just plain old Java code.

## Ebean and JPA

[Ebean](http://ebean-orm.github.io/) is an Object Relational Mapper (ORM). Ebean converts our Java object
to their database representation automatically.

Model classes are annotated with stuff like `@Column` and `@ManyToOne`. This tells Ebean
how to represent these Java classes in the database. Ebean takes care of everything that has to do
with the database as long as we give it all this information on how the relations between
Java objects would look like in the database world and not just the object-oriented world.

Most of these annotations comes from JPA (Java Persistance API) which is a standard adopted by
many different database handler like Ebean. In short this is just the Java way to handle storing
objects to a database without having to write plain SQL for every single thing we want to do.

Instead of writing something like this every time we want to update data: 
``` mysql
UPDATE FoodItems SET name='Avocado', energy_kj='500.0' WHERE id=320;
```  
we can just write this if we have modified our Java object in code:  
``` java
avocado.save()
```

And when we want to get objects from the database we don't have to write this:
``` mysql
SELECT * FROM fooditems WHERE fooditems.id = 320;
```
We can just write this in type-safe Java:
``` java
FoodItem.find.byId(320L);
```

## Setup for Mac (Unix) Users

#### Step 1: Create Database

The application needs a database, we currently use MySQL. This is how you install it.
If you dont have [Homebrew](https://brew.sh/index_se.html) installed, install it.
It is truly awesome. When you are done installing. Go into your terminal and type this:

``` bash
brew install mysql
```

Then, you type all this one row at a time to login to your MySQL and
create a database called **foodcoach**. This is the only name the application will recognize.

``` bash
mysql -u username -p password
create database foodcoach;
quit
```

**IMPORTANT:** You have to create a new file called **dbconfig.conf** inside the
_conf_ directory in the project structure where you also write your MySQL username
and password. These differ from setup to setup so all of us may have different settings here.
As a default the **dbconfig.conf** file should look like this:

```
db.default.username=root
db.default.password=""
```

But you may have to change this depending on your setup.

#### Step 2: Create Tables

You can now startup the server.

``` bash
sbt run
```

This will trigger SBT to startup a server at _localhost:9000_. This is like a webpage but it
runs locally on your machine. You can now open up a web browser and
navigate to _localhost:9000_ to see what the server has to say!

It will probably say that you have to apply a evolution script to your database,
click the _apply script_ button and wait until the server is done.

#### Step 3: Seed the Database

You can now go back to your terminal and either stop the server with _Ctrl+C_ or open
up a new terminal window to type this:

``` bash
sbt seed
```

This will run a script that reads the food information from Livsmedelsverket
and puts it into our new empty database!

#### Step 4: Enjoy

You can now start the server again with **sbt run** and enjoy the
application up and running with a complete food database!

## Setup for Windows users

#### Step 1: Install mysql server

First you should download mysql server some how. 
One way to do this is to download [mysql installer for windows](https://dev.mysql.com/downloads/installer/)
After your install you going to config the server. Here you choose a username and a password. 
Its **important** that you remember the **username and the password**. 

After this you need to set the PATH to the bin file in the sql server folder

#### Step 2: Create Database
Then the Path is set you can write:

``` bash
C:\**\foodcoach-server>mysql -u <username> -p
password: <password>
Welcome to the MySQL monitor.  Commands end with ; or \g.
Your MySQL connection id is 63
Server version: 5.7.17-log MySQL Community Server (GPL)

Copyright (c) 2000, 2016, Oracle and/or its affiliates. All rights reserved.

Oracle is a registered trademark of Oracle Corporation and/or its
affiliates. Other names may be trademarks of their respective
owners.

Type 'help;' or '\h' for help. Type '\c' to clear the current input statement.

mysql> create database foodcoach;
Query OK, 1 row affected (0.00 sec)

mysql>quit
Bye

C:\**\foodcoach-server>
```

**\<username>** and **\<password>** is what you wrote under the config step from **Step 1**. 

#### Step 3: Create config file
Now you need to create a config file in **conf** folder with the name **_dbconfig.conf_**, if you have problem as med to create a config file you can copy the existing config file **application** and change the name.

In the file you write following: 

```
db.default.username=<username>
db.default.password=<password>
```

As in step 2 the username and password is from the config step in **step 1**.

#### Step 4: Seed the Database

Now you go back to the consol and write

```
C:\**\foodcoach-server>sbt run
 ....
 ....
 ....
 
 --- (Running the application, auto-reloading is enabled) ---

[info] p.c.s.NettyServer - Listening for HTTP on /0:0:0:0:0:0:0:0:9000

(Server started, use Ctrl+D to stop and go back to the console...)

```

This will trigger SBT to startup a server at _localhost:9000_. This is like a webpage but it
runs locally on your machine. You can now open up a web browser and
navigate to _localhost:9000_ to see what the server has to say!

It will probably say that you have to apply a evolution script to your database,
click the _apply script_ button and wait until the server is done.

Then you go back to the console and press _Ctrl+D_.
Then you writhe following:

```
C:\**\foodcoach-server>stb seed
Java HotSpot(TM) 64-Bit Server VM warning: ignoring option MaxPermSize=256m; support was removed in 8.0
[info] Loading project definition from C:\Users\stefa\Projects\foodcoach-server\project
[info] Set current project to foodcoach (in build file:/C:/Users/stefa/Projects/foodcoach-server/)
[info] Running tasks.DatabaseSeeder

--- (Seeding database) ---
....
....
....

Linking food groups parents... Done

[success] Total time: 4 s, completed 2017-feb-25 20:32:27

C:\**\foodcoach-server>
```

You can now start the server again with **sbt run** and enjoy the
application up and running with a complete food database!


## Routes

The server can be called with these routes:

`/food/:id`

Where **:id** is the Livsmedelsverkets livsmedelsnummer _(320 for Avokado)_.  
Returns a JSON response containing the food object.

`/food/name/:name`

Where **:name** is the name of the food _(Avokado, Äpple)_.  
Returns a JSON Array response containing the food objects.

`/food/group/:code`

Where **:code** is the LanguaL code of the food group _(A0831 for Baljväxter)_.  
Returns a JSON response containing the food group object.

## Dependencies

If we want to use a dependency (an external library to handle some stuff for us,
like parse HTML for example) we can add this to the **build.sbt** file in the root folder.
The syntax looks like this:

```sbt
libraryDependencies += "org.plugin" % "plugin" % "1.0.0"
```

All packages that are available on [Maven Central](https://search.maven.org/) can be added
to the application in this way.

### Currently installed dependencies:

#### [MySQL Connector](https://dev.mysql.com/downloads/connector/j/)
JDBC driver for MySQL. We use this to connect to the database through our Java code.

#### [jsoup](https://jsoup.org/)  
A parser for HTML. We use this to find and extract text on a webpage,
a list of ingredients for example.

#### [uniVocity-parsers](https://github.com/uniVocity/univocity-parsers)  
A parser for CSV and TSV. We use this to find and extract the information
stored in Excel-sheets for example and put it into database tables.

#### [progressbar](https://github.com/ctongfei/progressbar)  
Used in the awesome seeding script to make the progressbar.

## Code Style

Too keep the Java code nice and consistent we use
[Google's style guide for Java](https://google.github.io/styleguide/javaguide.html).
If you use IntelliJ you can use this [repo](https://github.com/frellan/GoogleStyle)
with a settings file. Follow the instructions there, this makes IntelliJ format the code automatically for you.
