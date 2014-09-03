cd %CD%\lib\Twitter4JCrawler\twitter4j-async
mvn install
cd %CD%\lib\Twitter4JCrawler\twitter4j-core
mvn install & cd %CD%\lib\Twitter4JCrawler\twitter4j-stream -DskipTests=true
mvn install
mvn install:install-file -Dfile=%CD%\lib\jfxrt.jar -DgroupId=com.oracle -DartifactId=javafx -Dversion=2 -Dpackaging=jar"