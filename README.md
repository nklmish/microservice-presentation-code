# microservice-demo

#Instructions for Non-Docker users 

The sample application consists of microservices backed by spring and netflix components to build a simple catalog service. 

The catalog service communicates with :

1. Product-service : Simulates generating product. 
2. Price-service : Simulates price calculation for a given product.
3. Comment-service : Simulates comments generation for a given product.

Apart from these there are:

1. Eureka-service : Acts as a service registry.
2. Zuul-service : Acts as a proxy.
3. Hystrix-dashboard : Allows to monitor hystrix metrics in real time.

# Dependecies
Jdk-8

#Launching 
In order to launch all microservices you can execute
```
./run-all.sh
```

You can visit http://localhost:8761/ to monitor all services registered with eureka.

#Sample request

Note, I am using [jq](http://stedolan.github.io/jq/) library for filtering json data. If you prefer some different library
then you need to re-adjust all commands pasted below.

#Determing ports, ip address, hostname and service name
Execute 
```
curl -s -H "Accept: application/json" http://localhost:8761/eureka/apps | jq '.applications.application[] | {port:.instance.port["$"], ipAddress:.instance.ipAddr,hostName: .instance.hostName, serviceName:.name}'
```

**Replace the port under which zuul-service is running on your computer** and  execute ```curl```. For e.g. on my machine
zuul-service is running on port 44953, so we can execute
```
curl -s localhost:44953/catalog-service/catalog/100 | jq .
```

You will get response like this: 

```json
{
  "product": {
    "price": {
      "amount": 1000.2
    },
    "comments": [
      {
        "content": "I like this product very much!!!",
        "author": "John",
        "productId": 100,
        "id": 1
      },
      {
        "content": "A must buy, worth the price",
        "author": "Bob",
        "productId": 100,
        "id": 2
      }
    ],
    "description": "super cool product",
    "name": "Product-A",
    "id": 100
  },
  "id": 100
}
```

Now If you will take the comment-service down, and execute 
```
curl -s localhost:44953/catalog-service/catalog/100 | jq .
```

You will notice that only comments will be missing from the response and still your application will work. E.g.

```json
{
  "product": {
    "price": {
      "amount": 1000.2
    },
    "comments": [],
    "description": "super cool product",
    "name": "Product-A",
    "id": 100
  },
  "id": 100
}
```

#Hystrix dashboard
Please visit `http://localhost:7979/` and enter `http://localhost:REPLACE_WITH_PORT_ON_WHICH_CATALOG_SERVICE_IS_RUNNING/hystrix.stream`

Issue some requests to see the metrics data for e.g.
```
curl -s localhost:44953/catalog-service/catalog/100 | jq .
```

and remember to replace `44953` with your port on which zuul service is running

#Instructions for Docker users

#Dependecies
Docker and docker-compose, refer to https://docs.docker.com for installation

#For Mac users 
If you are on mac then please replace localhost with IP address allocated to your docker machine.
For e.g. If you are using `docker toolbox` then you can execute `docker-machine ip <machine-name>` and you will get something like `192.168.59.103`

#gradle.properties
Please create/update your's ```gradle.properties``` file to include docker's IP address. E.g. <br/>
For Mac Users: 
```
dockerServerUrl=https://<REPLACE_WITH_DOCKER_MACHINE_IP_ADDRESS>:2376/
```
For Linux Users: 
```
dockerServerUrl=unix:///var/run/docker.sock
```

#Launching 
Setup 
Build and deploy all projects by executing
```
./run-all-docker-on-docker.sh
```

Once all services are up and running , You can visit http://localhost:8761/ to monitor all services registered with eureka.

#Sample request

```
curl -s localhost:8765/catalog-service/catalog/100
```

You will get response like this: 
```json
{
  "product": {
    "price": {
      "amount": 1000.2
    },
    "comments": [
      {
        "content": "I like this product very much!!!",
        "author": "John",
        "productId": 100,
        "id": 1
      },
      {
        "content": "A must buy, worth the price",
        "author": "Bob",
        "productId": 100,
        "id": 2
      }
    ],
    "description": "super cool product",
    "name": "Product-A",
    "id": 100
  },
  "id": 100
}
```
Now If you will take the comment-service down, and execute 
```
curl -s localhost:8765/catalog-service/catalog/100
```
You will notice that only comments will be missing from the response and still your application will work. E.g.

```json
{
  "product": {
    "price": {
      "amount": 1000.2
    },
    "comments": [],
    "description": "super cool product",
    "name": "Product-A",
    "id": 100
  },
  "id": 100
}
```

#Hystrix dashboard
Please visit `http://localhost:7979/` and enter `http://localhost:8989/turbine/turbine.stream`

Issue some requests to see the metrics data for e.g.
```
curl -s localhost:8765/catalog-service/catalog/100 | jq .
```

#Scaling services
docker-compose scale serviceName=num, where serviceName refers to name of existence service declared in docker-compose.yml 
e.g. ```docker-compose scale commentService=2```

#Shutdown
execute ```./stop-all-on-docker.sh```
