# FAT

## Making a FAT filesystem in a file

I have a server which needs its BIOS upgraded.
The upgrade BIOS needs to be put onto a USB device formatted with a FAT filesystem and attached to the server.

The server is remote.

The server does have a network KVM attached.
The KVM can have an image uploaded to it, which it can expose as a USB device to the server.

So, I need to create a file which looks like a FAT formatted filesystem, put the BIOS onto it, and upload it to the KVM.

### The process to create a file containing a FAT filesystem

Create a file large enough to gold the FAT filesystem and the BIOS file, but not too big as it has to be uploaded to a remote location.

```console
truncate -s 128M fat-disk.img
```

Create a loop-back device pointing to the file, so that software requiring devices not files can work on the file.

```console
sudo losetup --show --find fat-disk.img
```

Format the loop-back device as a FAT filesystem.

```console
sudo mkfs -t fat /dev/loop0
```

Mount the formatted loop-back device.

```console
sudo mount /dev/loop0 /mnt
```

At this point I can copy the BIOS file, or any other files, to the directory I mounted the loop-back device onto.

When I'm done I reverse the process.

Unmount the loop-back device.

```console
sudo umount /mnt
```

Remove the loop-back device.

```console
sudo losetup --detach /dev/loop0
```

Now the file, fat-disk.img, can be uploaded to the network KVM, mount it onto the server, and upgrade the server's BIOS.
All achieved remotely.

Nice.
