# HarkerRoboLib
Harker Robotics (1072) presents HarkerRoboLib, a useful library to complement and enhance the features of WPILib and Phoenix.

## Setup
### Gradle
For a Gradle project, download the most recent .jar file from the "Releases" tab and place it somewhere in your project folder (ideally not in `src`; a folder called `lib`, for example, would work well). 

Then in your `build.gradle` file, find the `dependencies` section and add the following line to it:
```
compile files('lib/HarkerRoboLib-v2.0.jar') 
```
Refresh and build your project and the library should be ready to access.
