# Video and Audio Media Codecs

Media codecs, short for coding and decoding, are methods of encoding video or audio streams such that it can be stored in a file or streamed across networks such that the streams can be decoded and presented elsewhere.

It is often the case that the process of encoding the media stream will compress the video or audio data, such that it takes less file-space / bit-rate.

Often the compression is lossy, which can cause fidelity of the video image or audio sound to be decreased.

## AV1

[AV1 on Wikipedia](https://en.wikipedia.org/wiki/AV1)

## MPEG-1 Part 1 Layer 2

More widely just known as **MP2 audio**.

[MPEG-1_Audio_Layer_II on Wikipedia](http://en.wikipedia.org/wiki/MPEG-1_Audio_Layer_II)

## MPEG-1 Part 2

More widely just known as **MPEG 1 video**.

[MPEG-1#Part_2:_Video on Wikipedia](http://en.wikipedia.org/wiki/MPEG-1#Part_2:_Video)

## MPEG-2 Part 2

More widely just known as **MPEG 2 video**, but the specification is known as **h.262**.

[h.262 on Wikipedia](http://en.wikipedia.org/wiki/H.262)

## MPEG-2 part 7

See [MPEG-4 Part 3](#mpeg-4-part-3).

## MPEG-4 Part 2

More widely known as **MPEG-4 video**.

The legacy codecs **DivX** and **Xvid** create variants of the MPEG-4 Part 2 video codec.

[MPEG-4 Part 2 on Wikipedia](http://en.wikipedia.org/wiki/MPEG-4_Part_2)

## MPEG-4 Part 3

More widely known as **Advanced Audio Coding** or **AAC**.
Both MPEG-2 part 7 and MPEG-4 part 3 are the same thing.

[Advanced_Audio_Coding on Wikipedia](http://en.wikipedia.org/wiki/Advanced_Audio_Coding)

[FFmpeg encoding hints](https://trac.ffmpeg.org/wiki/AACEncodingGuide)

## MPEG-4 part 10

More widely just known as **MPEG 4 Advanced Video Coding** or **H.264**, is currently the most widely used video codec for user-generated content and other video "on the Internet".

[MPEG-4 AVC on Wikipedia](http://en.wikipedia.org/wiki/H.264/MPEG-4_AVC)

### Encoding library

A widely used open source library is [libx264](https://code.videolan.org/videolan/x264), which is very configurable and extremely well regarded.

A widely used closed source encoding library is available from Main Concept.

## MPEG-H Part 2

More widely known as **High Efficiency Video Coding**, **HEVC**, or **H.265**.
Has become more common, as the real-time encoding becomes possible in new silicon in new devices.

[HEVC on Wikipedia](http://en.wikipedia.org/wiki/HEVC)

## Opus

An open source and royalty free audio codec, which is often used with VP8 or VP9 video in a webm (Matroska) file container, to create a wholly free audio media file.

[Opus on Wikipedia](https://en.wikipedia.org/wiki/Opus_(audio_format))

## PCM

**Pulse Code Modulation** is an uncompressed representation of audio samples, often at a bit depth of 16 or 24 bits, often as signed integers.

[Pulse-code_modulation on Wikipedia](http://en.wikipedia.org/wiki/Pulse-code_modulation)

### Byte order

Big Endian was the original memory byte ordering used on Motorola CPUs, and therefore was the original PCM byte ordering for Mac computers and others.

Little Endian was the original memory byte ordering used on Intel CPUs, and therefore was the original PCM byte ordering for PCs.

[Endianness on Wikipedia](http://en.wikipedia.org/wiki/Endianness)

## SMPTE VC-1

More widely known as just **VC-1**.

Video Codec number 1 is a Discreet Cosign Transform, DCT, based video codec which can encode interlaced or progressive video in various sizes form 176 × 144 to 2048 × 1536.

The original legacy "Windows Media" video formats, the WMV3 - WMV9 formats, create variants of the VC-1 video codec.

VC-1 has three profiles: simple, main and advanced; and each profile has different levels. Only the advanced profile includes the interlaced encoding we require for broadcast video. The profiles and levels are adequately described on the Wikipedia page.

VC-1 is one of the codecs available for Blu-ray disks.

[VC-1 on Wikipedia](http://en.wikipedia.org/wiki/VC-1)

Licensing fees may be payable to the MPEG LA when using VC-1.

[http://www.mpegla.com/main/programs/Vc1/Pages/Licensors.aspx](http://www.mpegla.com/main/programs/Vc1/Pages/Licensors.aspx)

## SMPTE VC-2

More widely known as **Dirac**.

Dirac is a wavelet based video codec capable of encoding interlaced video.
Dirac was developed by BBC Research and Development.

[VC-2 on Wikipedia](ttp://en.wikipedia.org/wiki/VC-2)

## SMPTE VC-3

More widely known as **DNxHD** with the **DNxHR** variant.

An intra-frame only video codec, most usually used as a mezzanine "editing friendly" codec.

[DNxHD on Wikipedia](http://en.wikipedia.org/wiki/DNxHD_codec)

### DNxHD encoding with FFmpeg

Minimal example:

```code
ffmpeg -i <INPUT> \
 -codec:v dnxhd \
 -flags +ildct \
 -b:v 185M \
 -c:a pcm_s16le \
 <OUTPUT>
```

The `-b:v` video bit-rate can take a variety of values: please refer to the bitrate column in the following table.

For interlaced modes you must include: `-flags +ildct`

For best quality mode, which is very slow, you can include: `-mbd rd`

Supported Resolutions:

|format|resolution name|frame size|bit depth|FPS|bit rate|
|:--|:--|:--|:--|:--|:--|
|1080p / 29.7|DNxHD 45|1920 x 1080|8|29.97|45M|
|1080p / 25|DNxHD 185|1920 x 1080|8|25|185M|
|1080p / 25|DNxHD 120|1920 x 1080|8|25|120M|
|1080p / 25|DNxHD 36|1920 x 1080|8|25|36M|
|1080p / 24|DNxHD 175|1920 x 1080|8|24|175M|
|1080p / 24|DNxHD 115|1920 x 1080|8|24|115M|
|1080p / 24|DNxHD 36|1920 x 1080|8|24|36M|
|1080p / 23.976|DNxHD 175|1920 x 1080|8|23.976|175M|
|1080p / 23.976|DNxHD 115|1920 x 1080|8|23.976|115M|
|1080p / 23.976|DNxHD 36|1920 x 1080|8|23.976|36M|
|1080i / 59.94|DNxHD 220|1920 x 1080|8|29.97|220M|
|1080i / 59.94|DNxHD 145|1920 x 1080|8|29.97|145M|
|1080i / 50|DNxHD 220|1920 x 1080|10|25|220M|
|1080i / 50|DNxHD 185|1920 x 1080|10|25|185M|
|1080i / 50|DNxHD 220|1920 x 1080|8|25|220M|
|1080i / 50|DNxHD 185|1920 x 1080|8|25|185M|
|1080i / 50|DNxHD 120|1920 x 1080|8|25|120M|
|720p / 59.94|DNxHD 220|1280x720|8|59.94|220M|
|720p / 59.94|DNxHD 145|1280x720|8|59.94|145M|
|720p / 50|DNxHD 175|1280x720|8|50|175M|
|720p / 50|DNxHD 115|1280x720|8|50|115M|
|720p / 23.976|DNxHD 90|1280x720|8|23.976|90M|
|720p / 23.976|DNxHD 60|1280x720|8|23.976|60M|

|description|bit depth|bit rate|
|:--|:--|:--|
|1920x1080p; bitrate: 175Mbps; pixel format: yuv422p10|10|175M|
|1920x1080p; bitrate: 185Mbps; pixel format: yuv422p10|10|185M|
|1920x1080p; bitrate: 365Mbps; pixel format: yuv422p10|10|365M|
|1920x1080p; bitrate: 440Mbps; pixel format: yuv422p10|10|440M|
|1920x1080p; bitrate: 115Mbps; pixel format: yuv422p|8|115M|
|1920x1080p; bitrate: 120Mbps; pixel format: yuv422p|8|120M|
|1920x1080p; bitrate: 145Mbps; pixel format: yuv422p|8|145M|
|1920x1080p; bitrate: 240Mbps; pixel format: yuv422p|8|240M|
|1920x1080p; bitrate: 290Mbps; pixel format: yuv422p|8|290M|
|1920x1080p; bitrate: 175Mbps; pixel format: yuv422p|8|175M|
|1920x1080p; bitrate: 185Mbps; pixel format: yuv422p|8|185M|
|1920x1080p; bitrate: 220Mbps; pixel format: yuv422p|8|220M|
|1920x1080p; bitrate: 365Mbps; pixel format: yuv422p|8|365M|
|1920x1080p; bitrate: 440Mbps; pixel format: yuv422p|8|440M|
|1920x1080i; bitrate: 185Mbps; pixel format: yuv422p10|10|185M|
|1920x1080i; bitrate: 220Mbps; pixel format: yuv422p10|10|220M|
|1920x1080i; bitrate: 120Mbps; pixel format: yuv422p|8|120M|
|1920x1080i; bitrate: 145Mbps; pixel format: yuv422p|8|145M|
|1920x1080i; bitrate: 185Mbps; pixel format: yuv422p|8|185M|
|1920x1080i; bitrate: 220Mbps; pixel format: yuv422p|8|220M|
|1440x1080i; bitrate: 120Mbps; pixel format: yuv422p|8|120M|
|1440x1080i; bitrate: 145Mbps; pixel format: yuv422p|8|145M|
|1280x720p; bitrate: 90Mbps; pixel format: yuv422p10|8|90M|
|1280x720p; bitrate: 180Mbps; pixel format: yuv422p10|10|180M|
|1280x720p; bitrate: 220Mbps; pixel format: yuv422p10|10|220M|
|1280x720p; bitrate: 90Mbps; pixel format: yuv422p|8|90M|
|1280x720p; bitrate: 110Mbps; pixel format: yuv422p|8|110M|
|1280x720p; bitrate: 180Mbps; pixel format: yuv422p|8|180M|
|1280x720p; bitrate: 220Mbps; pixel format: yuv422p|8|220M|
|1280x720p; bitrate: 60Mbps; pixel format: yuv422p|8|60M|
|1280x720p; bitrate: 75Mbps; pixel format: yuv422p|8|75M|
|1280x720p; bitrate: 120Mbps; pixel format: yuv422p|8|120M|
|1280x720p; bitrate: 145Mbps; pixel format: yuv422p|8|145M|
|1920x1080p; bitrate: 36Mbps; pixel format: yuv422p|8|36M|
|1920x1080p; bitrate: 45Mbps; pixel format: yuv422p|8|45M|
|1920x1080p; bitrate: 75Mbps; pixel format: yuv422p|8|75M|
|1920x1080p; bitrate: 90Mbps; pixel format: yuv422p|8|90M|
|1920x1080p; bitrate: 350Mbps; pixel format: yuv444p10, gbrp10|10|350M|
|1920x1080p; bitrate: 390Mbps; pixel format: yuv444p10, gbrp10|10|390M|
|1920x1080p; bitrate: 440Mbps; pixel format: yuv444p10, gbrp10|10|440M|
|1920x1080p; bitrate: 730Mbps; pixel format: yuv444p10, gbrp10|10|730M|
|1920x1080p; bitrate: 880Mbps; pixel format: yuv444p10, gbrp10|10|880M|
|960x720p; bitrate: 42Mbps; pixel format: yuv422p|8|42M|
|960x720p; bitrate: 60Mbps; pixel format: yuv422p|8|60M|
|960x720p; bitrate: 75Mbps; pixel format: yuv422p|8|75M|
|960x720p; bitrate: 115Mbps; pixel format: yuv422p|8|115M|
|1440x1080p; bitrate: 63Mbps; pixel format: yuv422p|8|63M|
|1440x1080p; bitrate: 84Mbps; pixel format: yuv422p|8|84M|
|1440x1080p; bitrate: 100Mbps; pixel format: yuv422p|8|100M|
|1440x1080p; bitrate: 110Mbps; pixel format: yuv422p|8|110M|
|1440x1080i; bitrate: 80Mbps; pixel format: yuv422p|8|80M|
|1440x1080i; bitrate: 90Mbps; pixel format: yuv422p|8|90M|
|1440x1080i; bitrate: 100Mbps; pixel format: yuv422p|8|100M|
|1440x1080i; bitrate: 110Mbps; pixel format: yuv422p|8|110M|

### DNxHR

A more modern variant of DNxHD which supports a wider range of resolutions and colour options, but can only be used with progressive material.

### DNxHR encoding with FFmpeg

Minimal example:

```code
ffmpeg \
 -i <INPUT> \
 -codec:v dnxhd \
 -profile:v dnxhr_hq \
 -codec:a pcm_s16le \
 <OUTPUT.MOV>
```

There's no need to specify bitrate, it will automatically use the correct one based on your input file.

Most DNxHR codec variants are only 8-bit, and will give an error if you try to convert to a different bit depth.

With DNxHR you need to specify the profile to use:

|profile|description|
|:--|:--|
|dnxhr_lb|LB (roughly equivalent to ProRes Proxy)|
|dnxhr_sq|SQ (roughly equivalent to ProRes LT)|
|dnxhr_hq|HQ (roughly equivalent to ProRes 422 and roughly equivalent to DNxHD)|
|dnxhr_hqx|HQX (roughly equivalent to ProRes 422HQ)|
|dnxhr_444|444 (you guessed it, roughly equivalent to ProRes 444)|

LB through HQX will always use 422 chroma subsampling.

444 will always use, well, 444 chroma subsampling.

Both the 444 and HQX variants use 10-bit colour in HD or less, and either 10 or 12-bit colour at 2K/UHD/4k resolutions.
Although FFmpeg won't actually encode to 12-bit DNxHR HQX/444 or ProRes 4444XQ, it only supports 10-bit color depth.

#### 10bit

If you wanted to transcode to HQX 10bit from a file with a different bit-depth/color-space, you would do something like:

```coode
ffmpeg \
 -i <INPUT> \
 -filter "scale=0:0,format=yuv422p10le" \
 -codec:v dnxhd \
 -profile:v dnxhr_hqx \
 -codec:a pcm_s16le \
 <OUTPUT.MOV>
```

To go to 10-bit 444, you would need to give it a 2K or larger resolution file and do:

```code
ffmpeg \
 -i <INPUT> \
 -filter "scale=0:0,format=rgb24" \
 -codec:v dnxhd \
 -profile:v dnxhr_444 \
 -codec:a pcm_s16le \
 <OUTPUT.MOV>
```

As before, you generally don't want to use 444 unless you're coming from a 444 color-space file, or a raw capture format that FFmpeg can understand.
Otherwise, it's a waste of storage space, and is much slower to encode.

Oh, and if you're wondering how the FFMPEG DNxHD/HR encoder compares to the equivalent presets in Adobe Media Encoder, know that it's very close, but just slightly softer.

## SMPTE VC-5

More widely known as **CineForm**.

CineForm is a Wavelet based video codec.

The CineForm Codec supports RAW camera formats such as Bayer patterns as well as RGB and YCrCb with an optional alpha channel and colour difference sub-sampling.

GoPro supported the efforts of SMPTE to adopt the codec as the VC-5 video compression standard.

[http://cineform.com/](http://cineform.com/)

[CineForm project at SMPTE](https://kws.smpte.org/kws/public/projects/project/details?project_id=15)

## Theora

Theora from Xiph.Org is a fork of On2's TrueMotion [VP3](#vp3) CODEC, which On2 released as open-source software in 2001.

Theora extends VP3 by adding 4:2:2 and 4:4:4 chroma sub-sampling for better colour quality, compared to the 4:2:0 chroma sub-sampling in VP3.

## VP3

VP3 was released as open-source by On2 in 2001.
It was released to Xiph.Org, who forked the CODEC to produce Theora.

## VP8

VP8 is a royalty free and open source video CODEC developed On2 Technologies, later acquired by Google and released under the Modified BSD License as a successor to VP7.

VP8 is mostly known as a web-streaming CODEC, used in Google's YouTube web site, and often seen in the WebM container.

[VP8 at Wikepedia](http://en.wikipedia.org/wiki/VP8)

## VP9

VP9 is a royalty free and open source video CODEC developed by Google and released under the Modified BSD License as a successor to [VP8](#vp8).

VP9 is mostly known as a web-streaming CODEC, used in Google's YouTube web site for some media, and often seen in the WebM container.

There have been suggestions that VP9 may be in the next Blu-ray standard.

[VP9 at Wikipedia](http://en.wikipedia.org/wiki/VP9)
