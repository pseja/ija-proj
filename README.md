# ija-proj
Skupinový projekt do předmětu IJA

## Setup
### Requirements
1. Clone this repository
```sh
git clone git@github.com:pseja/ija-proj.git
```

2. Download Maven
```sh
sudo apt install maven
```

3. Run this project
```sh
mvn javafx:run
```

### Development
For extra debug info:
```sh
mvn -e -X javafx:run
```

For easy .fxml UI styling download [Scene Builder](https://gluonhq.com/products/scene-builder/#download)
- On Ubuntu:
  - `sudo dpkg -i SceneBuilder-23.0.1.deb`
  - if there are any errors here are some of the comments I used to fix it:
    1. `sudo dpkg --configure -a`
    2. `sudo nano /var/lib/dpkg/info/scenebuilder.postinst`
    3. Comment out the case like this
```bash
# case "$1" in
#     configure)
#  xdg-desktop-menu install /opt/scenebuilder/lib/scenebuilder-SceneBuilder.desktop
# 
#     ;;
# 
#     abort-upgrade|abort-remove|abort-deconfigure)
#     ;;
# 
#     *)
#         echo "postinst called with unknown argument \`$1'" >&2
#         exit 1
#     ;;
# esac
```
    4. `sudo dpkg --configure -a`

## Implementation details
### Design patterns
- [MVC](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93controller)
