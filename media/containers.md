# Video and Audio Media Containers

A media container could be a file format or a stream format.
In general terms, it will bring together one or more media streams with basic metadata about format of the media stream(s).

The container may hold one or more media streams. When multiple media streams are combined together in a container, the container must preserve synchronisation information binding the streams together, so that the media streams can be played in synchronisation at a later point.

A basic media container could combine a video stream and and audio stream into a single file, such that a media player could play that file at a later date and the video stream and audio stream will be played together.

## AVI

## QuickTime MOV

[QuickTime documentation at Apple](https://developer.apple.com/library/mac/documentation/quicktime/QTFF/qtff.pdf)

## MP4

## MXF

Material Exchange Format is used widely throughout broadcast, including in the IMX family of files, in DPP AS-11 files, and in IMF.

### MXF AS-11

To quote [http://www.amwa.tv/projects/AS-11.shtml](http://www.amwa.tv/projects/AS-11.shtml):

> AS-11 is a vendor-neutral subset of the MXF file format to use for delivery of finished programming from program producers and distributors to broadcast stations.
>
> AS-11 Files are intended to be complete and ready for playout. AS-11 support playout while the file transfer is in progress, a workflow is referred to as “late delivery”. It is preferable for AS-11 files to be used by playout servers directly without rewrapping of the MXF data structures. The content may be delivered at the ultimate bit-rate, picture format and aspect ratio, or it may be transcoded at the broadcast station to the required bit-rates and formats.

## WAV
