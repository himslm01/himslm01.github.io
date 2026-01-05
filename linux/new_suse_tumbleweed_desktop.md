# Install a new OpenSuse Tumbleweed desktop

## tweek Linux boot

```console
sudo vim /etc/default/grub
```

remove `splash=silent` and `quiet` from `GRUB_CMDLINE_LINUX_DEFAULT`

from

```text
GRUB_CMDLINE_LINUX_DEFAULT="splash=silent mitigations=auto quiet security=apparmor nosimplefb=1"
```

to

```text
GRUB_CMDLINE_LINUX_DEFAULT="mitigations=auto security=apparmor nosimplefb=1"
```

then update the boot-loader, and reboot

```console
sudo update-bootloader --refresh
sudo systemctl reboot
```

check the Linux command line

```console
cat /proc/cmdline
```

## Install Gnome extensions

* `Dock to Dash`
  * Position and size
    * Left
    * Extend to edge of screen
    * icon size: 28px
  * Launchers
    * Show open windows previews
  * Behaviour
    * Click action -> Show window previews
    * Scroll action -> Cycle through windows
  * Appearance
    * Shrink the dash
    * Customise windows counter indicators -> dots
    * Force straight corners
* `Tray Icons: Reloaded`
  * General
    * Tray icons limit -> 10
    * Icon size -> 20
    * Tray margin -> 4, 0
    * Icon margin -> 0, 4
    * Icon padding -> 0, 0

## Configure

Using `Tweaks`

* Enable minimise and maximise in the `Window Titlebars` menu
* Enable Weekday and Seconds in the `Top Bar` menu

## Install using zypper

* git
* Rancher Desktop
* VLC

```console
sudo zypper install git
sudo zypper install rancher-desktop
sudo zypper install vlc
```

## Download and install

* Visual Studio Code <https://code.visualstudio.com/download>
* DBvisualizer <https://www.dbvis.com/download/>
* Slack <https://slack.com/intl/en-gb/downloads/linux>
* Teams <https://snapcraft.io/install/teams/opensuse#install>

```console
sudo zypper addrepo --refresh https://download.opensuse.org/repositories/system:/snappy/openSUSE_Tumbleweed snappy
sudo zypper --gpg-auto-import-keys refresh
sudo zypper dup --from snappy
sudo zypper install snapd
sudo systemctl enable snapd
sudo systemctl start snapd
sudo systemctl enable snapd.apparmor
sudo systemctl start snapd.apparmor
sudo snap install teams
```

* Chrome <https://www.simplified.guide/google-chrome/install-on-suse>

```console
curl -o ~/Downloads/google_linux_signing_key.pub https://dl.google.com/linux/linux_signing_key.pub
sudo rpm --import ~/Downloads/google_linux_signing_key.pub
sudo zypper addrepo http://dl.google.com/linux/chrome/rpm/stable/x86_64 Google-Chrome
sudo zypper refresh
sudo zypper install --no-confirm google-chrome-stable
```

* Lens <https://k8slens.dev/> download the rpm then install it. There's a slight issue with dependent library names, but you can get zypper to ignore it.

```console
> sudo zypper install  ~/Downloads/Lens-5.3.4-latest.20220120.1.x86_64.rpm
Loading repository data...
Reading installed packages...
Resolving package dependencies...

Problem: nothing provides 'libuuid' needed by the to be installed lens-5.3.4_latest.20220120.1-20220120.1.x86_64
 Solution 1: do not install lens-5.3.4_latest.20220120.1-20220120.1.x86_64
 Solution 2: break lens-5.3.4_latest.20220120.1-20220120.1.x86_64 by ignoring some of its dependencies

Choose from above solutions by number or cancel [1/2/c/d/?] (c): 2
Resolving dependencies...
Resolving package dependencies...

The following 2 NEW packages are going to be installed:
  lens libnotify-tools

2 new packages to install.
Overall download size: 105.8 MiB. Already cached: 0 B. After the operation, additional 565.1 MiB will be used.
Continue? [y/n/v/...? shows all options] (y):
Retrieving package libnotify-tools-0.7.9-1.11.x86_64                            (1/2),  21.1 KiB ( 19.8 KiB unpacked)
Retrieving: libnotify-tools-0.7.9-1.11.x86_64.rpm .............................................................[done]
Retrieving package lens-5.3.4_latest.20220120.1-20220120.1.x86_64               (2/2), 105.8 MiB (565.1 MiB unpacked)
Lens-5.3.4-latest.20220120.1.x86_64.rpm:
    Package is not signed!

lens-5.3.4_latest.20220120.1-20220120.1.x86_64 (Plain RPM files cache): Signature verification failed [6-File is unsigned]
Abort, retry, ignore? [a/r/i] (a): i

Checking for file conflicts: ..................................................................................[done]
(1/2) Installing: libnotify-tools-0.7.9-1.11.x86_64 ...........................................................[done]
(2/2) Installing: lens-5.3.4_latest.20220120.1-20220120.1.x86_64 ..............................................[done]

```

## Copy from somewhere

```console
rsync --ignore-existing -raz --progress /run/media/mdsh/docker002-home-m/.gitconfig .
rsync --ignore-existing -raz --progress /run/media/mdsh/docker002-home-m/.abcde.conf .
rsync --ignore-existing -raz --progress /run/media/mdsh/docker002-home-m/.aws .
rsync --ignore-existing -raz --progress /run/media/mdsh/docker002-home-m/.aws-sam .
rsync --ignore-existing -raz --progress /run/media/mdsh/docker002-home-m/.config .
rsync --ignore-existing -raz --progress /run/media/mdsh/docker002-home-m/.dbvis   .
rsync --ignore-existing -raz --progress /run/media/mdsh/docker002-home-m/.kube .
rsync --ignore-existing -raz --progress /run/media/mdsh/docker002-home-m/.local .
rsync --ignore-existing -raz --progress /run/media/mdsh/docker002-home-m/.mozilla .
rsync --ignore-existing -raz --progress /run/media/mdsh/docker002-home-m/.m2 .
rsync --ignore-existing -raz --progress /run/media/mdsh/docker002-home-m/.ssh .
rsync --ignore-existing -raz --progress /run/media/mdsh/docker002-home-m/.vscode .
rsync --ignore-existing -raz --progress /run/media/mdsh/docker002-home-m/Documents .
rsync --ignore-existing -raz --progress /run/media/mdsh/docker002-home-m/Source .
```
