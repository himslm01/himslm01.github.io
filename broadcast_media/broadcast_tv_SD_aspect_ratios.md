<!-- himslm01 custom-head.html-->
<script type="text/x-mathjax-config">
  MathJax.Hub.Config({
    tex2jax: {
      skipTags: ['script', 'noscript', 'style', 'textarea', 'pre'],
      inlineMath: [['$','$']]
    }
  });
</script>
<script src="https://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML" type="text/javascript"></script>
<!-- end himslm01 custom-head.html-->

# Broadcast TV - SD Aspect Ratios

Refer to ITU-R BT.601-7 TABLE 4 and ITU-R BT.470-6

In the analogue world of "PAL" TV there are 625 lines transmitted 25 times a second.

A single line is $ 1 \over 25 * 625 $ = 64 microseconds long.

52 micro-seconds is active picture, the rest is sync-pulse and back & front porch.

In the analogue world of "NTSC" there are 525 lines transmitted 30/1.001 times per second.

A single line is $ 1 \over 29.97 * 525 $ = 63.55 microseconds long.

52.65 microseconds is active picture, the rest is sync-pulse and back & front porch.

When analogue video was converting to digital it was decided that a sample frequency of exactly 13.5 MHz would be used for "PAL"/"SECAM" 25 interlaced frames per second and also "NTSC" 30/1.001 frames per second.

To calculate the active picture of a line of video sampled at 13.5MHz:

$ 13500000 * 0.00005200 $ = 702 samples of active picture per line for "PAL"/"SECAM"

$ 13500000 * 0.00005265 $ = 710.8 samples of active per line for "NTSC"

It was decided to use 720 pixels per line, which means that when sampling "NTSC" and "PAL"/"SECAM" at 13.5MHz and the whole active line could fit.  

## SD 576 line formats ("PAL" & "SECAM")

With "PAL", the centre 702 pixels of the 720 encoded pixels per line are the active picture, the rest - 9 at the start and 9 at the end, are part of the back / front porch and must be cropped and discarded.

The active pixels may contain either 4:3 or 16:9 video.

Since $ 576 * 4 / 3 $ = 768 pixels, that means the 702 active pixels need to be scaled up to 768 pixels to make 4:3 video look square, and applying the same scaling ratio to the 720 pixels of complete line means we must scale the 720 pixels up to 788 pixels to make full frame 4:3 look square.

Also, since $ 576 * 16 / 9 $ = 1024 pixels, that means the 702 active pixels need to be scaled up to 1024 pixels to make 16:9 video look square, and applying the same scaling ratio to the 720 pixels of complete line means we must scale the 720 pixels up to 1048 pixels to make full frame 16:9 look square.

Compare the three testcards:

* 720x576 ("PAL") 4:3 SD (Testcard-J - top)
* 720x576 ("PAL") 16:9 SD (Testcard-W - middle)
* 1920x1080 16:9 (Testcard-HD - bottom).

![three testcards with aspect ratio markings](assets/Testcard-J_W_HD-smaller.png)

---
**_NOTE:_**

The picture area of both the 720x576 ("PAL") 4:3 SD (Testcard-J - top) and the 720x576 ("PAL") 16:9 SD (Testcard-W - middle) extends past the 4:3 and 16:9 lines.

This is because the 'active' picture (up to the outer chevron points) is the centre 702 pixels of the whole 720 pixel SD video line. The 'active' area is 4:3 or 16:9, the 'whole picture' area is wider than 4:3 or 16:9.

---
**_IMPORTANT:_**

When converting from SD video into HD video the edges past the 4:3 or 16:9 lines must be cropped and discarded. Only the centre 702 pixels of the 720 pixel SD video line must be used.

When converting from HD video into SD video the edges past the 4:3 or 16:9 lines must be added back. The HD video must fill the centre 702 pixels of the 720 pixel SD video line, the rest is usually filled with black.

An approach when scaling 16:9 SD video is to maintain the (about) 131:72 display aspect ratio of the full line of the SD video, then crop the resulting video down to 16:9 discarding the left and right edges.

### Example scaling "PAL" SD 16:9 video to square pixels

"PAL" SD media starts as 576 lines of anamorphic pixels.

$ 576 * 16 / 9 $ = 1024

Therefore square pixel 16:9 "PAL" SD media 1024 x 576

Scaling 16:9 SD media to square pixels you would scale 720x576 pixels to a display aspect ratio of 131:72.

$ 720 * 131 / 72 $ = 1048

Therefore you first scale 720 to 1048 and preserve the 576 lines.

Now crop to the centre 1024 x 576 pixels from the 1048 x 576.
Start 12 pixels from the left, take 1024 pixels, and ignore the final 12 pixels on the right.
