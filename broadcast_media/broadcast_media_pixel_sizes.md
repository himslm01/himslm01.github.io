# Broadcast Media Pixel Sizes

## Standard Definition

Standard Definition (SD) media may contain media expected to be consumed at either 4:3 or 16:9 aspect ratio.

All Standard Definition video media uses anamorphic pixels - the pixels are not a 1:1 aspect ratio, they are not square pixels.

Exactly what aspect ration the pixels are, or what aspect ratio the full video should be displayed at, might be encoded in the video file. Expect that information to be wrong.

Depending on the age and origin of the media the first half of the first line of active picture and the last half of the last line may contain black video.

|media width|media height|frames per second|active picture display aspect|notes|
|---|---|---|---|---|
|720|608|25|4:3 or 16:9|The top 32 lines contain Vertical Blanking Information and should be cropped and discarded.<br/>[The centre 702x576 of the remaining pixels contain the active picture](broadcast_tv_SD_aspect_ratios).|
|720|576|25|4:3 or 16:9|[The centre 702x576 pixels contain the active picture](broadcast_tv_SD_aspect_ratios).|
|704|576|25|4:3 or 16:9|Assumed that all pixels are active. Often this media will have been HD down-converted to SD for digital emission.|
|544|576|25|4:3 or 16:9|720x576 squeezed by roughly ¾ where the result rounds to the nearest 16. Used in "lower cost" digital TV channels.<br/>[The centre 530x576 pixels contain active picture](broadcast_tv_SD_aspect_ratios).|
|720|512|29.97|4:3 or 16:9|The top 26 lines contain Vertical Blanking Information and should be cropped.<br/>[The centre 712x486 of the remaining pixels contain the active picture](broadcast_tv_SD_aspect_ratios).|
|720|486|29.97|4:3 or 16:9|[The centre 712x486 pixels contain the active picture](broadcast_tv_SD_aspect_ratios).|
|720|480|29.97|4:3 or 16:9 DVD|This gets complicated, because the top 3 lines and bottom 3 lines have been cropped when converting TV to DVD.|

Note: where I have listed decimal frame rates 29.97fps or 59.94fps I really mean the fractional frame rates 30/1.001fps, or 60/1.001fps respectively.

## High Definition

High Definition (HD) media is expected to contain media to be consumed at 16:9 aspect ratio. Sometimes that is wrong, most often when Standard Definition media has been incorrectly scaled to High Definition.

|media width|media height|frames per second|active picture display aspect|notes|
|---|---|---|---|---|
|1920|1080|23.98, 24, 25, 29.97, 30, 47.95, 50, 59.94, 60|16:9|Often referred to as Full High Definition (FHD).|
|1440|1080|23.98, 24, 25, 29.97, 30, 47.95, 50, 59.94, 60|16:9|Anamorphic pixels have an aspect ratio of 3:4.|
|1280|1080|23.98, 24, 25, 29.97, 30, 47.95, 50, 59.94, 60|16:9|Anamorphic pixels have an aspect ratio of 2:3.|
|1280|720|23.98, 24, 25, 29.97, 30, 47.95, 50, 59.94, 60|16:9|May be referred to as High Definition (HD) or High Definition Ready.|
|960|720|23.98, 24, 25, 29.97, 30, 47.95, 50, 59.94, 60|16:9|Anamorphic pixels have an aspect ratio of 3:4.|

Note: where I have listed decimal frame rates 23.98fps, 29.97fps, 47.95fps, or 59.94fps I really mean the fractional frame rates 24/1.001fps 30/1.001fps, 47.95fps, or 60/1.001fps respectively.
