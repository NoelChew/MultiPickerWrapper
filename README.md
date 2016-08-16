[![Release](https://jitpack.io/v/noelchew/MultiPickerWrapper.svg)](https://jitpack.io/#noelchew/MultiPickerWrapper)
# MultiPickerWrapper
Wrapper around [android-multipicker-library](https://github.com/coomar2841/android-multipicker-library) ([com.kbeanie:multipicker:1.1.1@aar](https://mvnrepository.com/artifact/com.kbeanie/multipicker/1.1.1))

Integrated with Runtime Permission library [PermisoWrapper](https://github.com/NoelChew/PermisoWrapper)

PermisoWrapper provides convenient methods to handle permission request. It is a wrapper around the fantastic [Permiso](https://github.com/greysonp/permiso) library.


## Integration
This library is hosted by jitpack.io.

Root level gradle:
```
allprojects {
 repositories {
    jcenter()
    maven { url "https://jitpack.io" }
 }
}
```

Application level gradle:
```
dependencies {
    compile 'com.github.noelchew:MultiPickerWrapper:0.1.0'
}
```
Note: do not add the jitpack.io repository under buildscript
