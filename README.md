# VersionHelper ![1]![2]![3]

VersionHelper is a version update library for easier use when app updates

## Installation

VersionHelper is installed by adding the following dependency to your `build.gradle` file:

```groovy
dependencies {

    implementation 'com.bjnsc.versionlibrary:versionhelp:1.0.0'
    
}
```

## Usage

### Need Permissions
```groovy
    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
```
Need to handle STORAGE read and write permissions yourself

### Basic

To begin using VersionHelper

Configure appId in your Manifest.xml file
```groovy
<meta-data
    android:name="com.bjnsc.versionhelper"
    android:value="c6fa86f0-4ee7-11e9-8e7e-9326967da7de"/>
```

```java
VersionHelper helper = VersionHelper.init(this);
helper.updateApp(new VersionHelper.CheckAppVersionListener() {
            @Override
            public void checkAppVersionSuccess(int successCode) {
                // TODO: do something
            }

            @Override
            public void checkAppVersionFaild(int faildCode) {
                // TODO: do something
            }
        });
```


## LICENSE

```
	Copyright 2019 NSC

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

```

[1]: https://img.shields.io/badge/build-passing-green.svg
[2]: https://img.shields.io/badge/VersionHelper-1.0.0-yellowgreen.svg
[3]: https://img.shields.io/badge/API-19%2B-orange.svg

