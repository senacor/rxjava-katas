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


Sprint 3
========


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
- After some time (e.g. 1 minute) you want to expire the data. 
  Implement a reacive timed job which drops outdated data.
  
Sprint X.2 - visualizes backpressure buffers
========  
- add a diagram in the frontend which visualizes backpressure buffers in the streams.
  add a new statistics endpoint which collects and processes this data and delivers it to the frontend
  
