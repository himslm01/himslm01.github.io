# SSH onto an old server

## The issue

I have an old virtual machine.
You might have an old IOT device or router or something.

For me, the old VM is running OpenSSH_3.5p1 - protocol  version 1.99.

Yes, I know it's very out of date and fairly insecure, but the server is firmly secured in a DMZ on my network, so I feel fairly safe.

The problem I'm trying to fix is that my desktop `ssh` client refuses to connect to the remote `ssh` server.

```console
$ ssh device
Unable to negotiate with 10.11.12.13 port 22: no matching key exchange method found.
Their offer: diffie-hellman-group-exchange-sha1,diffie-hellman-group1-sha1
```

## The solution

I use `~/.ssh/config` extensively, with lots of entries for lots of machines.

```text
Host device
  hostname device.lan
  user user
```

I needed to add some configuration lines to change the options for this specific device:

1. tell my `ssh` client to add the `diffie-hellman-group1-sha1` key exchange algorithm to this host
2. tell my `ssh` client which ciphers to use for this host
3. tell my `ssh` client the minimum RSA key size to use for this host

```text
Host device
  hostname device.lan
  user user
  KexAlgorithms +diffie-hellman-group1-sha1
  Ciphers aes256-cbc,aes128-ctr,aes192-ctr,aes256-ctr,aes128-cbc,3des-cbc
  RequiredRSASize 1024
```

Now the `ssh` command works.

```console
$ ssh device
 *************************************************
 *                                               *
 *          Greetings, and welcome.              *
 *                                               *
 *************************************************
$ 
```
