# Temple Galaxia

![Screenshot](/assets/GalaxiaScreenshot.png "Example of Galaxia in LX.")

## To Run
`./gradlew run`

### Requirements
Java 8, if you don't already have it on your system:

- Debian/Ubuntu
  ```
  sudo apt-get install openjdk-8-jre
  ```
- Fedora/RHEL/Oracle
  ```
  su -c "yum install java-1.8.0-openjdk"
  ```
- OSX
  ```
  brew cask install caskroom/versions/java8
  ```
- Windows
  - Let us know!

## IntelliJ Setup
This is a gradle project so intellij should automagically just Work with a few clicks.

Note: tested with intellij 2018.1.2

- Checkout repo
- File > New > Project From Existing Sources
- Select Galaxia directory
- Hit "Import project from existing model" > "Gradle" and use the defaults
- Finish
- Intellij will then run gradle pulling in dependencies and building the project
- Create the run configuration
  - View > Tool Windows > Gradle
  - Open up Galaxia > Tasks > application
  - Double click on "run"
  - P3LX window should appear
  - You can now hit the run to start the project up

### Special thanks to
- [Mark Slee](https://github.com/mcslee): The creator of [LX Suite of tools](https://github.com/heronarts/LXStudio)
- [Matty Goodman](https://github.com/meawoppl): For all the software
- [Bash Zaidy](https://github.com/bash102): Circuit designs that make this all possible
- Robb Walters: PCBa layouts while we sleep!
