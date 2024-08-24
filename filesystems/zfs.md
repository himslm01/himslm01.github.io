# ZFS

## Install on OpenSUSE Tumbleweed

OpenZFS is available for install from a separate repository which needs to be added to OpenSUSE.

```console
zypper addrepo https://download.opensuse.org/repositories/filesystems/openSUSE_Tumbleweed/filesystems.repo
zypper refresh
zypper install zfs
```

For more details see the [OpenSUSE ZFS package](https://software.opensuse.org/download.html?project=filesystems&package=zfs).

## Install on MacOS

Download the appropriate OpenZFS for OSX package for your MacOS version, or the installer containing all of the packages, from [here](https://openzfsonosx.org/wiki/Downloads) and install the appropriate `.pkg` file for your operating system. A reboot is required after the install has completed.

## Create a pool and a dataset

Find the disk you are after using in the list of disks attached to the host. Best practice is to use the disk ID, because it is consistent across reboots and re-plugs.

```console
ls -l /dev/disk/by-id/
```

Creating a pool automatically creates a root dataset on that pool. Best practice is to not use the root dataset, therefore set the mount point to the magic value of `none` so that it will not be mounted.

```console
zpool create -m none -f usbwd14t usb-WD_Elements_25A3_394D48423242364A-0:0
```

Now make a non-root dataset and mount it onto a directory on the filesystem.

```console
zfs create usbwd14t/old_disks
mkdir /export
zfs set mountpoint=/export/old_disks usbwd14t/old_disks
```

Other datasets can be created at the root level or within the dataset just created. For instance, I'll create a dataset within the 'old_disks` dataset and rsync an old disk's contents into it.

```console
zfs create usbwd14t/old_disks/segate_500G
rsync -var /mnt/sdc1 /export/old_disks/segate_500G/
```

## Transfer the disk to another host

To properly unmount a zfs disk you need to un-attach the pool from the current host. This is done with the `zpool export` command.

```console
zpool export usbwd14t
```

This zfs disk can now be connected to another host and the pool can be attached to the new host. This is done with the `zpool import` command.

```console
zpool import usbwd14t
```

Note that the directories that the datasets are set to mount on must exist on the new host. See the [section below](#creating-directories-on-macos) if you need to create a directory on MacoOS.

## Creating directories on MacOS

Since I used the directory `/export` on Linux I'm going to need to create that on MacOS.

Since around 2020 the root partition on MacOS has been read-only. If you need to create a new directory on the root partition then this is the procedure.

1. make a new directory in a writeable area of the filesystem. /System/Volumes/Data/ is a good place:

```console
mkdir /System/Volumes/Data/export
```

2. create a new entry in `/etc/synthetic.conf` (if you have to create the file it should be owned by `root`, group `wheel`, and permissions `0644`)

```console
touch /etc/synthetic.conf
chown root:wheel /etc/synthetic.conf
chmod 0644 /etc/synthetic.conf
printf "export\tSystem/Volumes/Data/export\n" >> /etc/synthetic.conf
```

3. get the system to read `/etc/synthetic.conf` and create the folder as a symbolic link to the actual folder created in step 1.

The APFS utility tool `apfs.util` should be able to do that for you.

```text
apfs.util <option>
   -t  : stitches and creates synthetic objects on root volume group.
```

So we can use that option.

```console
/System/Library/Filesystems/apfs.fs/Contents/Resources/apfs.util -t
```

Or reboot the host.
