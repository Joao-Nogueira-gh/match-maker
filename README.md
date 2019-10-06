# MatchMaker

* Documentation and optimization of the MatchMaker(v1.0) app

* To build: ./build.sh (already built when pulled)

* To run: ./run.sh (runs main app, appConjunta)

* There are also some tests which you can run manually if you want to.

* To run with docker: https://hub.docker.com/r/joaonog/match-maker

```
docker pull joaonog/match-maker

docker run -i -t joaonog/match-maker   
```

Without adding '-i -t' docker will not wait for scanner input.

* **Our MatchMaker app, when initialized, asks the user for input of the threshold value, this is the similarity threshold the program will consider in order to make matches between users (lower value->less matches found)**
* **The suggested value for the threshold is 0.3**
