# Broadcast TV - SD Aspect Ratios

Refer to ITU-R BT.601-7 TABLE 4 and ITU-R BT.470-6

In the analogue world of PAL TV there are 625 lines transmitted 25 times a second.

A single line is 1 / (25 * 625) = 64 microseconds long.

52 micro-seconds is active picture, the rest is sync-pulse and back & front porch.

In the analogue world of NTSC there are 525 lines transmitted 30/1.001 times per second.

A single line is 1 / (29.97 * 525) = 63.55 microseconds long.

52.6 microseconds is active picture, the rest is sync-pulse and back & front porch.

When analogue video was converting to digital it was decided that a sample frequency of exactly 13.5 MHz would be used for PAL/SECAM 25 interlaced frames per second and also NTSC 30/1.001 frames per second.

To calculate the active picture of a line of video sampled at 13.5MHz:

13500000 * 0.0000520 = 702 samples of active picture per line for PAL/SECAM

13500000 * 0.0000526 = 710.1 samples of active per line for NTSC

It was decided to use 720 pixels per line, which means that when sampling NTSC and PAL/SECAM at 13.5MHz and the whole line could fit.  

Sticking with PAL, the centre 702 pixels of the 720 encoded pixels per line are the active picture, the rest - 9 at the start and 9 at the end, are part of the back / front porch and must be cropped and discarded.

The active pixels may contain either 4:3 or 16:9 video.

Since 576 x 4 / 3 is 768, that means the 702 active pixels need to be scaled up to 768 pixels to make 4:3 video look square, and applying the same scaling ratio to the 720 pixels of complete line means we must scale the 720 pixels up to 788 pixels to make full frame 4:3 look square.

Also, since 576 x 16 / 9 is 1024, that means the 702 active pixels need to be scaled up to 1024 pixels to make 16:9 video look square, and applying the same scaling ratio to the 720 pixels of complete line means we must scale the 720 pixels up to 1050 pixels to make full frame 16:9 look square.

## SD 576 line formats ('PAL' & 'SECAM')

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

---
