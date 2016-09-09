# bento-demo

##  Requirements
Before you start, make sure java 8 and maven is installed on your machine.

##  To Run
1.  git clone https://github.com/vaskalas/bento-demo.git
2.  From the command line go to the root and run:
`mvn clean package && java -jar target/path-to-philosophy-0.0.1-SNAPSHOT.jar`
3.  Open a brower and go to [http://localhost:8888](http://localhost:8888)

##  Database Access
1.  With the application running go to [http://localhost:8888/h2-console/](http://localhost:8888/h2-console/)
2.  Make sure Driver Class is `org.h2.Driver`
3.  Enter `jdbc:h2:mem:AZ;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE` in the JDBC Url 
4.  Once you're in, you can view the saved steps in the STEPS table
