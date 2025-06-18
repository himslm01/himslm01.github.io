# Broadcast File formats

## IMX30 / IMX40 / IMX50

### Video

- **CODEC**:
MPEG2
- **is long-GOP**:
no - iframe only
- **GOP size**:
1
- **bit-rate**:
30 / 40 / 50 Mbps
- **bit-depth**:
8
- **pixel format**:
yuv422
- **frame-rate**:
25,
or 30/1.001
- **frame width**:
720
- **active frame width**:
702 (25 fps - 9 pixels on left & right are back/front porch),
or ~712 (30/1.001 fps - ~4 pixels on left & right are back/front porch)
- **frame height**:
608 (25 fps),
or 512 (30/1.001 fps)
- **active frame height**:
576 (25 fps - top 32 lines are VBI),
or 486 (30/1.001 fps - top 26 lines are VBI)
- **display aspect ratio**:
4:3 (),
or 16:9
- **is anamorphic**:
yes (always, for both 4:3 and 16:9)
- **streams**:
1

### Audio

- **CODEC**:
PCM
- **bit-rate**:
48,000 samples per second
- **bit-depth**:
16 or 24 bits per sample
- **sample format**:
signed little-ended
- **streams**:
1
- **channels per stream**:
4

### Container

- **file type**:
MXF (D10)
- **file profile**:
OP-1a

## XDCAM

### Video

- **CODEC**:
MPEG2
- **is long-GOP**:
yes
- **GOP size**:
12
- **bit-rate**:
50 Mbps
- **bit-depth**:
8
- **pixel format**:
yuv422
- **frame-rate**:
25 fps,
or 30/1.001 fps
- **frame width**:
1920
- **active frame width**:
1920
- **frame height**:
1080
- **active frame height**:
1080
- **display aspect ratio**:
16:9
- **is anamorphic**:
no
- **streams**:
1

### Audio

- **CODEC**:
- **bit-rate**:
- **bit-depth**:
- **sample format**:
- **streams**:
- **channels per stream**:

### Container

- **file type**:

## AVCi50

### Video

- **CODEC**:
MPEG 4 part 10 (h.264)
- **is long-GOP**:
no - i-frame only
- **GOP size**:
- **bit-rate**:
- **bit-depth**:
- **pixel format**:
- **frame-rate**:
- **frame width**:
- **active frame width**:
- **frame height**:
- **active frame height**:
- **display aspect ratio**:
- **is anamorphic**:
- **streams**:
1

### Audio

- **CODEC**:
- **bit-rate**:
- **bit-depth**:
- **sample format**:
- **streams**:
- **channels per stream**:

### Container

- **file type**:

## AVCi100

### Video

- **CODEC**:
- **is long-GOP**:
no - i-frame only
- **GOP size**:
1
- **bit-rate**:
- **bit-depth**:
- **pixel format**:
- **frame-rate**:
- **frame width**:
- **active frame width**:
- **frame height**:
- **active frame height**:
- **display aspect ratio**:
- **is anamorphic**:
- **streams**:
1

### Audio

- **CODEC**:
- **bit-rate**:
- **bit-depth**:
- **sample format**:
- **streams**:
- **channels per stream**:

### Container

- **file type**:

## DVCProHD aka DV100

### Video

- **CODEC**:
DV
- **is long-GOP**:
no - i-frame only
- **GOP size**:
1
- **bit-rate**:
100 Mbps
- **bit-depth**:
8
- **pixel format**:
yuv422
- **frame-rate**:
25,
or 30/1.001,
or 50,
or 60/1.001
- **frame width**:
1440 (25 fps),
or 1280 (30/1.001 fps),
or 960 (50 fps)
or
- **active frame width**:
same as frame width
- **frame height**:
- **active frame height**:
same as frame height
- **display aspect ratio**:
16:9
- **is anamorphic**:
yes (always)
- **streams**:
1

### Audio

- **CODEC**:
PCM
- **bit-rate**:
48,000 sample per second
- **bit-depth**:
16 or 24 bits per sample
- **sample format**:
- **streams**:
- **channels per stream**:

### Container

- **file type**:
MXF,
or MOV
