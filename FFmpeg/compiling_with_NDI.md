# Compiling with NDI

## NDI SDK

Download the NDI SDK from the [ndi.video site](https://ndi.video/for-developers/ndi-sdk/).

Set an environment variable in your terminal pointing to the file you dlownloaded - this example will use the `Downloads` folder in my home directory:

```bash
export NDI_SDK_FILE=$HOME/Downloads/NewTek-NDI-3.7.sdk.tar.bz2
```

Decide where you will extract the NDI SDK to - it's best if there are no spaces in the folder name.

Set an environment variable in your terminal pointing to the folder you have decided - this example will use the `Source/ndi_sdk` folder in my home directory:

```bash
export NDI_SDK_HOME=$HOME/Source/ndi_sdk
```

Extract the SDK

```bash
$ mkdir -vp $NDI_SDK_HOME
$ cd $NDI_SDK_HOME
$ tar --strip-components=1 -xjf ${NDI_SDK_FILE}
```

## Patch set

An unofficial and unsupported set of patches are available.

Patch them into your source code.

```bash
$ cd <root_of_ffmpeg_source>
$ find <root_of_patch_set> -maxdepth 1 -type f -iname "*.patch" | while IFS= read -r file; do echo "${file}"; patch -p1 < "${file}" || exit 1; done
```

## Build FFmpeg

Build FFmpeg including the compile switch `--enable-libndi_newtek` and adding the location of the libs and include files:

```bash
$ cd <root_of_ffmpeg_source>
$ ./configure \
  --enable-nonfree \
  --enable-libndi_newtek \
  --extra-cflags="-I$NDI_SDK_HOME/include/ -L$NDI_SDK_HOME/lib/x86_64-linux-gnu"
$ make V=1
```
