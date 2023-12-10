# Installing Blackmagic Design DaVinci Resolve Studio on OpenSuse Tumbleweed

Last tested in December 2023 with DaVinci Resolve 18.6.4.

* Download the Linux install zip file from <https://www.blackmagicdesign.com/support/family/davinci-resolve-and-fusion>

* Extract the contents of the zip file you downloaded

```console
unzip DaVinci_Resolve_Studio_18.6.4_Linux.zip
```

* Install the package, ignoring the whether all of the prerequisite packages are installed

```console
cd DaVinci_Resolve_Studio_18.6.4_Linux
SKIP_PACKAGE_CHECK=1 ./DaVinci_Resolve_Studio_18.6.4_Linux.run
```

* Download the gdk-pixbuf2 package from Fedora

```console
curl -O https://dl.fedoraproject.org/pub/fedora/linux/releases/38/Everything/x86_64/os/Packages/g/gdk-pixbuf2-2.42.10-2.fc38.x86_64.rpm
```

* Extract the contents of the rpm file you downloaded

```console
rpm2cpio ./gdk-pixbuf2-2.42.10-2.fc38.x86_64.rpm | cpio -idmv
```

* Copy the library files into DaVinci Resolve's library folder

```console
cd usr/lib64/
sudo cp -vr * /opt/resolve/libs/
```

* Copy the system's libc libraries into DaVinci Resolve's library folder

```console
sudo cp -va /lib64/libglib-2.0.* /opt/resolve/libs/
```
