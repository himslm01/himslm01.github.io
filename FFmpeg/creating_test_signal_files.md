# Creating test signal files with FFmpeg

[FFmpeg](http://ffmpeg.org/) can create its own source media streams and can encode them into output files. In this guide I will create some output media files containing video colour bars and audio tones.

For more information on [FFmpeg](http://ffmpeg.org/) command lines please see the full list of [FFmpeg](http://ffmpeg.org/) command-line options: <http://ffmpeg.org/ffmpeg-all.html>

Note that this guide assumes that you have [FFmpeg](http://ffmpeg.org/) installed and is available in your default `$PATH`.

## Creating media streams

There are two ways to create media streams in [FFmpeg](http://ffmpeg.org/):

* within a `Libavfilter` chain
* as a virtual input source created from a `Libavfilter` fragment

I think the easiest concept to understand is as as an input source.

## `Libavfilter` Input source

In this method the [FFmpeg](http://ffmpeg.org/) command line tells it to create an input stream taking the specification as a `Libavfilter` filter chain fragment.

Lets dive straight into an example.

### Create a mono 440Hz sine wave

I'll start by creating a mono WAV file containing a standard test tone:

```console
ffmpeg -f lavfi -i "sine=f=440:r=48000" -t 10 -y tone-440Hz.wav
```

* `-f lavfi` : the immediately following input file name will be interpreted as a `Libavfilter` filter chain fragment
* `-i "sine=f=440:r=48000"` : the input filename to be interpreted as a `Libavfilter` filter chain fragment
  * `sine=f=440:r=48000` : use the `sine` audio source which generates a sine wave, with a frequency of 440Hz and a sample rate of 48KHz
* `-t 10` : stop encoding after 10 seconds
* `-y` : overwrite any output file if the filename already exists
* `tone-440Hz.wav` : the output filename

When that command is run then very quickly a .wav file will be created containing a mono 440Hz tone at -18dBFS.

Note that in this command there is no command-line parameter defining the audio codec. It is not needed because FFmpeg known that PCM audio is the default for a WAV file.

### Create HD colour bars

Now I'll create an MP4 file containing a standard colour bar pattern:

Note that the following commands will be too long to easily read on a single line, so I will split the command across multiple lines using the usual `\` character.

```console
ffmpeg \
  -f lavfi -i "smptehdbars=s=1920x1080:r=25,format=yuv420p" \
  -c:v libx264 -b:v 3500k -crf:v 23 \
  -t 10 -y bars-smptehd.mp4`
```

* `-f lavfi` : the immediately following input file name will be interpreted as a `Libavfilter` filter chain fragment
* `-i "smptehdbars=s=1920x1080:r=25,format=yuv420p"` : the input filename to be interpreted as a `Libavfilter` filter chain fragment
  * `smptehdbars=s=1920x1080:r=25,format=yuv420p` : use the `smptehdbars` video source which generates a color bars pattern, based on the SMPTE RP 219-2002, with a picture size of 1920 wide and 1080 high, at a frame rate of 25 frames per second and in a yuv-420 format
* `-c:v libx264 -b:v 3500k -crf:v 23` : encode the video to create h.264 video at no-more than 3500 kilo-bits per second and aiming for the quality of 23 using the constant-rate-factor constrained-quality algorithm
* `-t 10` : stop encoding after 10 seconds
* `-y` : overwrite any output file if the filename already exists
* `bars-smptehd.wav` : the output filename

When that command is run then quite quickly a .mp4 file will be created containing an h.264 video track of SMPTE colour bars.

### Create HD colour bars and stereo tone

The final example for the input source method will combine the two commands above, creating a file with colour-bars and a stereo tone audio track.

```console
ffmpeg \
  -f lavfi -i "sine=f=440:r=48000" \
  -f lavfi -i "sine=f=523:r=48000" \
  -f lavfi -i "smptehdbars=s=1920x1080:r=25,format=yuv420p" \
  -filter_complex "[0:a][1:a]amerge=inputs=2[aout]" \
  -map 2:v \
  -map "[aout]" \
  -c:v libx264 -b:v 3500k -crf:v 23 \
  -c:a libfdk_aac -b:a 192k \
  -t 10 -y "bars+tone-smptehd+stereo.mp4"
```

* `-f lavfi -i "sine=f=440:r=48000"` : create an audio stream as above - 440Hz sine wave
* `-f lavfi -i "sine=f=523:r=48000"` : create an audio stream as above - 523Hz sine wave
* `-f lavfi -i "smptehdbars=s=1920x1080:r=25,format=yuv420p"` - create a video stream as above - SMPTE colour bars at a size of 1920x1080 at 25 FPS in a yuv-420 format
* `-filter_complex "[0:a][1:a]amerge=inputs=2[aout]"` : merge the two audio streams (called `[0:a]` and `[1:a]`) into one stereo stream called `[aout]`
* `-map 2:v` : map the video of our colour-bars stream into the output file
* `-map "[aout]"` : map the stereo audio stream into the output file
* `-c:v libx264 -b:v 3500k -crf:v 23` : encode the video as above - h.264 constrained quality
* `-c:a libfdk_aac -b:a 192k` : encode the audio to create AAC at 192 kilo-bits per second
* `-t 10` : stop encoding after 10 seconds
* `-y` : overwrite any output file if the filename already exists
* `bars+tone-smptehd+stereo.mp4` : the output filename

When that command is run then quite quickly a .mp4 file will be created containing an h.264 video track of SMPTE colour bars and an AAC audio track of stereo audio sine wave tones.

## `Libavfilter` chain

That last command ended up including four fragments of `Libavfilter` chain - three defining the input streams and one merging the two input audio streams into one stereo stream. It's at this amount of complexity that I feel it's worth changing tack and doing the whole job in one complex filter chain.

### Create HD colour bars and stereo tone again

I shall make exactly the same file as above, but this time using the `Libavfilter` chain method:

```console
ffmpeg \
  -filter_complex "sine=f=440:r=48000[a0],sine=f=523:r=48000[a1],[a0][a1]amerge=inputs=2[aout],smptehdbars=s=1920x1080:r=25,format=yuv420p[vout]" \
  -map "[vout]" \
  -map "[aout]" \
  -c:v libx264 -b:v 3500k -crf:v 23 \
  -c:a libfdk_aac -b:a 192k \
  -t 10 -y "bars+tone-smptehd+stereo.mp4"
```

Almost everything in that [FFmpeg](http://ffmpeg.org/) command line should look familiar from the previous examples, but some of the parts of the command are in an unfamiliar location:

* `-filter_complex "sine=f=440:r=48000[a0],sine=f=523:r=48000[a1],[a0][a1]amerge=inputs=2[aout],smptehdbars=s=1920x1080:r=25,format=yuv420p[vout]"` : the complex filter chain creating two audio streams, merging the audio streams into a stereo stream, and creating the video stream.<br />Note that a complex filter ia separated into fragments by a `,` so I can break the long complex filter into 4 separate fragments:
  * `sine=f=440:r=48000[a0]` : create an audio stream as above - 440Hz sine-wave tone - and call the stream `[a0]`
  * `sine=f=523:r=48000[a0]` : create an audio stream as above - 523Hz sine-wave tone - and call the stream `[a1]`
  * `[a0][a1]amerge=inputs=2[aout]` : merge the two audio streams as above - into one stereo stream called `[aout]`
  * `smptehdbars=s=1920x1080:r=25,format=yuv420p[vout]` : create a video stream as above - SMPTE colour bars at a size of 1920x1080 at 25 FPS in a yuv-420 format - and call the stream `[vout]`
* `-map "[vout]"` : map the video stream into the output file
* `-map "[aout]"` : map the stereo audio stream into the output file
* `-c:v libx264 -b:v 3500k -crf:v 23` : encode the video as above - h.264 constrained quality 3.5Mbps
* `-c:a libfdk_aac -b:a 192k` : encode the audio as above - AAC at 192Kbps
* `-t 10` : stop encoding after 10 seconds
* `-y` : overwrite any output file if the filename already exists
* `bars+tone-smptehd+stereo.mp4` : the output filename

When that command is run then quite quickly a .mp4 file will be created containing an h.264 video track of SMPTE colour bars and an AAC audio track of stereo audio sine wave tones - exactly the same as the previous command.

## Conclusion

I shall leave it to you to decide when to use the `Libavfilter` input source method and when to use the `Libavfilter` chain method.
