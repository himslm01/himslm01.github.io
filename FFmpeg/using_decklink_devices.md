# Using BlackMagic Design Decklink devices with FFmpeg

I build my own versions of FFmpeg. I build both static and shared library versions. MOsst of the time I use the static version, but for this I must use the shared library version, so that it will use the shared BlackMagic Design Decklink shared libraries.

To help me use the shared library version of FFmpeg I have a small shell script I save into `$HOME/bin/ffmpeg_shared`:

```bash
#!/bin/sh

LD_LIBRARY_PATH=$HOME/Source/ff_builder/build/linux_x86_64/ffmpeg_shared/lib/lib/ \
    $HOME/Source/ff_builder/build/linux_x86_64/ffmpeg_shared/bin/ffmpeg \
    "$@"
````

## List Decklink sources

```console
$ ffmpeg_shared -sources decklink
ffmpeg version N-96958-g8f22257-shared_linux_x86_64_202003141011 Copyright (c) 2000-2020 the FFmpeg developers
  built with gcc 5.4.0 (Ubuntu 5.4.0-6ubuntu1~16.04.12) 20160609
  configuration: --extra-version=shared_linux_x86_64_202003141011 --extra-cflags=' ' --extra-libs=' -lpthread -lm' --pkg-config-flags= --cross-prefix= --arch=x86_64 --target-os=linux --prefix=/opt/ffbuild --enable-gpl --enable-version3 --enable-nonfree --disable-ffplay --disable-dxva2 --enable-libxml2 --enable-openssl --enable-libsrt --enable-libmp3lame --enable-libspeex --enable-libtheora --enable-libvorbis --enable-libopus --enable-libxvid --enable-libvpx --enable-libfdk-aac --enable-libx264 --enable-libx265 --enable-libopenjpeg --enable-libaom --enable-decklink
  libavutil      56. 42.100 / 56. 42.100
  libavcodec     58. 75.100 / 58. 75.100
  libavformat    58. 41.100 / 58. 41.100
  libavdevice    58.  9.103 / 58.  9.103
  libavfilter     7. 77.100 /  7. 77.100
  libswscale      5.  6.101 /  5.  6.101
  libswresample   3.  6.100 /  3.  6.100
  libpostproc    55.  6.100 / 55.  6.100
Auto-detected sources for decklink:
  75:0a4aa32e:00000000 [Intensity Pro 4K]
```

Or

```console
$ ffmpeg_shared -sources decklink
ffmpeg version N-96920-ge6c5329-shared_linux_x86_64_202003092300 Copyright (c) 2000-2020 the FFmpeg developers
  built with gcc 5.4.0 (Ubuntu 5.4.0-6ubuntu1~16.04.12) 20160609
  configuration: --extra-version=shared_linux_x86_64_202003092300 --extra-cflags=' ' --extra-libs=' -lpthread -lm' --pkg-config-flags= --cross-prefix= --arch=x86_64 --target-os=linux --prefix=/opt/ffbuild --enable-gpl --enable-version3 --enable-nonfree --disable-ffplay --disable-dxva2 --enable-libxml2 --enable-openssl --enable-libsrt --enable-libmp3lame --enable-libspeex --enable-libtheora --enable-libvorbis --enable-libopus --enable-libxvid --enable-libvpx --enable-libfdk-aac --enable-libx264 --enable-libx265 --enable-libopenjpeg --enable-libaom --enable-decklink
  libavutil      56. 42.100 / 56. 42.100
  libavcodec     58. 74.100 / 58. 74.100
  libavformat    58. 41.100 / 58. 41.100
  libavdevice    58.  9.103 / 58.  9.103
  libavfilter     7. 77.100 /  7. 77.100
  libswscale      5.  6.100 /  5.  6.100
  libswresample   3.  6.100 /  3.  6.100
  libpostproc    55.  6.100 / 55.  6.100
Auto-detected sources for decklink:
  46:00000000:002e0a00 [DeckLink SDI (1)]
  46:00000000:002e0900 [DeckLink SDI (2)]
```

### List formats for capture

```console
$ ffmpeg_shared -f decklink -f decklink -list_formats 1 -i "DeckLink SDI (2)"
ffmpeg version N-96920-ge6c5329-shared_linux_x86_64_202003092300 Copyright (c) 2000-2020 the FFmpeg developers
  built with gcc 5.4.0 (Ubuntu 5.4.0-6ubuntu1~16.04.12) 20160609
  configuration: --extra-version=shared_linux_x86_64_202003092300 --extra-cflags=' ' --extra-libs=' -lpthread -lm' --pkg-config-flags= --cross-prefix= --arch=x86_64 --target-os=linux --prefix=/opt/ffbuild --enable-gpl --enable-version3 --enable-nonfree --disable-ffplay --disable-dxva2 --enable-libxml2 --enable-openssl --enable-libsrt --enable-libmp3lame --enable-libspeex --enable-libtheora --enable-libvorbis --enable-libopus --enable-libxvid --enable-libvpx --enable-libfdk-aac --enable-libx264 --enable-libx265 --enable-libopenjpeg --enable-libaom --enable-decklink
  libavutil      56. 42.100 / 56. 42.100
  libavcodec     58. 74.100 / 58. 74.100
  libavformat    58. 41.100 / 58. 41.100
  libavdevice    58.  9.103 / 58.  9.103
  libavfilter     7. 77.100 /  7. 77.100
  libswscale      5.  6.100 /  5.  6.100
  libswresample   3.  6.100 /  3.  6.100
  libpostproc    55.  6.100 / 55.  6.100
[decklink @ 0x35ea240] Supported formats for 'DeckLink SDI (2)':
    format_code    description
    ntsc        720x486 at 30000/1001 fps (interlaced, lower field first)
    nt23        720x486 at 24000/1001 fps
    pal         720x576 at 25000/1000 fps (interlaced, upper field first)
    23ps        1920x1080 at 24000/1001 fps
    24ps        1920x1080 at 24000/1000 fps
    Hp25        1920x1080 at 25000/1000 fps
    Hp29        1920x1080 at 30000/1001 fps
    Hp30        1920x1080 at 30000/1000 fps
    Hi50        1920x1080 at 25000/1000 fps (interlaced, upper field first)
    Hi59        1920x1080 at 30000/1001 fps (interlaced, upper field first)
    Hi60        1920x1080 at 30000/1000 fps (interlaced, upper field first)
    hp50        1280x720 at 50000/1000 fps
    hp59        1280x720 at 60000/1001 fps
    hp60        1280x720 at 60000/1000 fps
DeckLink SDI (2): Immediate exit requested
```

or

```console
$ ffmpeg_shared -f decklink -f decklink -list_formats 1 -i "Intensity Pro 4K"
ffmpeg version N-96958-g8f22257-shared_linux_x86_64_202003141011 Copyright (c) 2000-2020 the FFmpeg developers
  built with gcc 5.4.0 (Ubuntu 5.4.0-6ubuntu1~16.04.12) 20160609
  configuration: --extra-version=shared_linux_x86_64_202003141011 --extra-cflags=' ' --extra-libs=' -lpthread -lm' --pkg-config-flags= --cross-prefix= --arch=x86_64 --target-os=linux --prefix=/opt/ffbuild --enable-gpl --enable-version3 --enable-nonfree --disable-ffplay --disable-dxva2 --enable-libxml2 --enable-openssl --enable-libsrt --enable-libmp3lame --enable-libspeex --enable-libtheora --enable-libvorbis --enable-libopus --enable-libxvid --enable-libvpx --enable-libfdk-aac --enable-libx264 --enable-libx265 --enable-libopenjpeg --enable-libaom --enable-decklink
  libavutil      56. 42.100 / 56. 42.100
  libavcodec     58. 75.100 / 58. 75.100
  libavformat    58. 41.100 / 58. 41.100
  libavdevice    58.  9.103 / 58.  9.103
  libavfilter     7. 77.100 /  7. 77.100
  libswscale      5.  6.101 /  5.  6.101
  libswresample   3.  6.100 /  3.  6.100
  libpostproc    55.  6.100 / 55.  6.100
[decklink @ 0x4ded240] Supported formats for 'Intensity Pro 4K':
    format_code    description
    ntsc        720x486 at 30000/1001 fps (interlaced, lower field first)
    pal         720x576 at 25000/1000 fps (interlaced, upper field first)
    23ps        1920x1080 at 24000/1001 fps
    24ps        1920x1080 at 24000/1000 fps
    Hp25        1920x1080 at 25000/1000 fps
    Hp29        1920x1080 at 30000/1001 fps
    Hp30        1920x1080 at 30000/1000 fps
    Hp50        1920x1080 at 50000/1000 fps
    Hp59        1920x1080 at 60000/1001 fps
    Hp60        1920x1080 at 60000/1000 fps
    Hi50        1920x1080 at 25000/1000 fps (interlaced, upper field first)
    Hi59        1920x1080 at 30000/1001 fps (interlaced, upper field first)
    Hi60        1920x1080 at 30000/1000 fps (interlaced, upper field first)
    hp50        1280x720 at 50000/1000 fps
    hp59        1280x720 at 60000/1001 fps
    hp60        1280x720 at 60000/1000 fps
    4k23        3840x2160 at 24000/1001 fps
    4k24        3840x2160 at 24000/1000 fps
    4k25        3840x2160 at 25000/1000 fps
    4k29        3840x2160 at 30000/1001 fps
    4k30        3840x2160 at 30000/1000 fps
```

## Play

### Big Buck Bunny to Intensity Pro 4K at 1920x1080p50

```console
$ ffmpeg_shared \
  -i big_buck_bunny_1080p_h264.mov \
  -filter:v scale=1920:1080:interl=0,format=yuv422p,framerate=50 \
  -c:v v210 \
  -c:a pcm_s16le -ac 2 -ar 48000 \
  -f decklink "Intensity Pro 4K"
```

### From NewsChannel iPlayer DASH stream to Decklink

```console
$ ffmpeg_shared \
  -i https://vs-cmaf-push-uk-live.akamaized.net/x=3/i=urn:bbc:pips:service:bbc_news_channel_hd/pc_hd_abr_v2.mpd \
  -map 0:5 -map 0:6 \
  -filter:v "scale=1920:1080:interl=0:sws_flags=lanczos,format=yuv422p,framerate=50" \
  -c:v v210 \
  -c:a pcm_s16le -ar 48000 -ac 2 \
  -f decklink "Intensity Pro 4K"
```

### From YouTube through FFmpeg to Decklink

```console
$ ffmpeg_shared \
  $(youtube-dl -g "https://www.youtube.com/watch?v=dQw4w9WgXcQ" \
  | while read -d $'\n' file; do echo -n "-i ${file} "; done) \
  -filter:v "scale=1920:1080:interl=0:sws_flags=lanczos,format=yuv422p,framerate=50" \
  -c:v v210 \
  -c:a pcm_s16le -ar 48000 -ac 2 \
  -f decklink "Intensity Pro 4K"
```

### From YouTube through FFmpeg to GStreamer to Decklink

```console
$ ffmpeg \
 $(youtube-dl -g "https://www.youtube.com/watch?v=u-OXc_ltf_M" \
  | while read -d $'\n' file; do echo -n "-i \"${file}\" "; done) \
 -c:v copy -c:a copy -f matroska - \
  | gst-launch-1.0 -v fdsrc fd=0 \
   ! decodebin name=bin \
   bin. ! autovideoconvert ! autovideoconvert ! videorate ! videoscale \
   ! decklinkvideosink mode=14 video-format=2 \
   bin. ! audioconvert ! decklinkaudiosink
```

### From YouTube through GStreamer to Decklink

```console
gst-launch-1.0 -v \
   souphttpsrc "location=$(youtube-dl -g -f bestvideo 'https://www.youtube.com/watch?v=u-OXc_ltf_M')" \
   ! decodebin ! autovideoconvert ! autovideoconvert ! videorate ! videoscale \
   ! decklinkvideosink mode=14 video-format=2 \
   souphttpsrc "location=$(youtube-dl -g -f bestaudio 'https://www.youtube.com/watch?v=u-OXc_ltf_M')" \
   ! decodebin ! audioconvert \
   ! decklinkaudiosink
```

## SRT

### Capture from Decklink and stream to srt

```console
$ ffmpeg_shared -re \
  -channels 2 -format_code Hi50 -f decklink -i "DeckLink SDI (1)" \
  -map 0:0 -map 0:1 \
  -filter:v "scale=0:0:interl=1,format=yuv420p" \
  -c:v libx264 -crf:v 28 -preset:v ultrafast -profile:v baseline -tune:v zerolatency \
  -flags +ilme+ildct -top 1 \
  -force_key_frames "expr:eq(mod(n,50),0)" \
  -x264opts rc-lookahead=50:keyint=100:min-keyint=50 \
  -c:a aac -b:a 128k -ar 48000 -ac 2 \
  -f mpegts "srt://0.0.0.0:1364?latency=9000&maxbw=1638400&mss=1360&mode=listener"
```

#### SRT settings

* 0.0.0.0:1364 :: all interfaces on random port
* latency=90000 :: one second in mpegts timestamp counts (90Khz)
* maxbw=1638400 :: 12,500,000 bits/second = 1,638,400 bytes/second
* mss=1360 :: I know my network's MSS
* mode=listener :: listen for connection from client

#### Set GOP size

<https://superuser.com/questions/908280/what-is-the-correct-way-to-fix-keyframes-in-ffmpeg-for-dash>

```console
  -g 50 \
  -force_key_frames "expr:gte(t,n_forced*2)" \
  -force_key_frames "expr:eq(mod(n,50),0)" -x264opts rc-lookahead=50:keyint=100:min-keyint=50 \
```

### From srt stream play to Decklink

Assume the srt stream is being created on `10.11.12.13`:

```console
$ ffmpeg_shared -re \
  -i "srt://10.11.12.13:1364" \
  -filter:v "scale=1920:1080:interl=1:sws_flags=lanczos,format=yuv422p,w3fdif,framerate=50" \
  -c:v v210 \
  -c:a pcm_s16le -ar 48000 -ac 2 \
  -f decklink "Intensity Pro 4K"
```

### From srt stream create simple UDP multicast stream

```console
$ ffmpeg_shared -re \
  -i "srt://10.11.12.13:1364" \
  -c:v copy \
  -c:a copy \
  -strict -2 \
  -f mpegts udp://239.0.0.2:1234?pkt_size=1316
```

### From UDP stream play to Decklink

```console
$ ffmpeg_shared -re \
  -i "udp://239.0.0.2:1234?pkt_size=1316" \
  -filter:v "scale=1920:1080:interl=1:sws_flags=lanczos,format=yuv422p,w3fdif,framerate=50" \
  -c:v v210 \
  -c:a pcm_s16le -ar 48000 -ac 2 \
  -f decklink "Intensity Pro 4K"
```

### Can also play that back in CasparCG

```console
LOADGB 1-2 udp://239.0.0.2:1234
```
