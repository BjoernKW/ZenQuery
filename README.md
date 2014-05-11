ZenQuery
========

## Table of Contents

### [Introduction - Enterprise Backend as a Service](#introduction)
### [Requirements](#requirements)
### [Getting started](#getting-started)
### [Using ZenQuery](#usage)
### [Features](#features)
### [Support](#support)
### [License and version information](#license)

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

* using our [stand-alone Apache Tomcat server release](#stand-alone)
* using our [WAR (web application archive) release](#war) with your own Servlet container or application server

#### <a name="stand-alone"/>Stand-alone Apache Tomcat server

#### <a name="war"/>WAR

## <a name="usage"/>Using ZenQuery

### Database Connections

### Queries

#### Transitive Navigation

Primary and foreign keys

### API

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


## <a name="license"/>License and version information

Commercial / premium versions of ZenQuery will contain the ZenQuery source code (licensed under
[Affero GPL](http://www.gnu.org/licenses/agpl-3.0.html)). The free version is closed source for now.

### Version information

Current version: 1.0
