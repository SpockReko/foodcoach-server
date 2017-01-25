# FoodCoach-server

> Backend server for FoodCoach platform.

This is the backend part of the FoodCoach platform (the amazing server).

This is an applikation that respondes to HTTP requests that is sent to it. These requests can come
from a number of different sources, a web browser or a mobile app for example.

## SBT

SBT strands for Scala Build Tool and that is just what it is. This tool compiles and
packages our code so that it can run as a server. We need this to compile our project and host a
local server (see below).

## Java Play

The backend is written in Java and uses a web application framework called
[Java Play](https://github.com/playframework/playframework). It is actually called just
_"Play framework"_ but the Java implementation is called Java Play. The framework is written in a
different programming language called _Scala_ but we dont need to worry about that since we are
only going to use the Java API that it provides.

This framework contains all sorts of fancy stuff. It can be used as a _full-stack_ framework for
rendering an entire web application. We however only use it to serve a backend server that contain
all our business-logic as well as talk our database. All of the stuff that actually renders a
webpage (frontend client) is instead written in a separate application that communicates with this
application via HTTP requests.

Besides the classes and the structure that belongs to the framwork the applicaion will consist of
just plain old Java code.

## Setup

You start the server via the terminal on your machine.
Open up a window, navigate to the correct folder and type this:

``` bash
sbt run
```

This will trigger SBT to startup a server at localhost:9000. This is like a webpage but it
runs locally on your machine. You can now open up a web browser and
navigate to _localhost:9000_ to see what the server has to say!


## Dependencies

If we want to use a dependency (an external library to handle some stuff for us,
like parse HTML for example) we can add this to the **build.sbt** file in the root folder.
The syntax looks like this:

```scala
libraryDependencies += "org.jsoup" % "jsoup" % "1.10.2"
```

All packages that are available on [Maven Central](https://search.maven.org/) can be added
to the application in this way.

#### Currently installed dependencies:

######[jsoup](https://jsoup.org/)  
A parser for HTML. We use this to find and extract text on a webpage,
a list of ingredients for example.

## Code Style

Too keep the Java code nice and consistent we use
[Google's style guide for Java](https://google.github.io/styleguide/javaguide.html).
If you use IntelliJ _(it's just the best)_ here is a [repo](https://github.com/feggak/GoogleStyle)
with a settings file that you can use to make IntelliJ format the code automatically for you.
