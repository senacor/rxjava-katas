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
- Start the spring boot application (see `services/README.md`) and try out your new Endpoint, 
  e.g with curl, a Browser or an RESTClient <http://localhost:8091/article/...> 

Sprint 2
========


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