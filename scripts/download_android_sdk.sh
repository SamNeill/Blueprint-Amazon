#!/bin/bash

# Accept all SDK licenses
yes | $ANDROID_HOME/cmdline-tools/latest/bin/sdkmanager --licenses

# Download required SDK components
echo "Downloading Android SDK components..."
$ANDROID_HOME/cmdline-tools/latest/bin/sdkmanager \
    "platform-tools" \
    "platforms;android-33" \
    "build-tools;33.0.0" \
    "extras;android;m2repository" \
    "extras;google;m2repository"
