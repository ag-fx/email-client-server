# email-client-server
This is a project demonstrating the principles of socket programming in Kotlin.

Communication Networks, 5th semester of Computer Science Dept. @ Aristotle University of Thessaloniki

## About
This repository essentially contains two programs, one for the mail server and one for the mail client (application).

The first one can be found in **email-server/**, which is a basic threaded Socket Server waiting for incoming requests.
The other one can be found in **email-client/**, which is a [TornadoFX](https://github.com/edvin/tornadofx) application for the client.
TornadoFX is a JavaFX framework for Kotlin which supports the *Model-View-Controller (MVC)* pattern.

The package that's connecting the two programs can be found in **email-common/**, which contains all the common entities
and the request/response types that are available to both the client and the server so that they can communicate efficiently.

The JDK version that was used for development is **11.0.1+13-LTS** so in case any issues with TornadoFX show up, you can
try downloading that specific version to see if there's been a breaking change.
The app should run on both Windows and Linux.

## Compilation
You can compile this multi-project with `gradle shadowJar`, which will build the executable jars in **email-client/build/libs/**
and **email-server/build/libs/** for the client and the server respectively. (This makes everything *so* much easier, doesn't it?!)
