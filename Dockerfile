FROM clojure:lein-2.5.1
MAINTAINER Scott Bauer

LABEL Description="This image is used to start the link-service executable."

# To avoid pulling dependencies everytime, we create the directory below, run lein deps,
# and then use those dependencies until something changes.
RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app
COPY project.clj /usr/src/app/
RUN lein deps
COPY . /usr/src/app

# Generate the uberjar and rename it.
RUN mv "$(lein uberjar | sed -n 's/^Created \(.*standalone\.jar\)/\1/p')" app-standalone.jar

EXPOSE 8090

# The tokens property below should be changed and adjusted to your needs.
CMD ["java", "-Dip=0.0.0.0", "-Dport=8090", "-Dtokens=a1f4e6ea,fpk7hxs2", "-jar", "app-standalone.jar"]
