This Android app uses the Android Studio IDE for project management and
compilation of the Java sources and the NDK toolchain for management and
compilation of the C/C++ sources. To open and run the app, please follow these
instructions:

1. Install the Android Studio IDE (2.0+) and the Android SDK:
https://developer.android.com/sdk

2. Install the Android NDK through Android Studio:
- Open Tools > Android > SDK Manager
- Go to tab SDK Tools
- Select and install Android NDK

3. Open the Android Studio IDE.

4. Click "Open an existing Android Studio project" and select this folder.

5. After the project finishes loading, open the terminal on this folder and:
- Enter the folder app/src/main
- Run 'ndk-build'.
    - Note: the ndk-bundle folder (where Android Studio installed the NDK) needs
        to be added to the PATH in order to enable the 'ndk-build' command.

6. Open the "Build" menu on Android Studio and select "Make Project".

7. After the Gradle build finishes, open the "Run" menu and select "Run 'app'".

8. Select a running mobile device that is connected to the computer through
USB debugging (http://developer.android.com/tools/device.html) or select an
emulator and press OK.

9. The app will be installed and automatically opened on the target device.

