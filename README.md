# MultiPickerWrapper
Wrapper around [android-multipicker-library](https://github.com/coomar2841/android-multipicker-library) ([com.kbeanie:multipicker:1.1.1@aar](https://mvnrepository.com/artifact/com.kbeanie/multipicker/1.1.1))

Integrated with Runtime Permission library [PermisoWrapper](https://github.com/NoelChew/PermisoWrapper) ([com.github.noelchew:PermisoWrapper:0.1.1](https://github.com/NoelChew/PermisoWrapper/releases/tag/0.1.1))

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
    compile 'com.github.noelchew:MaterialDialogWrapper:0.1.3'
}
```
Note: do not add the jitpack.io repository under buildscript
