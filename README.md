# HelloWorld MicroService

This is a sample repository for a Hello World service with additional health and readyness check so it can be probed
from a Kubernetes cluster.

## Building

You will need [SBT](https://www.scala-sbt.org) installed in your computer to be able to build the source code.

Once you have that, clone this repository and build the docker image:

```bash
sbt docker:publishLocal
```

That will create a Docker image in your local computer under the `alonsodomin/bankifi-helloworld-ms` name.

## Using the service

The service is extremely simple, once the container is started it will bind itself to the 8080 port. There are only 3
routes available from the service, and these are the following ones:

### GET /hello/myname

This will reply wit a `Hello myname`

### GET /health

This is a health check, it will reply with `OK` or with a random error message.

### GET /ready

This is the readiness check, it will reply with `OK` or `KO` in a random pattern.
