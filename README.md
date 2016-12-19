REST API using Jersey, Hibernate, MySQL and Jetty

Mysql Database setup

1. create user name root with password mysql.
2. create a database with name cis.
3. create a table name stock with the command below
```$xslt
CREATE TABLE `stock` (   
`STOCK_ID` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,   
`STOCK_CODE` VARCHAR(10) NOT NULL,   
`STOCK_NAME` VARCHAR(20) NOT NULL,   
PRIMARY KEY (`STOCK_ID`) USING BTREE,   
UNIQUE KEY `UNI_STOCK_NAME` (`STOCK_NAME`),   
UNIQUE KEY `UNI_STOCK_ID` (`STOCK_CODE`) USING BTREE 
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```
4. insert some values into the database.

Steps to run the project

1. cd hibernate-mysql
2. mvn clean
3. mvn install
4. run src/main/java/com/mkyong/common/app.java
5. login http://localhost:8080/api/read