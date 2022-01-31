# What I install on a new Ubuntu desktop

## Generaly required stuff

1. Update apt and install retuired stuff

       sudo apt update
       sudo apt install apt-transport-https \
         curl wget \
         vim-nox \
         xmlstarlet \
         openssh-server \
         git mercurial \
         default-jre default-jdk \
         openjdk-17-jdk

## Chrome browser

1. Download the latest deb with `curl -v -O -L https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb`
1. Install the deb with `sudo dpkg -i google-chrome-stable_current_amd64.deb`
1. Install any unsatisfied dependencies with `sudo apt install -f`

## Visual Studio Code

1. Download the latest deb from <https://code.visualstudio.com/docs/setup/linux>
1. Install the deb with `sudo dpkg -i code_1.63.2-1639562499_amd64.deb`
1. Install any unsatisfied dependencies with `sudo apt install -f`

## DbVizulasier

1. Download the latest deb with Java from <https://www.dbvis.com/download/>
1. Install the deb with `sudo dpkg -i dbvis_linux_12_1_5.deb`

## Rancher Desktop

1. Download the latest deb from <https://software.opensuse.org/download.html?project=isv%3ARancher%3Astable&package=rancher-desktop>
1. Install the deb with `sudo dpkg -i rancher-desktop_0.7.1-2.1_amd64.deb`
1. Install any unsatisfied dependencies with `sudo apt install -f`

Rancher Desktop will install `kubectl` and `helm`, which will make Visual Studio Code stop complaining

## Slack

1. Download the latest deb from <https://slack.com/intl/en-gb/downloads/linux>
1. Install the deb with `sudo dpkg -i slack-desktop-4.23.0-amd64.deb`

## Microsoft Teams

1. Download the latest deb from <https://www.microsoft.com/en-gb/microsoft-teams/download-app>
1. Install the deb with `sudo dpkg -i teams_1.4.00.26453_amd64.deb`

## Tabby terminal

1. Download the latest deb from
1. Install the deb with `sudo dpkg -i tabby-1.0.169-linux.deb`
1. Install any unsatisfied dependencies with `sudo apt install -f`
