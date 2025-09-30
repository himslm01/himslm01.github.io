# BTRFS

* [Device groups and subvolumes](#device-groups-and-subvolumes)
* [Building BTRFS device groups](#making-btrfs-device-groups)
  * [Making device groups](#creating-a-btrfs-device-group)
  * [Mounting device groups](#mounting-a-btrfs-device-group)
  * [Subvolumes or directories?](#device-group-subvolumes)
  * [Un-mounting device groups](#unmounting-the-btrfs-device-group)
  * [Building up to a RAID 10 device group](#building-up-to-a-raid10-device-group)
* [Subvolumes](#subvolumes)

## Device groups and subvolumes

With btrfs, one or more storage devices can be brought together to create a btrfs device group, into which one or more POSIX style filesystems can be created.

When a btrfs device group is created, a filesystem is always created in that device group.
That filesystem is called `/` and may be referred to as the "root subvolume", or the "top level subvolume", or the "root filesystem" of the btrfs device group.

Do not confuse a btrfs device group's root subvolume, called `/`, with the path of the root directory of your system's filesystems, also called `/`.
When you have multiple btrfs device groups, each device group will have its own root subvolume, each called `/`.
Your system will only have one directory path called `/` from which you can traverse to every other directory, file, and mounted filesystem in your system.

## Making BTRFS device groups

### Creating a btrfs device group

For example:

```text
$ sudo mkfs -t btrfs /dev/sda
```

Once created, the btrfs device group's root subvolume can be mounted anywhere onto the system's filesystem, either manually or using `/etc/fstab`.

### Mounting a BTRFS device group

For example:

```text
mount /dev/sda /mnt
```

And we can check the mount worked as expected:

```text
mount | grep "/mnt"
/dev/sda on /mnt type btrfs (rw,relatime,ssd,discard=async,space_cache=v2,subvolid=5,subvol=/)
```

Note the `subvolid=5` and the `subvol=/` from the output of the mount command, these will become important later.

#### Highlighting the above point about `/`

* This btrfs device group has it's root subvolume, which is called `/`, sometimes referred to as the "top level subvolume", or the "root filesystem", or `subvol=/`.
* This btrfs device group's root subvolume is mounted onto the systems filesystem at the directory `/mnt`.

### Device group Subvolumes

Subvolumes can be created within the root subvolume of the btrfs device group, and subvolumes can be created within other subvolumes.

Subvolumes look like a directory within the root filesystem or subvolume.
Looking at a directory or a subvolume will look identical from the system's filesystem.

#### Creating a subvolume

```text
$ cd /mnt
$ sudo mkdir foo
$ sudo btrfs subvolume create bar
Create subvolume './bar'
$ ls -la
total 16
drwxr-xr-x 1 root root  14 Nov 15 15:15 .
drwxr-xr-x 1 root root 164 Jul  4 18:00 ..
drwxr-xr-x 1 root root   0 Nov 15 15:15 bar
drwxr-xr-x 1 root root   0 Nov 15 15:15 foo
```

The only way to know that `/mnt/bar` is a subvolume is to list the subvolumes of the path we are at.

```text
$ sudo btrfs subvolume list .
ID 1016 gen 1964 top level 5 path bar
```

#### Removing a subvolume

Removing a subvolume can be done it two ways, remove it using usual system commands like rmdir (if it's empty) or rm -rf (if it's not empty), or the `btrf subvolume delete` command.

```text
$ sudo rmdir foo
$ sudo rmdir bar
```

### Unmounting the BTRFS device group

The device group can be unmounted manually:

```text
umount /mnt
```

## Building up to a RAID10 device group

It is possible to start with a single disk btrfs device group and later include other disks to make the btrfs device group fault tolerant and extend the pool size.

Lets do this for real - only **we'll use files instead of disks.**

1. make temporary files to be used in the exercise
1. make loopback devices to be used by btrfs
1. make a btrfs device group using one file only
1. turn the btrfs device group into a mirrored btrfs device group (RAID1)
1. extend the btrfs device group size (RAID10)

### 1. make the temporary files

Make four empty files which we can use as though they were disks.

```text
$ mkdir ~/tmp_btrfs
$ cd ~/tmp_btrfs/
$ for i in {0..3} ; do truncate -s 10G $i.raw ; done
$ ls -l
-rw-r--r--   1 user users 10737418240 Oct 29 08:50 0.raw
-rw-r--r--   1 user users 10737418240 Oct 29 08:50 1.raw
-rw-r--r--   1 user users 10737418240 Oct 29 08:50 2.raw
-rw-r--r--   1 user users 10737418240 Oct 29 08:50 3.raw
```

### 2. make loop devices

```text
for i in {0..3} ; do  losetup --show --find $i.raw ; done
````

remember the output of this command - it should look something like:

```text
/dev/loop1
/dev/loop2
/dev/loop3
/dev/loop4
```

### 3. make a btrfs filesystem

Using one of the loopback device created from the files, create a simple device group with one device.

```text
$ mkfs -t btrfs -f --mixed /dev/loop1
btrfs-progs v6.10.1
See https://btrfs.readthedocs.io for more information.

Performing full device TRIM /dev/loop22 (10.00GiB) ...
NOTE: several default settings have changed in version 5.15, please make sure
      this does not affect your deployments:
      - DUP for metadata (-m dup)
      - enabled no-holes (-O no-holes)
      - enabled free-space-tree (-R free-space-tree)

Label:              (null)
UUID:               67dc4d5e-3738-42c4-8013-71cbc8a54cba
Node size:          4096
Sector size:        4096    (CPU page size: 4096)
Filesystem size:    10.00GiB
Block group profiles:
  Data+Metadata:    single            8.00MiB
  System:           single            4.00MiB
SSD detected:       yes
Zoned device:       no
Features:           mixed-bg, extref, skinny-metadata, no-holes, free-space-tree
Checksum:           crc32c
Number of devices:  1
Devices:
   ID        SIZE  PATH
    1    10.00GiB  /dev/loop1
```

All operations need to be applied when the btrfs filesystem is mounted.
Mount the newly created btrfs device root subvolume onto `/mnt`.

```text
$ mount /dev/loop1 /mnt
```

Once the btrfs device group is mounted onto the system's filesystem, here at the directory `/mnt`, the device group can be interacted with and manipulated by referring to that mount point.

### 4. mirror the pool (RAID1)

Add another device into this btrfs device group.

```text
$ btrfs device add -f /dev/loop2 /mnt
Performing full device TRIM /dev/loop2 (10.00GiB) ...
```

Convert the device group into a Raid 1 mirrored pair.

```text
$ btrfs balance start -dconvert=raid1 -mconvert=raid1 /mnt
Done, had to relocate 2 out of 2 chunks
```

### 4. extend the pool (RAID10)

Add two more devices into the btrfs device group.

```text
$ btrfs device add -f /dev/loop3 /dev/loop4 /mnt
Performing full device TRIM /dev/loop3 (10.00GiB) ...
Performing full device TRIM /dev/loop4 (10.00GiB) ...
```

convert the device group into a Raid 10 striped mirror

```text
$ btrfs balance start -dconvert=raid10 -mconvert=raid10 /mnt
Done, had to relocate 2 out of 2 chunks
```

```text
btrfs filesystem usage /mnt
Overall:
    Device size:              40.00GiB
    Device allocated:          8.06GiB
    Device unallocated:       31.94GiB
    Device missing:            0.00B
    Device slack:              0.00B
    Used:                     88.00KiB
    Free (estimated):         19.97GiB   (min: 19.97GiB)
    Free (statfs, df):        19.97GiB
    Data ratio:                2.00
    Metadata ratio:            2.00
    Global reserve:            1.38MiB   (used: 0.00B)
    Multiple profiles:           no

Data+Metadata,RAID10: Size:4.00GiB, Used:40.00KiB (0.00%)
   /dev/loop1       2.00GiB
   /dev/loop2       2.00GiB
   /dev/loop3       2.00GiB
   /dev/loop4       2.00GiB

System,RAID10: Size:32.00MiB, Used:4.00KiB (0.01%)
   /dev/loop1      16.00MiB
   /dev/loop2      16.00MiB
   /dev/loop3      16.00MiB
   /dev/loop4      16.00MiB

Unallocated:
   /dev/loop1       7.98GiB
   /dev/loop2       7.98GiB
   /dev/loop3       7.98GiB
   /dev/loop4       7.98GiB
```

### 5. Or add all devices in one go

Using all four of the loopback devices created from the files, create a Raid 10 pool.

```text
$ mkfs -t btrfs --mixed --metadata raid10 --data raid10 /dev/loop1 /dev/loop2 /dev/loop3 /dev/loop4
```

## Subvolumes

As seen above, to interact with a btrfs device group the group has to be mounted onto the system's filesystem.
In the examples above, the btrfs device group's root subvolume was mounted onto the directory at the path `/mnt` on the system's filesystem.

Once that has been done, the device group can be interacted with by referring to the mount point path `/mnt`.

But as was alluded to by the output of the mount command above, where it said `subvol=/`, it's possible to mount a btrfs subvolume onto any (usually empty) directory of the system's filesystem.

Subvolumes can have any valid filesystem compliant name, we've seen that subvolumes look just like directories.
But there is a _convention_ that subvolumes which are going to be mounted begin with `/@/`.

Many desktop linux distributions use btrfs as their filesystem, and mount subvolumes instead of partitions to separate data and make for easier backups and rollbacks.
For instance distributions may create btrfs subvolumes called `/@/home`, `/@/opt`, `/@/root`, `/@/srv`, `/@/usr/local` and `/@/var` which would be mounted on `/home`, `/opt`, `/root`, `/srv`, `/usr/local` and `/var` respectively.
In this instance, the btrfs device group's root subvolume would usually not be mounted onto the system's filesystem.

The subvolumes are mounted at boot file through the instructions in `/etc/fstab`, which might look like this:

```text
/dev/sda3  /           btrfs  defaults             0  0
/dev/sda3  /var        btrfs  subvol=/@/var        0  0
/dev/sda3  /usr/local  btrfs  subvol=/@/usr/local  0  0
/dev/sda3  /srv        btrfs  subvol=/@/srv        0  0
/dev/sda3  /root       btrfs  subvol=/@/root       0  0
/dev/sda3  /opt        btrfs  subvol=/@/opt        0  0
/dev/sda3  /home       btrfs  subvol=/@/home       0  0
```

_just an example - better fstab usage is preferred_
