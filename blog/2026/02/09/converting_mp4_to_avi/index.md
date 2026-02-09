# Converting Seestar S50 .mp4 video for use in AstroSurface

Today I wanted to use [AstroSurface] to stack and process the frames from a video I'd previously recorded with my [Seestar S50][SeestarS50]. I needed to convert the `.mp4` recording to a `.avi` file which [AstroSurface] could load.

I am a big fan of using FFmpeg for video file manipulation.

I decided that the best idea was to use FFmpeg to:

* decode the original `h.264` encoded video
* convert the original `yuv420` format into 8 bits-per-channel BGR format
* store the resultant uncompressed video into a `.avi` file

After some research and fiddling I believe that this is a reasonable command for the conversion:

```console
ffmpeg \
 -i 2024-08-16-220407-Lunar.mp4 \
 -filter 'scale=iw:ih:sws_flags=bitexact+full_chroma_int+accurate_rnd+lanczos,format=bgr24' \
 -c:v rawvideo \
 -map 0:v \
 2024-08-16-220407-Lunar.avi
```

* `-i '<FILENAME>'` the source recording from the [Seestar S50][SeestarS50]
* `-filter '<FILTER>'` convert from source format to 8 bits-per-channel BGR
* `-c:v rawvideo` set the output video codec to `rawvideo`
* `-map 0:v` map only the decoded & converted source video stream into the output file
* `'<FILENAME>'` the output destination file

## References

* The English [AstroSurface][AstroSurface] web page
* Zwo [Seestar S50][SeestarS50]

[AstroSurface]: <https://astrosurface.com/pageuk.html>

[SeestarS50]: <https://store.seestar.com/products/seestar-s50>
