Reactive Microservices
======================

### start application
- wikiloader (running on port 8081):
<br> `mvn spring-boot:run`
<br> or `mvn spring-boot:run -Drun.profiles=mock` to run the Service in Mock mode, 
     which loads wiki articles from local files
- statistics (running on port 8080):
<br> `mvn spring-boot:run`

### jmeter (load/performance tests)
- have a look at ./jmeter/README.md
<br> `./jmeter$ mvn exec:exec`