# Migrate an ESXi host to Harvester

I've just moved a Windows10 host from ESXi to Harvester 1.0.0.

Basically:

- stopped the VM on ESXi
- scped the disk volume (a `<name>.vmdk` ~500 byte text file, and a `<name>-flat.vmdk` binary fie) from the ESXi host to a Linux host
- converted the disk volume to qcow2

  ```console
  $ qemu-img convert \
    -f vmdk \
    -O qcow2 \
    W10Home_0.vmdk W10Home.qcow
  ```

- uploaded that qcow2 to Harvester
- created a VM using the 'raw image base template'
- on the volume tab I selected the image I'd just uploaded, and made it a SATA type (neither VirtIO nor SCSI worked, for me - OS could not find the boot disk)
- started the VM

It booted.

No idea on the performance yet - I'm just impressed it worked!

## Drivers

I expect I can make it better with some drivers...

- in `Images` I created a new image using the URL of the Windows driver ISO file from [here](https://fedorapeople.org/groups/virt/virtio-win/direct-downloads/archive-virtio/virtio-win-0.1.215-2/)
- in `Volumes` I use the `Add VM Image` option to add a volume of type cd-rom using the image I just added
- rebooted the VM
- In the Windows 10 VM I installed the drivers from the CD-ROM - `D:\`

Once the drivers were installed, In Harvester I could alter VM to switch the root disk from SATA to VirtIO, and when the VM had rebooted the Win10 VM continued to work.
