# ZFS

Contents:

* [Install zfs](#install)
  * [Install zfs on OpenSUSE Tumbleweed](#tumbleweed)
  * [Install zfs on MacOS](#macos)
* [Building zfs pools](#build)
  * [Building up to a zfs RAID10 pool](#raid10)
* [Create a zfs pool with some datasets on a removable disk](#createpool)
  * [Transfer the pool to another host](#transferpool)
  * [Creating root level directories on MacOS](#macosdirectories)

<div id='install'/>

## Install zfs

<div id='tumbleweed'/>

### Install on OpenSUSE Tumbleweed

OpenZFS is available for install from a separate repository which needs to be added to OpenSUSE.

```text
zypper addrepo https://download.opensuse.org/repositories/filesystems/openSUSE_Tumbleweed/filesystems.repo
zypper refresh
zypper install zfs
```

For more details see the [OpenSUSE ZFS package](https://software.opensuse.org/download.html?project=filesystems&package=zfs).

<div id='macos'/>

## Install on MacOS

Download from [here](https://openzfsonosx.org/wiki/Downloads) the appropriate OpenZFS for OSX package for your MacOS version, or the installer containing all of the packages, and install the appropriate `.pkg` file for your operating system. A reboot is required after the install has completed.

<div id='build'/>

## Building pools

<div id='raid10'/>

### Building up to a RAID10 pool

It is possible to start with a single disk pool and later include other disks to make the pool fault tolerant and extend the pool size.

Lets do this for real - only **we'll use files instead of disks.**

1. make temporary files to be used in the exercise
1. make a pool using one file only
1. turn the pool into a mirrored pool (RAID1)
1. extend the pool size (RAID10)
1. reduce the pool size
1. destroy the pool

Carefully note the difference between `add` and `attach` - they are very different actions to the pool.
**Using the wrong verb will be disastrous to your data.**

#### 1. make the temporary files

Make four empty files which we can use as though they were disks.

```text
$ mkdir ~/tmp_zfs
$ cd ~/tmp_zfs/
$ for i in {0..3} ; do truncate -s 10G $i.raw ; done
$ ls -l
-rw-r--r--   1 user users 10737418240 Oct 29 08:50 0.raw
-rw-r--r--   1 user users 10737418240 Oct 29 08:50 1.raw
-rw-r--r--   1 user users 10737418240 Oct 29 08:50 2.raw
-rw-r--r--   1 user users 10737418240 Oct 29 08:50 3.raw
```

#### 2. make a pool

Using one of the files, create a simple pool with one vdev.

```text
$ sudo zpool create -o ashift=12 tmp_zfs "$(pwd)/0.raw"
$ sudo zpool status tmp_zfs
  pool: tmp_zfs
 state: ONLINE
config:

    NAME                        STATE     READ WRITE CKSUM
    tmp_zfs                     ONLINE       0     0     0
      /home/user/tmp_zfs/0.raw  ONLINE       0     0     0

errors: No known data errors
```

#### 3. mirror the pool (RAID1)

Include a second file in the pool to create a mirrored vdev.

```text
$ sudo zpool attach tmp_zfs "$(pwd)/0.raw" "$(pwd)/1.raw"
$ sudo zpool status tmp_zfs
  pool: tmp_zfs
 state: ONLINE
  scan: resilvered 696K in 00:00:00 with 0 errors on Tue Oct 29 08:54:17 2024
config:

    NAME                          STATE     READ WRITE CKSUM
    tmp_zfs                       ONLINE       0     0     0
      mirror-0                    ONLINE       0     0     0
        /home/user/tmp_zfs/0.raw  ONLINE       0     0     0
        /home/user/tmp_zfs/1.raw  ONLINE       0     0     0

errors: No known data errors
```

Note how the vdev `mirror-0` has appeared.

#### 4. extend the pool (RAID10)

Two options: and one drive at a time, or add two drives together.

##### 4.1.1 add single drive

```text
$ sudo zpool add -f tmp_zfs "$(pwd)/2.raw"
mdsh@mdsh01:~/tmp_zfs$ sudo zpool status tmp_zfs
  pool: tmp_zfs
 state: ONLINE
  scan: resilvered 696K in 00:00:00 with 0 errors on Tue Oct 29 08:54:17 2024
config:

    NAME                          STATE     READ WRITE CKSUM
    tmp_zfs                       ONLINE       0     0     0
      mirror-0                    ONLINE       0     0     0
        /home/user/tmp_zfs/0.raw  ONLINE       0     0     0
        /home/user/tmp_zfs/1.raw  ONLINE       0     0     0
      /home/user/tmp_zfs/2.raw    ONLINE       0     0     0

errors: No known data errors
```

Note that `-f` is required, otherwise you get an error:

```text
invalid vdev specification
use '-f' to override the following errors:
mismatched replication level: pool uses mirror and new vdev is file
```

##### 4.1.2 then attach the mirror drive

```text
$ sudo zpool attach tmp_zfs "$(pwd)/2.raw" "$(pwd)/3.raw"
$ sudo zpool status tmp_zfs
  pool: tmp_zfs
 state: ONLINE
  scan: resilvered 96K in 00:00:00 with 0 errors on Tue Oct 29 08:55:45 2024
config:

    NAME                          STATE     READ WRITE CKSUM
    tmp_zfs                       ONLINE       0     0     0
      mirror-0                    ONLINE       0     0     0
        /home/user/tmp_zfs/0.raw  ONLINE       0     0     0
        /home/user/tmp_zfs/1.raw  ONLINE       0     0     0
      mirror-1                    ONLINE       0     0     0
        /home/user/tmp_zfs/2.raw  ONLINE       0     0     0
        /home/user/tmp_zfs/3.raw  ONLINE       0     0     0

errors: No known data errors
```

Note how the vdev `mirror-1` has appeared.

##### 4.1.3 detach the mirror drive

It's also possible to remove the redundancy from the mirror.

```text
$ sudo zpool detach tmp_zfs  "$(pwd)/3.raw"
$ sudo zpool status tmp_zfs
  pool: tmp_zfs
 state: ONLINE
  scan: resilvered 96K in 00:00:00 with 0 errors on Tue Oct 29 08:55:45 2024
config:

    NAME                          STATE     READ WRITE CKSUM
    tmp_zfs                       ONLINE       0     0     0
      mirror-0                    ONLINE       0     0     0
        /home/user/tmp_zfs/0.raw  ONLINE       0     0     0
        /home/user/tmp_zfs/1.raw  ONLINE       0     0     0
      /home/user/tmp_zfs/2.raw    ONLINE       0     0     0

errors: No known data errors
```

##### 4.1.4 remove the extension

And in some circumstances it's also possible to make the pool smaller again - assuming all the data can fit onto the remaining space, and other assumptions.

```text
$ sudo zpool remove tmp_zfs  "$(pwd)/2.raw"
$ sudo zpool status tmp_zfs
  pool: tmp_zfs
 state: ONLINE
  scan: resilvered 96K in 00:00:00 with 0 errors on Tue Oct 29 08:55:45 2024
remove: Removal of vdev 1 copied 108K in 0h0m, completed on Tue Oct 29 11:46:06 2024
    96 memory used for removed device mappings
config:

    NAME                          STATE     READ WRITE CKSUM
    tmp_zfs                       ONLINE       0     0     0
      mirror-0                    ONLINE       0     0     0
        /home/user/tmp_zfs/0.raw  ONLINE       0     0     0
        /home/user/tmp_zfs/1.raw  ONLINE       0     0     0

errors: No known data errors
```

It might take some time to copy the data from the removed vdev to the remaining vdevs. Monitor the progress with the `zpool status` command. **Don't just yank the removed drive.**

##### 4.2 add a mirror pair of drives in one go

```text
$ sudo zpool add tmp_zfs mirror "$(pwd)/2.raw" "$(pwd)/3.raw"
$ sudo zpool status tmp_zfs
  pool: tmp_zfs
 state: ONLINE
  scan: resilvered 96K in 00:00:00 with 0 errors on Tue Oct 29 08:55:45 2024
config:

    NAME                          STATE     READ WRITE CKSUM
    tmp_zfs                       ONLINE       0     0     0
      mirror-0                    ONLINE       0     0     0
        /home/user/tmp_zfs/0.raw  ONLINE       0     0     0
        /home/user/tmp_zfs/1.raw  ONLINE       0     0     0
      mirror-2                    ONLINE       0     0     0
        /home/user/tmp_zfs/2.raw  ONLINE       0     0     0
        /home/user/tmp_zfs/3.raw  ONLINE       0     0     0

errors: No known data errors
```

Note how the vdev `mirror-2` has appeared.

#### 5. remove the pool

```text
$ sudo zpool destroy tmp_zfs
$ sudo zpool status tmp_zfs
cannot open 'tmp_zfs': no such pool
```

<div id='createpool'/>

## Create a pool on a removable disk with some datasets

Note that creating pools on USB attached disks is not recommended, due to the possability of USB disks becoming detached unexpectedly. But sometimes needs must, and it might be an okay plan for a backup disk for home.

Find the disk you are after using in the list of disks attached to the host. Best practice is to use the disk ID, because it is consistent across reboots and re-plugs.

```text
ls -l /dev/disk/by-id/
```

Creating a pool automatically creates a root dataset on that pool. Best practice is to not use the root dataset, therefore set the mount point to the magic value of `none` so that it will not be mounted.

```text
zpool create -m none -f usbwd14t usb-WD_Elements_25A3_394D48423242364A-0:0
```

Now make a non-root dataset and mount it onto a directory on the filesystem.

```text
zfs create usbwd14t/old_disks
mkdir /export
zfs set mountpoint=/export/old_disks usbwd14t/old_disks
```

Or alternatively

```text
mkdir /export
zfs create -o mountpoint=/export/old_disks usbwd14t/old_disks
```

Other datasets can be created at the root level or within the dataset just created. For instance, I'll create a dataset within the 'old_disks` dataset and rsync an old disk's contents into it.

```text
zfs create usbwd14t/old_disks/segate_500G
rsync -var /mnt/sdc1 /export/old_disks/segate_500G/
```

<div id='transferpool'/>

## Transfer the pool to another host

To properly unmount a zfs disk you need to un-attach the pool from the current host. This is done with the `zpool export` command.

```text
zpool export usbwd14t
```

This zfs disk can now be connected to another host and the pool can be attached to the new host. This is done with the `zpool import` command.

```text
zpool import usbwd14t
```

Note that the directories that the datasets are set to mount on must either exist already on the new host, or be creatable by the ZFS drivers. For the directories to be creatable means the filesystem cannot be read-only. See the [section below](#macosdirectories) if you need to create a root level directory on MacoOS.

<div id='macosdirectories'/>

## Creating root level directories on MacOS

Since around 2020 the root partition on MacOS has been read-only.

Since I set the mount points for the datasets in the pool to be within the `/export` directory on Linux, I'm going to need to create that on MacOS. Alternate solutions might be to use the `-R` option to set the alternate root for the pool, or use the `-N` option to not automatically mount the datasets in the pool.

If you need to create a new directory on the root partition then this is the procedure.

1. make a new directory in a writeable area of the filesystem. /System/Volumes/Data/ is a good place:

```text
mkdir /System/Volumes/Data/export
```

2. create a new entry in `/etc/synthetic.conf` (if you have to create the file it should be owned by `root`, group `wheel`, and permissions `0644`)

```text
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

```text
/System/Library/Filesystems/apfs.fs/Contents/Resources/apfs.util -t
```

Or reboot the MacOS machine.
