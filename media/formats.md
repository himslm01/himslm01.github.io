# Video and Audio Media Formats

A media format is a manufacturers specification of how to combine multiple streams of media encoded with specific parameters of a specific [media codec](codecs) into a specific [media container](containers).

## AVC Intra 100

**AVC-I 100** format files are either [MXF](containers#mxf) or [MOV](containers#quicktime-mov) files containing one stream of i-frame only [H.264 video] and zero or more streams of [PCM audio](codecs#pcm).

Minimal example of encoding with FFmpeg:

```code
ffmpeg  \
 -i <INPUT> \
 -filter:v "
   scale=1920:1080:interl=1,
   format=yuv422p10le,
   setparams=field_mode=tff:range=tv:color_primaries=bt709:color_trc=bt709:colorspace=bt709,
   setdar=dar=16/9" \
 -codec:v libx264 \
 -vb 100M \
 -g 1 \
 -tune psnr \
 -avcintra-class 100 \
 -flags +ilme+ildct \
 -codec:a pcm_s24le \
 -ar 48000 \
 -vtag ai12 \
 <OUTPUT>
```

* `scale=1920:1080:interl=1,format=yuv422p10le` is equivalent to `-pix_fmt yuv422p10le` but works with interlaced media
* `setparams=field_mode=tff` is equivalent to `-top 1`
* `setparams=range=tv` is equivalent to `-color_range 2`
* `setparams=color_primaries=bt709` is equivalent to `-color_primaries bt709`
* `setparams=color_trc=bt709` is equivalent to `-color_trc bt709`
* `setparams=colorspace=bt709` is equivalent to `-colorspace bt709`

## IMX

**IMX30**, **IMX40** and **IMX50** are tightly defined [MXF](containers#mxf) files containing one stream of [MPEG 2 video](codecs#mpeg-2-part-2) and one stream of [PCM audio](codecs#pcm) carrying four channels of audio.

* **IMX50** was the BBC's *standard definition* broadcast format of choice.
* **IMX30** was BBC News' *standard definition* broadcast format of choice.

### IMX30

Minimal example of encoding with FFmpeg:
assumes input media is 720 pixels wide by 608 pixels high, with 32 lines of VBI at the top of the video:

```code
ffmpeg -i <INPUT_FILE_720x608>
 -codec:v  mpeg2video \
 -b:v 30000k \
 -minrate 30000k \
 -maxrate 30000k \
 -bufsize 1200000 \
 -rc_init_occupancy 1200000 \
 -intra \
 -flags +ildct+low_delay \
 -intra_vlc 1 \
 -non_linear_quant 1 \
 -ps 1 \
 -qmin 1 \
 -qmax 3 \
 -dc 10 \
 -g 1 \
 -qscale 1
 -rc_max_vbv_use 1 \
 -rc_min_vbv_use 1 \
 -codec:a pcm_s16le \
 -ar 48000 \
 -ac 4\
 -color_range tv \
 -colorspace bt470bg \
 -color_primaries bt470bg \
 -color_trc gamma28 \
 -top 1 \
 -f mxf_d10
 <OUTPUT.MXF>
```

## XAVC

Sony use the term **XAVC** to describe a variety of media formats.

* XAVC - [H.264](codecs#mpeg-4-part-10) compressed video and [PCM](codecs#pcm) audio in an [MXF](containers#mxf) file container
  * XAVC-I - I-frame only video encoding, very high bitrate, and usually very high quality
  * XAVC-L - Long-GOP video encoding, lower bitrate and harder for software to decode
* XAVC H - [H.265](codecs#mpeg-h-part-2) compressed video and [PCM](codecs#pcm) audio in an [MXF](containers#mxf) file container
* XAVC S - Semi-pro [H.264](codecs#mpeg-4-part-10) compressed video and [PCM](codecs#pcm) or [AAC](codecs#mpeg-4-part-3) compressed audio in a [MP4](containers#mp4) file container
  * XAVC S-I - similar to XAVC-I
* XAVC HS - Semi-pro [H.265](codecs#mpeg-h-part-2) compressed video and [PCM](codecs#pcm) or [AAC](codecs#mpeg-4-part-3) compressed audio in a [MP4](containers#mp4) file container

[Full details of Sony's web site](https://assets.pro.sony.eu/Web/supportcontent/XAVC_Profiles_and_OperatingPoints_220.pdf)
