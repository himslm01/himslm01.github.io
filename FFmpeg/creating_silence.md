# Creating silence with FFmpeg

Note that the following commands will be too long to easily read on a single line, so I will split the command across multiple lines using the usual `\` character.

## The old way to Create a never-ending mono stream of silence

The old way to create silence is to say the input file is of a format of raw-uncompressed audio and read from /dev/zero.
This creates an infinitely long audio stream.

```text
ffmpeg \
-ar 48000 -ac 1 -f s16le -i /dev/zero \
-t 10 -y silence.wav
```

* `-ar 48000` set the sample rate to 48KHz
* `-ac 1` one audio channel
* `-f s16le` interpret the source as uncompressed 16 bit pcm audio
* `-i /dev/zero` read from `/dev/zero`
* `-t 10` stop encoding after 10 seconds
* `-y` overwrite any output file if the filename already exists
* `silence.wav` the output filename

When adding that to a video source to an infinitely long audio stream you need to stop reading the silence when when the video source stops.

The usual way to do that is to use the [`-shortest` output command-line option](https://ffmpeg.org//ffmpeg-all.html#toc-Advanced-options).

```text
-shortest
```

If you know length of the source video you could use the [-frames output command-line option](https://ffmpeg.org//ffmpeg-all.html#toc-Advanced-options), or the [-t output command-line option](https://ffmpeg.org//ffmpeg-all.html#Main-options).

```text
-t 60000ms
```

Duration can be in seconds or other [FFmpeg time formats](https://ffmpeg.org//ffmpeg-utils.html#time-duration-syntax)

## The current way to create a never-ending mono stream of silence

Reading from /dev/zero is a bit of a 'caveman' approach.

The filter source `aevalsrc` can create audio from a mathmatical formula.

Creating silence is simple.

```text
-f lavfi -i "aevalsrc=0:s=48000:n=1920" \
-shortest
```

- s = sample rate (defaults to 44,100)
- n = number of samples per block (defaults to 1024)

It might be better to create one audio block per frame - which is easy for UK frame rates:

n = sample_rate ÷ frame_rate

for example:

- 48000 ÷ 25 = 1920
- 48000 ÷ 50 = 960

## Create a time bound mono stream of silence

If you are adding silence to a video source, and you know length of the source video you could use the `duration` option to the `aevalsrc` filter source.

```text
-f lavfi -i "aevalsrc=0:n=1920:s=48000:d=60.0"
```

- d = duration

duration can be in seconds or other [FFmpeg time formats](https://ffmpeg.org//ffmpeg-utils.html#time-duration-syntax)

for example:

- 60.0
- 60000ms

## Multiple streams of time bound mono stream of silence

If you need multiple streams - just create multiple inputs.

```text
-f lavfi -i "aevalsrc=0:n=1920:s=48000:d=60.0" \
-f lavfi -i "aevalsrc=0:n=1920:s=48000:d=60.0" \
-map 0:a \
-map 1:a \
```

## Do it in a fliter instead

Alternatively, especially if you are going to use `-filter_complex` in your full command-line, you can 'just' make a stream in the filter, and use other filter commands to manipulate the stream.

```text
-filter_complex "aevalsrc=0:n=1920:s=48000:d=60000ms,asplit=4[a0][a1][a2][a3]" \
-map "[a0]" \
-map "[a1]" \
-map "[a2]" \
-map "[a3]" \
```
