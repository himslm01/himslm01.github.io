# Migrate an ESXi host to Harvester

I've just moved a Windows10 host from ESXi to Harvester 0.3.0.

Basically:
- stopped the VM on ESXi
- scped the disk volume (a .vmdk (500 byte text file) and a -flat.vmdk) from the ESXi host to a Linux host
- converted the disk volume to qcow2

      qemu-img convert \
        -f vmdk \
        -O qcow2 \
        W10Home_0.vmdk W10Home.qcow

- uploaded that qcow2 to Harvester
- created a VM using the 'raw image base template'
- for the volume I selected the image I'd just uploaded, and made is a SATA type (neither VirtIO nor SCSI worked, for me - OS could not find the boot disk)
- unchecked "start virtual machine on creation", and created the VM
- edited the YAML to change spec.template.spec.domain.resources.requests.cpu to something small enough that the VM will start
- started the VM

No idea on the performance yet - I'm just impressed it worked!
I expect I can make it better with some drivers...
