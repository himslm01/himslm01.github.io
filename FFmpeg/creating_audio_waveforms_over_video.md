# Creating audio waveforms over video with FFmpeg

```console
ffmpeg \
  -i test_audio.mp3 \
  -i test_image.jpg \
  -filter_complex "\
    [0:a]showwaves=s=1920x500:mode=line:colors=White:draw=full,colorkey=0x000000:0.01:0.1,scale=iw:ih,format=yuva420p[v]; \
    [1:v]scale=1920:1080[bg]; \
    [bg][v]overlay=0:620,scale=iw:ih,format=yuv420p[outv] \
  " \
  -map "[outv]" \
  -map 0:a \
  -c:v libx264 -b:v 3500k -crf:v 23 \
  -c:a libfdk_aac -b:a 360k -r:a 48000 \
  output.mp4
```
