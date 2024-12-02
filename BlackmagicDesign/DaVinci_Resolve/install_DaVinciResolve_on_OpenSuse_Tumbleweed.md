# Installing Blackmagic Design DaVinci Resolve Studio on OpenSuse Tumbleweed

Last tested in December 2024 with DaVinci Resolve v19.1.1.

* Download the Linux install zip file from <https://www.blackmagicdesign.com/support/family/davinci-resolve-and-fusion>

* Extract the contents of the zip file you downloaded

```console
$ unzip DaVinci_Resolve_Studio_19.1.1_Linux.zip
```

* Install the package, ignoring the whether all of the prerequisite packages are installed

```console
$ cd DaVinci_Resolve_Studio_19.1.1_Linux
$ SKIP_PACKAGE_CHECK=1 ./DaVinci_Resolve_Studio_19.1.1_Linux.run
```

* Download fedora’s libgdk rpm (see: <https://forums.opensuse.org/t/davinci-resolve-18-5-not-working-on-opensuse-tumbleweed/167175/14>)

```console
$ curl -O https://dl.fedoraproject.org/pub/fedora/linux/releases/38/Everything/x86_64/os/Packages/g/gdk-pixbuf2-2.42.10-2.fc38.x86_64.rpm
```

* Extract the contents of the rpm file you downloaded

```console
$ rpm2cpio ./gdk-pixbuf2-2.42.10-2.fc38.x86_64.rpm | cpio -idmv
```

* Copy the library files into DaVinci Resolve's library folder

```console
$ cd usr/lib64/
$ sudo cp -vr * /opt/resolve/libs/
```

Make a symbolic link to the system's OpenCL library in DaVinci Resolve's library folder

```console
$ sudo ln -s /usr/lib64/ocl-icd/libOpenCL.so.1.0.0 /opt/resolve/libs/libOpenCL.so.1
```

* Copy the system's libglib libraries into DaVinci Resolve's library folder

```console
$ sudo cp -va /lib64/libglib-2.0.* /opt/resolve/libs/
```


Now DaVinci Resolve will start and work.
