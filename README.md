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

The stand-alone version uses a pre-configured embedded H2 database for storing application data. This database is
located under src/main/resources/sql (files: ZenQuery.mv.db and ZenQuery.trace.db) in the ZenQuery directory.

For testing purposes this is perfectly fine. For production purposes, however, you should use your own, dedicated
database. See the section on [configuration](#configuration) below for further information.

#### <a name="war"/>WAR

ZenQuery is available as a WAR release for usage in your favourite Servlet container / application server under
[https://]

First, you need to [configure](#configuration) a few settings before installing ZenQuery (see the section on
[configuration](#configuration) below).

Afterwards, install and deploy the downloaded WAR file as usual.

#### <a name="configuration"/>Configuration

In order to start and run properly two settings need to be configured for ZenQuery:

* the database to store the application data in
* security preferences

#### Database

If you're using a dedicated database for ZenQuery (as you probably should for production) you first need to run a DDL
script that creates the necessary tables. DDL scripts for the following databases are included in src/main/resources/sql
in the ZenQuery directory.

Then you need to set the system property DATABASE_URL according to your database connection settings.

How you set this system property depends on your environment. A common way for doing so is using the -D command line
option for Java, e.g.:

`java -DDATABASE_URL=postgres://username:password@localhost:5432/ZenQuery ...`

The format for the URL is

`PROTOCOL://USERNAME:PASSWORD@HOST:PORT/DATABASE_NAME`

A PostgreSQL DATABASE_URL for example would look like this:
DATABASE_URL=postgres://username:password@localhost:5432/ZenQuery`

The following protocols / RDBMS are available. Please note that only H2, MySQL and PostgreSQL drivers are included with
ZenQuery. If you want to use one of the other RDBMS you have to add the appropriate JDBC driver to the shared libraries
directory of your Servlet container / application server:

* as400 (IBM DB2 AS400)
* db2 (IBM DB2)
* derby (Apache Derby)
* edbc (Ingres Database)
* firebirdsql (Firebird)
* h2 (H2)
* hsqldb (HSQLDB)
* mysql (MySQL)
* oracle (Oracle Database)
* postgres (PostgreSQL)
* sapdb (SAP DB)
* sqlserver (Microsoft SQL Server)
* sybase (Sybase)

ZenQuery has been tested with H2, MySQL, Oracle Database and PostgreSQL as application databases. If you run into any
problems with another RDBMS please don't hesitate to [contact us](mailto:zenquery-support@fullmontymedia.com).

**Please note: Only H2, MySQL and PostgreSQL drivers are included with
ZenQuery. If you want to use one of the other RDBMS you have to add the appropriate JDBC driver to the shared libraries
directory of your Servlet container / application server.**

#### Security

spring.profiles.active=local or spring.profiles.active=public

How you set this system property depends on your environment. A common way for doing so is using the -D command line
option for Java, e.g.:

`java -DDATABASE_URL=postgres://username:password@localhost:5432/ZenQuery ...`

Set password in spring-security-public.xml in src/main/webapp in the ZenQuery directory.

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

#### Arguments and variable interpolation

SELECT * FROM table WHERE field = ?

#### Transitive Navigation

Primary and foreign keys

### API

#### Formats

The ZenQuery REST API returns data in the following formats:
* JSON
* XML
* HTML


* [/api/v1/resultSetForQuery/6.json](/api/v1/resultSetForQuery/6.json) (JSON)
* [/api/v1/resultSetForQuery/6.xml](/api/v1/resultSetForQuery/6.xml) (XML)
* [/api/v1/resultSetForQuery/6.csv](/api/v1/resultSetForQuery/6.csv) (CSV)
* [/api/v1/resultSetForQuery/vertical/false/6.html](/api/v1/resultSetForQuery/vertical/false/6.html) (HTML list)
* [/api/v1/resultSetForQuery/vertical/true/6.html](/api/v1/resultSetForQuery/vertical/true/6.html) (styled HTML list)
* [/api/v1/resultSetForQuery/horizontal/false/6.html](/api/v1/resultSetForQuery/horizontal/false/6.html) (HTML table)
* [/api/v1/resultSetForQuery/horizontal/true/6.html](/api/v1/resultSetForQuery/horizontal/true/6.html)
(styled HTML table)


For instance:
* [/api/v1/resultSetForQuery/6/45,SomeValue.json](/api/v1/resultSetForQuery/6/45,SomeValue.json)
(JSON with interpolated variables)
* [/api/v1/resultSetForQuery/6/size/3.json](/api/v1/resultSetForQuery/6/size/3.json)
(JSON with number of results limited to 3)
* [/api/v1/resultSetForQuery/6/45,SomeValue/size/3.json](/api/v1/resultSetForQuery/6/45,SomeValue/size/3.json)
(JSON with interpolated variables and number of results limited
to 3)

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

Commercial / premium versions of ZenQuery will include the source code (under
[Apache License](http://www.apache.org/licenses/)).

The free version for now is closed source.

### Version information

Current version: 1.0
