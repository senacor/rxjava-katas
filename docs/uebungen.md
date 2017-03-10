1. article fetch
  - as
- read event + frontend
  - frontend read geht schon
- stats consumer
- stats endpoint
- rating wordcount endpoints
- optimieren mit batch
- backpressure handeln

Sprint 1 - your first nonblocking Endpoint
========
- Have a look at `WikiController#fetchArticle`. 
  This endpoint signature is currently like an traditional Spring MVC endpoint.
  Change the signature to the 'reactive-way' and implement this service.
  Hint: use `ArticleService#fetchArticle` to fetch the article from Wikipedia.
- Write Unittests (`WikiControllerTest`) and integration tests (`WikiControllerIntegrationTest`)
- Start the spring boot application (see `services/README.md`) and try out your new endpoint and fetch an article, 
  e.g with curl, an browser or an RESTClient <http://localhost:8081/article/...> 

Sprint 2 - stream articles to the frontend
========
- Have a look at `WikiController#getReadStream`.
  This endpoint signature is currently like an traditional Spring MVC endpoint.
  Change the signature to the 'reactive-way' and implement this service.
  For each call to `WikiController#fetchArticle` there should be emitted an Event on
  this `#getReadStream`.
  Hint: you need a `org.reactivestreams.Processor`, have a look what reactor offers.
- Write Unittests (`ReadEventTest`) and integration tests (`WikiControllerIntegrationTest`)
- start ms-wikiloader and ms-statistics. ms-statistics contains a basic frontend, which consumes 
  the `#getReadStream` endpoint and draws a diagram. The URL is printed out in the console after 
  startup of the service.
- execute some calls to the `#fetchArticle` endpoint and watch the diagram.
- Start jmeter (see `services/README.md`), open an provided testplan (*.jmx) and give some load on `#fetchArticle`.
  At this point it is better to start the wikiloader service in mock mode 
  by activating the profile `mock` (see `services/README.md`). This should be the default
  from now on.
  
Sprint 3 - getReadStream optimization
========
- Maybe you noticed some problems when jmeter put heavy load on `#fetchArticle`
  When you publish every single 'articleRead'-Event on it's own, spring will flush 
  the HTTP connection to the frontend for each event, this produces a lot of overhead.
  Now try to reduce the overhead and deliver the Events in batches.
  Hint: A good batch size could be 250 milliseconds -> 4 flushes per second.
- Write tests
- Test it with jmeter and the frontend

Sprint 4
========


Sprint 5
========


Sprint 6
========


Sprint 7
========


Sprint X.1 - improved caching (reactive datastore)
========
- have a look at `PublisherCache`, this is a quite simple and small LRU cache
  to cache the fetched content from wikipedia. You can extend this cache by an 
  in-memory Mongo/Casandra/Redis Database:
  look in the LRU Cache, if not found, look in the Mongo/Casandra Database, 
  if not found, query wikipedia.
  Use Spring Data to query the Database <https://spring.io/blog/2016/11/28/going-reactive-with-spring-data>

