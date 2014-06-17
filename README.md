ZenQuery
========

## Table of Contents

**[Introduction - Enterprise Backend as a Service](#introduction)**

**[Requirements](#requirements)**

**[Getting started](#getting-started)**

**[Using ZenQuery](#usage)**

**[Features](#features)**

**[Support](#support)**

**[Version information](#license)**

## <a name="introduction"/>Introduction - Enterprise Backend as a Service

ZenQuery allows you to easily access databases through a REST API.

All you have to do is enter your database connection settings and ZenQuery will generate REST API endpoints for all
your database tables and views. Moreover, you can easily add custom SQL queries for access via REST API calls.

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

* using the [stand-alone release](#stand-alone)
* using the [WAR (web application archive) release](#war) with your own Servlet container or application server

#### <a name="stand-alone"/>Stand-alone

ZenQuery is available as a stand-alone release (using the latest [Apache Tomcat](http://tomcat.apache.org/)) under
[http://fullmontymedia.com/ZenQuery/downloads/ZenQuery-standalone.tar.gz](http://fullmontymedia.com/ZenQuery/downloads/ZenQuery-standalone.tar.gz)

Unpack the archive and start the Tomcat server:

```
tar -xzf ZenQuery-standalone.tar.gz && cd ZenQuery-standalone
./bin/startup.sh
```

(under Windows use startup.bat instead)

After that ZenQuery is available under [http://localhost:8080/](http://localhost:8080/).

The server can be stopped with the following command:

`./bin/shutdown.sh`

(under Windows use shutdown.bat instead)

The stand-alone version uses a pre-configured embedded H2 database for storing application data. This database is
located under src/main/resources/sql (files: ZenQuery.mv.db and ZenQuery.trace.db) in the ZenQuery directory.

For testing purposes this is perfectly fine. For production purposes, however, you should use your own, dedicated
database. See the section on [configuration](#configuration) below for further information.

#### <a name="war"/>WAR

ZenQuery is available as a WAR release for usage in your favourite Servlet container / application server under
[http://fullmontymedia.com/ZenQuery/downloads/ZenQuery.war](http://fullmontymedia.com/ZenQuery/downloads/ZenQuery.war)

If you need a version that runs under JDK 1.6 (Java EE 6) please download ZenQuery from
[http://fullmontymedia.com/ZenQuery/downloads/ZenQueryJDK16.war](http://fullmontymedia.com/ZenQuery/downloads/ZenQueryJDK16.war)

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
in the ZenQuery directory:

* H2
* MySQL
* Oracle Database
* PostgreSQL

Then you need to set the system property DATABASE_URL according to your database connection settings.

How you set this system property depends on your environment. A common way for doing so is using the -D command line
option for Java, e.g.:

`java -DDATABASE_URL=postgres://username:password@localhost:5432/ZenQuery ...`

The format for the URL is

`PROTOCOL://USERNAME:PASSWORD@HOST:PORT/DATABASE_NAME`

A PostgreSQL DATABASE_URL for example would look like this:

`DATABASE_URL=postgres://username:password@localhost:5432/ZenQuery`

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

**Please note: Only H2, MySQL and PostgreSQL drivers are included with ZenQuery. If you want to use one of the other
RDBMS you have to add the appropriate JDBC driver to the shared libraries directory of your Servlet container /
application server.**

#### Security

In order to set your basic security preferences for ZenQuery the environment variable *spring.profiles.active* has to be
set to either 'local' or 'public', i.e.:

`spring.profiles.active=local`

or

`spring.profiles.active=public`

'local' will disable HTTP basic authentication whereas 'public' will enable it. The default password is 'pP2XLieKb6'.
This password can (and should be) changed in spring-security-public.xml in src/main/webapp in the ZenQuery directory.
Please restart the server after having changed the password. Please note: If you turn HTTP basic authentication on your
API calls will have to send an HTTP basic authentication header with the appropriate username / password, too.

How you set the *spring.profiles.active* system property depends on your environment. A common way for doing so is using
the -D command line option for Java, e.g.:

`java -Dspring.profiles.active=local ...`

## <a name="usage"/>Using ZenQuery

Using ZenQuery is simple. ZenQuery has 2 main views:

* [database connections](#database-connections)
* [queries](#queries)

### <a name="database-connections"/>Database Connections

Here you can add and edit and your database connections. A valid database connection consists of:

* name
* URL
* username
* password

The URL has to be a valid JDBC connection URL such as:

`jdbc:postgresql://localhost:5432/SomeDatabase`

The following JDBC connection protocols / RDBMS are supported:

* jdbc:as400:// (IBM DB2 AS400)
* jdbc:db2:// (IBM DB2)
* jdbc:derby: (Apache Derby)
* jdbc:ingres:// (Ingres Database)
* jdbc:firebirdsql:// (Firebird)
* jdbc:h2: (H2)
* jdbc:hsqldb:mem: (HSQLDB)
* jdbc:JTurbo:// (Microsoft SQL Server, JTurbo driver)
* jdbc:mysql:// (MySQL)
* jdbc:oracle:thin:@ (Oracle Database)
* jdbc:postgresql:// (PostgreSQL)
* jdbc:sapdb:// (SAP DB)
* jdbc:microsoft:sqlserver (Microsoft SQL Server)
* jdbc:sybase:Tds: (Sybase)

**Please note: Only H2, MySQL and PostgreSQL drivers are included with ZenQuery. If you want to use one of the other
RDBMS you have to add the appropriate JDBC driver to the shared libraries directory of your Servlet container /
application server.**

After having entered a valid database connection ZenQuery will automatically create *SELECT * FROM* queries for each
table and view in your database. If your database uses foreign keys ZenQuery will extend those queries to include
[links to referenced database entity resources](#foreign-keys).

### <a name="queries"/>Queries

Clicking on the 'Queries' button from the top menu will show all queries. Clicking the respective button for each
database connection will show only the queries for that connection.

Clicking on 'Details' for a query will preview the result set for this query as well as reveal a few additional options:

* Execute
* Update
* New
* Previous versions

Alongside these options ZenQuery displays [REST API](#rest-api) links for this query above and below the result set
preview.

#### <a name="interpolation"/>Arguments and variable interpolation

ZenQuery allows you to use the *?* operator for dynamically supplying one or multiple arguments to a query, e.g.:

```
SELECT * FROM table WHERE field = ?
SELECT * FROM table WHERE field = ? OR another_field = ?
```

These arguments can then be supplied as [additional URL parameters](#formats) to your API calls.

#### <a name="foreign-keys"/>Navigating entities referenced by foreign keys

If your database tables make use of foreign keys for referencing entities ZenQuery will automatically link those to the
referencing entity and add a link to the API resource for the referenced entity.

### <a name="rest-api"/>REST API

ZenQuery turns each SQL query into an easily accessible REST API endpoint that returns data in
[a variety of formats](#formats).

#### <a name="formats"/>Parameters and formats

The ZenQuery REST API returns data in the following formats:

* JSON
* XML
* HTML
* CSV

These are a few example URLs:

* [/api/v1/resultSetForQuery/6.json](/api/v1/resultSetForQuery/6.json) (JSON)
* [/api/v1/resultSetForQuery/6.xml](/api/v1/resultSetForQuery/6.xml) (XML)
* [/api/v1/resultSetForQuery/6.csv](/api/v1/resultSetForQuery/6.csv) (CSV)
* [/api/v1/resultSetForQuery/vertical/false/6.html](/api/v1/resultSetForQuery/vertical/false/6.html) (HTML list)
* [/api/v1/resultSetForQuery/vertical/true/6.html](/api/v1/resultSetForQuery/vertical/true/6.html) (styled HTML list)
* [/api/v1/resultSetForQuery/horizontal/false/6.html](/api/v1/resultSetForQuery/horizontal/false/6.html) (HTML table)
* [/api/v1/resultSetForQuery/horizontal/true/6.html](/api/v1/resultSetForQuery/horizontal/true/6.html)
(styled HTML table)

ZenQuery also allows you to add arguments to an API call, which will be used for
[interpolating variables in the SQL query](#interpolation). The arguments are appended to the URL after the query ID.
Multiple arguments are comma-separated. Moreover, you can also limit the size of the result set by adding a *size*
parameter.

Again, these are a few example URLs:

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
* Supports all major RDBMS (including IBM DB2, Microsoft SQL Server, MySQL, Oracle Database and PostgreSQL).
* Entirely hosted on-premises. Your data stays with you all the time!
* Conveniently edit your SQL queries and preview your data.
* Version control for SQL queries.
* Snapshots (i.e. materialized views if supported by RDBMS).
* Transitive navigation (i.e. navigating entities referenced by foreign keys).
* Variable interpolation.
* Limit and filter query results.

## <a name="support"/>Support

If you have any questions, suggestions, problems or feature requests please contact us under
[zenquery-support@fullmontymedia.com](mailto:zenquery-support@fullmontymedia.com)

Please also visit [our website](http://www.zenqry.com/) for further information.

## <a name="license"/>Version information

Current version: 1.0

The current free version has a valid license until 15 August 2014.
