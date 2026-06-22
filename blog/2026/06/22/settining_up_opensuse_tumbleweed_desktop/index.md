# Setting up OpenSUSE Tumbleweed desktop

## Enable paste-on-middle-click

I have used paste-on-middle-click for many years.
I have specifically bought `LoLogitech MX Anywhere 2` mice with their dedicated middle-button.

Sometime in early 2026 Gnome decided that paste-on-middle-click was confusing to users, so they disabled it.

The following command, entered at a terminal/console, will re-enable paste-on-middle-click.

```console
gsettings set org.gnome.desktop.interface gtk-enable-primary-paste true
```

## Disable screen blanking and snooze at login screen

Sometimes I want to turn on my Tumbleweed desktop and access it remotely via SSH.
The desktop will be sitting at the login screen while SSH onto the machine and rummage for files.

The default is for Tumbleweed to blank the screen then enter sleep mode when it is at the login screen for a while. Sleeping the computer while I'm accessing it remotely over SSH is less than ideal.

The following commands, entered at a terminal/console, will disable idle power saving.

1. become the `gdm` user:

```console
sudo su -l -s /bin/sh gdm
```

2. set the idle time to zero seconds, thereby disabling the idle power saving:

```console
dbus-launch gsettings set org.gnome.desktop.session idle-delay 0
```

3. confirm the setting has been altered

```console
DCONF_PROFILE=gdm dconf dump /org/gnome/desktop/session/
```

4. the result should be:

```text
[/]
idle-delay=uint32 0
session-name='gnome-login'
```
