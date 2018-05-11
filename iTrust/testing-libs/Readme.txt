The /testing-libs directory is used for all of the jars needed to run automated tests.

Any jars needed in production should be in the WebRoot/WEB-INF/lib folder.

Also, the MySQL driver needs to be in Tomcat's shared library (see deployment instructions).

Please don't mix the jars in these folders. 