# FoodCoach-server

> Backend server for FoodCoach platform.

This is the backend part of the FoodCoach platform (the amazing server).

This is an application that responds to HTTP requests that is sent to it. These requests can come
from a number of different sources, a web browser or a mobile app for example.

## SBT

SBT strands for Scala Build Tool and that is just what it is. This tool compiles and
packages our code so that it can run as a server. We need this to compile our project and host a
local server (see below).

## Java Play

The backend is written in Java and uses a web application framework called
[Java Play](https://github.com/playframework/playframework). It is actually called just
_"Play framework"_ but the Java implementation is called Java Play. The framework is written in a
different programming language called _Scala_ but we don't need to worry about that since we are
only going to use the Java API that it provides.

This framework contains all sorts of fancy stuff. It can be used as a _full-stack_ framework for
rendering an entire web application. We however only use it to serve a backend server that contain
all our business-logic as well as talk our database. All of the stuff that actually renders a
webpage (frontend client) is instead written in a separate application that communicates with this
application via HTTP requests.

Besides the classes and the structure that belongs to the framework the application will consist of
just plain old Java code.

## Setup

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
mysql -u root
create database foodcoach;
quit
```

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

## Routes

Will post more information about all the routes that are available here,
until then you can look in the conf/**routes** file to see what URL's the server responds to!


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
