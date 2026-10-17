# Converting Standard Definition Broadcast Media Files

Converting all Standard Definition (SD) broadcast media files takes extra thought.
This is due to several issues, including:

* All SD broadcast media files have ***anamorphic pixels***
* Most SD broadcast media files have picture content on the left had side and the right hand side of the encoded picture which is not intended to be seen - it is outside of the ***active picture area***
* Some SD broadcast media has half a line of missing or repurposed pixels at the top and the bottom of the active picture area

I explain the reasons for these issues in detail in my page on [SD broadcast media aspect ratios](/broadcast_media/broadcast_tv_SD_aspect_ratios).

See my page on [broadcast media pixel sizes](/broadcast_media/broadcast_media_pixel_sizes) to familiarise yourself on the usual pixel dimensions of broadcast SD media files.

Before you continue, ensure you understand the following concepts:

* the ***active pixel area*** of Standard Definition media files - read the pages I have linked above if you are in any doubt
* ***Interlaced*** and ***Progressive*** media
* colour video ***pixel formats*** such as `YUV422` and `YUV420`

## Converting to square pixel formats for non-broadcast use

### Assumptions

* the examples will demonstrate using FFmpeg's default `scale` video filter.
Other scale filters are available, but they require specific compilation options and may not be available in all versions of FFmpeg.
I would suggest looking at using the [zscale](https://ffmpeg.org/ffmpeg-all.html#zscale-1).
* the examples focus on SD media files which are 720x576 pixels and 720x480 pixels, which are the most usual SD media file sizes.
As described in the [broadcast media pixel sizes](/broadcast_media/broadcast_media_pixel_sizes) page, SD media files can have other pixel dimensions and need different processing.

### Options

You have three main options when converting SD media files to a square pixel formats:

1. keep all of the pixels from all of the lines from the original encoded video file including pixels outside of the active picture area, which may leave some black or unrelated content on the left and right of the converted video, and parts of the top line and bottom line of the converted video
2. keep all of the active pixels from all of the lines from the original encoded video file, removing the extra pixels from the left and the right edges of the original video, but potentially leaving some black or unrelated content in parts of the top line and bottom line of the converted video
3. keep all of the active pixels from most of the lines from the original encoded video file, removing the extra pixels from the left and the right edges and removing the first and last line of the original video which might have contained some black or unrelated content - for interlaced media this will mean the converted video is field reversed

Option 2 is the option most commonly used conversion.

Notes:

* SD IMX media files of 720x608 or 720x512 pixels need the same processing but also crop the top 32 or 26 lines respectively
* other SD media file sizes like 704x576 do not include any picture content on the left or right hand sides of the ***active picture area***.

### Progressive SD source media

#### Progressive 16:9 source media (720x576p) - option 1

```sh
ffmpeg \
 -i $INPUT_FILE \
 -vf "scale=1048:576:interl=0:flags=lanczos" \
 ... # other encoding options
 $OUTPUT_FILE
```

#### Progressive 4:3 source media (720x576p) - option 1

```sh
ffmpeg \
 -i $INPUT_FILE \
 -vf "scale=788:576:interl=0:flags=lanczos" \
 ... # other encoding options
 $OUTPUT_FILE
```

#### Progressive 16:9 source media (720x576p) - option 2

```sh
ffmpeg \
 -i $INPUT_FILE \
 -vf "scale=1048:576:interl=0:flags=lanczos,
      crop=1024:576:12:0" \
 ... # other encoding options
 $OUTPUT_FILE
```

#### Progressive 4:3 source media (720x576p) - option 2

```sh
ffmpeg \
 -i $INPUT_FILE \
 -vf "scale=788:576:interl=0:flags=lanczos,
      crop=768:576:10:0" \
 ... # other encoding options
 $OUTPUT_FILE
```

#### Progressive 16:9 source media (720x576p) - option 3

```sh
ffmpeg \
 -i $INPUT_FILE \
 -vf "scale=1048:576:interl=0:flags=lanczos,
      crop=1024:574:12:1" \
 ... # other encoding options
 $OUTPUT_FILE
```

Note that this conversion command, as it stands, will not work for original media files which have the video encoded in a `YUV420` format, where chroma samples are shared between two lines.

For original media files which have the video encoded in a `YUV420` format the chroma must be converted into a format where each line's chroma is independent, such as `YUV422`.

```sh
ffmpeg \
 -i $INPUT_FILE \
 -vf "scale=1048:576:interl=0:flags=lanczos,
      format=yuv422p,
      crop=1024:574:12:1,
      scale=iw:ih:interl=0:flags=lanczos,
      format=yuv420p" \
 ... # other encoding options
 $OUTPUT_FILE
```

#### Progressive 4:3 source media (720x576p) - option 3

```sh
ffmpeg \
 -i $INPUT_FILE \
 -vf "scale=788:576:interl=0:flags=lanczos,
      crop=768:574:10:1" \
 ... # other encoding options
 $OUTPUT_FILE
```

Note that this conversion command, as it stands, will not work for original media files which have the video encoded in a `YUV420` format, where chroma samples are shared between two lines.

For original media files which have the video encoded in a `YUV420` format the chroma must be converted into a format where each line's chroma is independent, such as `YUV422`.

```sh
ffmpeg \
 -i $INPUT_FILE \
 -vf "scale=788:576:interl=0:flags=lanczos,
      format=yuv422p,
      crop=768:574:12:1,
      scale=iw:ih:interl=0:flags=lanczos,
      format=yuv420p" \
 ... # other encoding options
 $OUTPUT_FILE
```

### Interlaced source media

Since our converted output file is not for use in a broadcast environment it has no business being interlaced.
The compromise of interlacing video media was only required for broadcast.
The best bet is to de-interlace the original media during the conversion to create progressive media at double the original file's frame rate.

The following commands use the video de-interlacing filter invented by BBC Research and Development and originally coded and added to FFmpeg's code by me. This video de-interlacing filter is called [`w3fdif`](https://git.ffmpeg.org/gitweb/ffmpeg.git/blob_plain/refs/heads/master:/libavfilter/vf_w3fdif.c).

#### Interlaced 16:9 source media (720x576i) - option 2

```sh
ffmpeg \
 -i $INPUT_FILE \
 -vf "scale=iw:ih:interl=1:flags=lanczos,
      format=yuv422p,
      w3fdif,
      scale=1048:576:interl=0:flags=lanczos,
      crop=1024:576:12:0" \
 ... # other encoding options
 $OUTPUT_FILE
```

Note that the `w3fdif` video de-interlacing filter works best with source video which has chroma that is independent on each line, therefore the source video is converted to `YUV422` format before any processing starts.
