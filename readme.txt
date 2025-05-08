# Light bulb game

Author: Lukáš Pšeja (xpsejal00)

Author: Václav Sovák (xsovakv00)

## Setup
### Requirements
This project uses Maven and JavaFX, these can be downloaded on Ubuntu
```sh
sudo apt install maven openjfx
```

1. Build the project, create documentation and .jar file
```sh
mvn clean compile javadoc:javadoc package
```

**Note:** The project documentation includes UML diagrams generated using the UMLDoclet Maven plugin.

2. Run this project with Maven
```sh
mvn javafx:run
```

### Development
The following tools were used for development:
- `-e -X` flags for extra debug information
```sh
mvn -e -X javafx:run
```

- [Scene Builder](https://gluonhq.com/products/scene-builder) to design all of the menus easily without manually writing the FXML files.
- [JavaFX documentation](https://www.oracle.com/java/technologies/javase/javafx-docs.html) and [Getting Started with JavaFX](https://openjfx.io/openjfx-docs/#introduction).

- Main menu title image generated with [textcraft](https://textcraft.net/).
- Assets for the game inspired by [Minecraft](https://www.minecraft.net/en-us).

