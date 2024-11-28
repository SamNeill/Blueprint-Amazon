# Blueprint-Amazon

A customized version of Blueprint dashboard optimized for Amazon Appstore, based on the original [Blueprint](https://github.com/jahirfiquitiva/Blueprint) project by [Jahir Fiquitiva](https://github.com/jahirfiquitiva).

## Features

- Amazon Appstore Integration
- In-App Billing Support
- Material Design UI
- Kotlin-first Architecture
- Multi-module Project Structure
- Cloud-based wallpapers
- In-app icon request tool
- Multiple launcher support
- Offline functionality
- Theme options (Light, Dark, System, AMOLED)
- Tablet layout support

## Project Setup

### Prerequisites

- Android Studio Arctic Fox or newer
- JDK 17
- Android SDK with minimum API level as specified in build.gradle
- Amazon Appstore Developer Account (for publishing)

### Building the Project

1. Clone the repository:
```bash
git clone [repository-url]
cd Blueprint-Amazon
```

2. Open the project in Android Studio

3. Sync Gradle files and install dependencies

4. Build variants:
   - Debug: For development and testing
   - Release: For production deployment

### Configuration

#### Amazon Appstore Setup

The project includes Amazon Appstore billing compatibility (version 4.2.0) for in-app purchases:

```gradle
dependencies {
    implementation files('libs/appstore-billing-compatibility-4.2.0.jar')
}
```

## Project Structure

- `/app` - Main application module
- `/library` - Core library module
- `/buildSrc` - Build logic and dependency management
- `/gradle` - Gradle wrapper and configuration

## Development Notes

### Build Configuration

- Target SDK: Latest stable Android SDK
- Minimum SDK: As specified in build.gradle
- Kotlin Version: Latest stable
- Build Tools: Gradle with Kotlin DSL

### Code Style

- Kotlin-first approach
- Material Design guidelines
- MVVM architecture pattern

## Testing

Run tests using:
```bash
./gradlew test
```

For instrumented tests:
```bash
./gradlew connectedAndroidTest
```

## Publishing

1. Configure your Amazon Appstore developer account
2. Update the signing configuration
3. Build release variant
4. Submit to Amazon Appstore

## Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project contains two licenses:

### Original Blueprint License

This app is shared under the CreativeCommons Attribution-ShareAlike license.

    Copyright 2020 Jahir Fiquitiva

    Licensed under the CreativeCommons Attribution-ShareAlike 
    4.0 International License. You may not use this file except in compliance 
    with the License. You may obtain a copy of the License at

       http://creativecommons.org/licenses/by-sa/4.0/legalcode

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

### Blueprint-Amazon License

MIT License

Copyright (c) 2024 Blueprint-Amazon Team

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

## Acknowledgments

- [Original Blueprint Project](https://github.com/jahirfiquitiva/Blueprint) by Jahir Fiquitiva
- Amazon Appstore Development Team
- Material Design Components
- Android Jetpack Libraries

## Amazon In-App Purchase Setup

To enable Amazon IAP:

1. Create `app/src/main/assets` directory
2. Download AppstoreAuthenticationKey.pem from Amazon Developer Console:
   - Go to your app in Amazon Developer Console
   - Navigate to App > In-App Items
   - Click "Download Public Key"
3. Place AppstoreAuthenticationKey.pem in `app/src/main/assets/`

Note: The authentication key is not included in the repository for security reasons.