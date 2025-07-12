# Converting broadcast video media for inclusion into HTML graphics

Using HTML graphics in broadcast productions is becoming a big thing.

Often the device playing the HTML graphics is essentially an embedded web browser, with super clever code around it to control the rendering of the HTML graphics and outputting the HTML graphics to broadcast production standards. Often the embedded web browser is Chrome, using the [Chrome Embedded Framework](https://en.wikipedia.org/wiki/Chromium_Embedded_Framework), [CEF](https://en.wikipedia.org/wiki/Chromium_Embedded_Framework).

Sometimes the HTML graphics also need to play moving video files, for animating components or for full-frame backgrounds behind the HTML graphics.

It is very likely that the embedded web browser cannot play broadcast media files. I cannot imaging playing in a web browser [MXF](/media/containers#mxf) files containing AVCI-100, or [MOV](/media/containers#quicktime-mov) files containing ProRes422.

Converting the broadcast video media files to a canonical format that web browsers can play natively will be many HTML graphics designers goal. But they will need to achieve that goal while retaining as much quality as possible, within the limits of web browser technology, and with as little cost as possible.

[FFmpeg](https://en.wikipedia.org/wiki/FFmpeg) is a free, open source, video media conversion tool.

[VP9](/media/codecs#vp9) is a free, open source, royalty-free video media codec which is designed for playing video media in modern web browsers.

[WebM](/media/containers#webm) is a free, open source, file format which is designed to hold [VP9](https://en.wikipedia.org/wiki/VP9) encoded video.

## Conversion command lines using FFmpeg

Here are some example [FFmpeg](/FFmpeg) commands which convert broadcast (or other) video media files into [VP9](/media/codecs#vp9) encoded video in [WebM](/media/containers#webm) format files, which [CEF](https://en.wikipedia.org/wiki/Chromium_Embedded_Framework) plays with no external additional codecs.

This media plays very nicely in [CasparCG](https://casparcg.com/) and [OBS Studio](https://obsproject.com/), to name only two possible systems for using HTML graphics in a broadcast production.

### Progressive source media

```bash
ffmpeg -loglevel debug \
 -i input.mxf \
 -codec:v libvpx-vp9 \
 -crf 6 \
 -b:v 0 \
 -filter:v "
    scale=0:0:interl=0:out_color_matrix=bt709:out_range=tv:flags=lanczos,
    format=yuv420p10le" \
 -an \
 -y \
 output.webm
```

### Interlaced source media

```bash
ffmpeg -loglevel debug \
 -i input.mov \
 -codec:v libvpx-vp9 \
 -crf 6 \
 -b:v 0 \
 -filter:v "
    scale=0:0:interl=1:out_color_matrix=bt709:out_range=tv:flags=lanczos,
    format=yuv422p10le,
    w3fdif,
    scale=0:0:interl=0:out_color_matrix=bt709:out_range=tv:flags=lanczos,
    format=yuv420p10le" \
 -an \
 -y \
 output.webm
```

## Notes

The example commands above assume the output media needs to be created that is the same size as the input media. If that is not the case then the `scale` filters will need to be altered, with the possibly addition of other `scale`, `crop` and `pad` filters.
