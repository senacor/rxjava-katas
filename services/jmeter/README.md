### jmeter (load/performance tests)
- start jmeter:
<br>
run project included jmeter with maven comman:
    `mvn exec:exec`
<br>    
run external jmeter with maven command:
    `mvn exec:exec -DjmeterDir=/path/to/apache-jmeter`            
- open a testplan (./testplan/*.jmx)
- execute the testplan with the green 'play' button