# Installing Desktop Video on OpenSuse Tumbleweed Kernel 6.8

Last tested June 2024 with Desktop Video version 12.9

* Install the RPMs from the Desktop Video 12.9 zip file downloaded from the Blackmagic site:
<https://www.blackmagicdesign.com/support/download/6e65a87d97bd49e1915c57f8df255f5c/Linux>

* Download the zip file containing a patch file attached to this post:
<https://forum.blackmagicdesign.com/viewtopic.php?f=12&t=199022&p=1047235#p1034293>

```console
$ cd /tmp
$ curl -o blackmagic-io-12.8.1a1-001-fix_for_kernel_6.8.zip https://forum.blackmagicdesign.com/download/file.php?id=72754
```

* Extract the zip file

```console
$ cd /tmp
$ unzip blackmagic-io-12.8.1a1-001-fix_for_kernel_6.8.zip
```

* Patch the Blackmagic source

```console
$ cd /usr/src/blackmagic-io-12.9a3
$ sudo patch -p 2  < /tmp/blackmagic-io-12.8.1a1-001-fix_for_kernel_6.8.patch
```

* In order to make dkms do a thorough recompile, I had to delete remnants of old builds

```console
$ sudo rm -rf /var/lib/dkms/blackmagic/12.7a4
$ sudo rm -rf /var/lib/dkms/blackmagic-io/12.7a4/
```

* Then I could use the usual dkms rebuild process

```console
$ sudo dkms autoinstall -k $(uname -r)
```

Now MediaExpress sees my Blackmagic Design Intensity Pro 4K.
