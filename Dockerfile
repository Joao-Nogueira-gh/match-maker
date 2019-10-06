FROM openjdk:8-alpine
COPY . .
RUN apk update && apk add bash && chmod u+x run.sh
ENTRYPOINT ["./run.sh"]
