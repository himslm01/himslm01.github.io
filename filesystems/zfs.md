# ZFS

## Install on OpenSUSE Tumbleweed

```console
zypper addrepo https://download.opensuse.org/repositories/filesystems/openSUSE_Tumbleweed/filesystems.repo
zypper refresh
zypper install zfs
```

For more details see the [OpenSUSE ZFS package](https://software.opensuse.org/download.html?project=filesystems&package=zfs).

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

Note that the directories that the datasets are set to mount on must exist on the new host.
