# ChromCloud

A cloud to manage multiply minecraft servers with multiroot feature.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Requirements

 * Java 8
 * Linux/Windows server with a minimum of 2GB DDR3 Memory and 2 vCores
 
 **The use of Linux containers (LXC) or OpenVZ containers (OVZ) is discouraged. There are many issues with their stability.**  
Use of KVM virtualization or dedicated servers is recommended.

### Prerequisites

What things you need to install the software and how to install them

```
Docker
Java
```

### Installing

First clone the respository

Then run the maven build command.

```
mvn build
```

Copy the ChromNode.jar and the ChromSubnode.jar from the export folders.

Start both jar files. And follow the install instructions.

## Deployment

For live system use the system with the api token system.

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management

## Contributing

Please read [CONTRIBUTING.md](https://github.com/rexlManu/ChromCloud/blob/master/CONTRIBUTING.md) for details on our code of conduct, and the process for submitting pull requests to us.

## Versioning

For the versions available, see the [tags on this repository](https://github.com/rexlManu/ChromCloud/tags). 

## Authors

* **Emmanuel L.** - *Initial work* - [rexlManu](https://github.com/rexlManu)

See also the list of [contributors](https://github.com/rexlManu/ChromCloud/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE.md](https://github.com/rexlManu/ChromCloud/blob/master/LICENSE) file for details

