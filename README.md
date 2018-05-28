# Temple Galaxia

![Screenshot](/assets/GalaxiaScreenshot.png "Example of Galaxia in LX.")

## To Run
`./gradlew run`

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

### Special thanks
Mark Slee (@mcslee) the creator of https://github.com/heronarts/LXStudio
