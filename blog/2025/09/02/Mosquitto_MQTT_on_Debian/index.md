# Install and configure an MQTT broker server on Debian

I have used two MQTT broker servers, ActiveMQ and Mosquitto.

I have already written [instructions on installing ActiveMQ in a Kubernetes cluster](../kubernetes/activemq/), which has MQTT enabled.

This document will describe how to install a Mosquitto server on a simple server. I'm going to use a Raspberry Pi 5, but the instructions will be similar for other systems.

## Installation

### Ensure your system is up to date

```bash
sudo apt update && sudo apt upgrade
```

### Install Mosquitto

Running the following command will install both the Mosquitto MQTT broker server and a command-line MQTT client.

```bash
sudo apt install -y mosquitto mosquitto-clients
```

### Enable Mosquitto server to automatically start at system boot

```bash
sudo systemctl enable --now mosquitto.service
```

## Enable Remote Access and Authentication

By default the Mosquitto server will only listen for local connections, from software on the same server as the Mosquitto server.

IoT and other devices will need to remotely access this MQTT broker service, so a `listener` configuration is required to disable the local only access and enable remote access.

I also want to enable authentication for the remote devices.

To do these teo things, I need to edit one configuration file, create a user + password file, and enable remote access using that user + password file.

The Mosquitto documentation includes all of the [configuration commands](https://mosquitto.org/man/mosquitto-conf-5.html) which will be used.

The Mosquitto documentation also contains comprehensive information about the [authentication methods](https://mosquitto.org/documentation/authentication-methods/).

I use `vim.tiny` as my text file editor on my Raspberry Pi devices.

### Edit `mosquitto.conf`

Ensure that authentication and access control settings will be controlled on a per-listener basis.

```bash
sudo vim.tiny /etc/mosquitto/mosquitto.conf
```

Add the following as the first configuration line. It needs to be before some other configuration lines, so first in the file is best:

```text
per_listener_settings true
```

For me, the complete Mosquitto config file now contains this:

```text
# Place your local configuration in /etc/mosquitto/conf.d/
#
# A full description of the configuration file is at
# /usr/share/doc/mosquitto/examples/mosquitto.conf.example

per_listener_settings true

pid_file /run/mosquitto/mosquitto.pid

persistence true
persistence_location /var/lib/mosquitto/

log_dest file /var/log/mosquitto/mosquitto.log

include_dir /etc/mosquitto/conf.d
```

### Create a user + password file

Replace `<YOUR_USERNAME>` with the username you want to use:

```bash
mosquitto_passwd -c /etc/mosquitto/passwd_1883 <YOUR_USERNAME>
```

When you run that command with your username, you will be asked to type a password then re-enter the same password. No characters will be displayed while you type the password. Remember the username & password, you’ll need it later when you connect remotely.

### Enable remote access using that user + password file

```bash
sudo vim.tiny /etc/mosquitto/conf.d/listener_1883.conf
```

The complete contents of the new config file will be

```text
allow_anonymous false
listener 1883
password_file /etc/mosquitto/passwd_1883
```

Once the Mosquitto server is restarted, this configuration will disable the unauthenticated local-only connectors and enable an authenticated remotely available connector.

Note that with this configuration there is no encryption on the network traffic to this MQTT broker server. The username and password and all data sent to and from the server will be sent in the clear and could be intercepted.

### Restart Mosquitto server to pickup the changes

```bash
sudo systemctl restart mosquitto
```

## Test

In one terminal session, either local to your Mosquitto server or remote on another server, listen for all data sent to a test topic:

Replace `<YOUR_USERNAME>` and `<YOUR_PASSWORD>` with the username and password you used earlier:

```bash
mosquitto_sub -d -t testTopic -u <YOUR_USERNAME> -P <YOUR_PASSWORD>
```

In a second terminal session, either local to your Mosquitto server or remote on another server, send a message to the test topic:

Replace `<YOUR_USERNAME>` and `<YOUR_PASSWORD>` with the username and password you used earlier:

```bash
mosquitto_pub -d -t testTopic -m "Hello world!" -u <YOUR_USERNAME> -P <YOUR_PASSWORD>
```

In the first terminal session I can see I received the message sent from the second terminal session:

```bash
$ mosquitto_sub -d -t testTopic u <YOUR_USERNAME> -P <YOUR_PASSWORD>
Client (null) sending CONNECT
Client (null) received CONNACK (0)
Client (null) sending SUBSCRIBE (Mid: 1, Topic: testTopic, QoS: 0, Options: 0x00)
Client (null) received SUBACK
Subscribed (mid: 1): 0
Client (null) received PUBLISH (d0, q0, r0, m0, 'testTopic', ... (12 bytes))
Hello world!
```
