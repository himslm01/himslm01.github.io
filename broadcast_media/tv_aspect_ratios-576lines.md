# TV Aspect Ratios - SD 576 line formats ('PAL' & 'SECAM')

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

When converting from SD video into HD video the edges past the 4:3 or 16:9 lines must be cropped away. Only the centre 702 pixels of the 720 pixel SD video line must be used.

When converting from HD video into SD video the edges past the 4:3 or 16:9 lines must be added back. The HD video must fill the centre 702 pixels of the 720 pixel SD video line, the rest is usually filled with black.

---
