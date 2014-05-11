ZenQuery
========

## Table of Contents

**[Introduction - Enterprise Backend as a Service](#introduction)**

**[Requirements](#requirements)**

**[Getting started](#getting-started)**

**[Using ZenQuery](#usage)**

**[Features](#features)**

**[Support](#support)**

**[License and version information](#license)**

## <a name="introduction"/>Introduction - Enterprise Backend as a Service

ZenQuery allows you to easily access databases through a REST API.

All you have to do is enter your database connection settings and ZenQuery will generate REST API endpoints for all
your database tables views. Moreover, you can easily add custom SQL queries for access via REST API calls.

ZenQuery provides SQL result sets as JSON, XML, HTML or CSV data.

ZenQuery is an Enterprise Backend as a Service along lines of [Parse](https://parse.com/) but for your own databases.
It's like [Heroku Dataclips](https://devcenter.heroku.com/articles/dataclips) but for non-cloud, on-premises databases.

ZenQuery is a Java EE application that's securely hosted on your servers. No data ever leaves your company!

## <a name="requirements"/>Requirements

ZenQuery has the following minimum requirements:

* Java 6
* a Servlet container or application server (stand-alone option available)
* a database server (embedded H2 database available)

## <a name="getting-started"/>Getting started

### Installation

There are two ways of installing ZenQuery:

* using our [stand-alone release](#stand-alone)
* using our [WAR (web application archive) release](#war) with your own Servlet container or application server

#### <a name="stand-alone"/>Stand-alone

ZenQuery is available as a stand-alone release (using the latest [Apache Tomcat](http://tomcat.apache.org/)) under
[https://]

Unpack the archive and start the Tomcat server:
```
tar -xzf ZenQuery-standalone.tar.gz && cd ZenQuery-standalone
./bin/startup.sh
```

(under Windows use startup.bat instead)

The server can be stopped with the following command:
`./bin/shutdown.sh`

(under Windows use shutdown.bat instead)

The stand-alone version uses a pre-configured embedded H2 database for storing application data. For testing this is
fine. For production purposes, however, you should use your own, dedicated database. See the section on
[configuration](#configuration) below for further information.

#### <a name="war"/>WAR

ZenQuery is available as a WAR release for usage in your favourite Servlet container / application server under
[https://]

First, you need to [configure](#configuration) a few settings before installing ZenQuery.

Afterwards, install and deploy the downloaded WAR file as usual.

#### <a name="configuration"/>Configuration

In order to start and run properly two settings need to be configured for ZenQuery:

* the database to store the application data in
* security preferences

#### Database

DATABASE_URL=postgres://willy:willy@localhost:5432/ZenQuery

* as400://
* db2=jdbc:db2://
* derby=jdbc:derby:
* edbc=jdbc:ingres://
* firebirdsql=jdbc:firebirdsql://
* h2=jdbc:h2:./
* hsqldb=jdbc:hsqldb:mem:
* JTurbo=jdbc:JTurbo://
* mysql=jdbc:mysql://
* oracle=jdbc:oracle:thin:@
* postgres=jdbc:postgresql://
* postgresql=jdbc:postgresql://
* sapdb=jdbc:sapdb://
* sqlserver=jdbc:microsoft:sqlserver
* sybase

DDL script

ZenQuery has been tested with H2, MySQL, Oracle Database and PostgreSQL as application databases. If you run into any
problems with another RDBMS please don't hesitate to [contact us](mailto:zenquery-support@fullmontymedia.com).

#### Security

spring.profiles.active=local or spring.profiles.active=public

Set password in spring-security-public.xml in src/main/webapp

## <a name="usage"/>Using ZenQuery

### Database Connections

* jdbc:as400://
* jdbc:db2://
* jdbc:derby:
* jdbc:ingres://
* jdbc:firebirdsql://
* jdbc:h2:
* jdbc:hsqldb:mem:
* jdbc:JTurbo://
* jdbc:mysql://
* jdbc:oracle:thin:@
* jdbc:postgresql://
* jdbc:postgresql://
* jdbc:sapdb://
* jdbc:microsoft:sqlserver
* jdbc:sybase:Tds:

### Queries

#### Transitive Navigation

Primary and foreign keys

### API

\[optional\]

#### Formats

The ZenQuery REST API returns data in the following formats:

* JSON
* XML
* HTML
* CSV

## <a name="features"/>Features

* Generate REST APIs from SQL queries.
* Access your data in JSON, XML or CSV formats.
* Supports all major RDBMS (including Oracle Database, Microsoft SQL Server, IBM DB2, PostgreSQL and MySQL).
* Entirely hosted on-premises. Your data stays with you all the time!
* Conveniently edit your SQL queries and preview your data.
* Version control for SQL queries.
* Snapshots (i.e. materialized views if supported by RDBMS)
* Transitive Navigation
* Variable Interpolation
* Limit and filter query results

## <a name="support"/>Support

If you have any questions, suggestions, problems or feature requests please contact us under
[zenquery-support@fullmontymedia.com](mailto:zenquery-support@fullmontymedia.com)

Please also visit [our website](http://www.zenqry.com/).

## <a name="license"/>License and version information

Commercial / premium versions of ZenQuery will contain the ZenQuery source code (under
[Apache License](http://www.apache.org/licenses/)). The free version is closed source for now.

### Version information

Current version: 1.0
