

Sprint 1 - your first nonblocking Endpoint
========
- Have a look at `WikiController#fetchArticle`. 
  This endpoint signature is currently like an traditional Spring MVC endpoint.
  Change the signature to the 'reactive-way' and implement this service.
  Hint: use `ArticleService#fetchArticle` to fetch the article from Wikipedia.
- Ensure Unittests (`WikiControllerTest`) and integration tests (`WikiControllerIntegrationTest`) are still running
- Start the spring boot application (see `services/README.md`) and try out your new endpoint and fetch an article, 
  e.g with curl, an browser or an RESTClient <http://localhost:8081/article/...> 

Sprint 2 - stream articles to the frontend
========
- Have a look at `WikiController#getReadStream`.
  This endpoint signature is currently like an traditional Spring MVC endpoint.
  Change the signature to the 'reactive-way' and implement this service.
  Hint: You have to use a Processor, which acts as a subscriber and as a publisher. 
        In `WikiController` there is already a Processor named `readArticles`, use this one.
- Activate the Unittests in `ReadEventTest` and `WikiControllerIntegrationTest` and look if they run successfully.
- Start the sprig boot application and also the ms-statistics service which contains a frontend (see `services/README.md`)
  and try out your endpoints

Sprint 3
========
- Have a look at `WikiController#countWords`.
  This Implementation is not too bad, but is's blocking. Change it to non-blocking.
- Ensure Unittests (`WikiControllerTest`) and are still running successfully 
  and active integration tests (`WikiControllerIntegrationTest`)

Sprint 4
========
?


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
