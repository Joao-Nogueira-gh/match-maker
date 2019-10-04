FROM ubuntu:16.04
RUN apt-get install git && \
git clone https://github.com/Joao-Nogueira-gh/match-maker.git && \
<additional bash commands for building your project>
CMD <command to run your project> # Can also use ENTRYPOINT in certain cases
