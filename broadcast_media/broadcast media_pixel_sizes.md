# Broadcast Media Pixel Sizes

## Standard Definition

Standard Definition (SD) media may contain media expected to be consumed at either 4:3 or 16:9 aspect ratio.

All Standard Definition video media uses anamorphic pixels - the pixels are not a 1:1 aspect ratio, they are not square pixels.

Exactly what aspect ration the pixels are, or what aspect ratio the full video should be displayed at, might be encoded in the video file. Expect that information to be wrong.

Depending on the age and origin of the media the first half of the first line of active picture and the last half of the last line may contain black video.

|width|height|notes|
|---|---|---|
|720|608|4:3 or 16:9 25fps media.<br/>The top 32 lines contain Vertical Blanking Information and should be cropped and discarded.<br/>[The centre 702x576 of the remaining pixels contain the active picture](tv_aspect_ratios-576lines).|
|720|576|4:3 or 16:9 25fps media.<br/>[The centre 702x576 pixels contain the active picture](tv_aspect_ratios-576lines).|
|704|576|4:3 or 16:9 25fps media.<br/>Assumed that all pixels are active. Often this media will have been HD down-converted to SD for digital emission.|
|544|576|4:3 or 16:9 25fps media.<br/>720x576 squeezed by roughly ¾ where the result rounds to the nearest 16. Used in "lower cost" digital TV channels.<br/>The centre 530x576 pixels contain active picture.|
|720|512|4:3 or 16:9 29.97fps media.<br/>The top 26 lines contain Vertical Blanking Information and should be cropped.<br/>The centre 712x486 of the remaining pixels contain the active picture.|
|720|486|4:3 or 16:9 29.97fps media.<br/>The centre 712x486 pixels contain the active picture.|
|720|480|4:3 or 16:9 29.97fps DVD media.<br/>This gets complicated, because the top 3 lines and bottom 3 lines have been cropped when converting TV to DVD.|

## High Definition

High Definition (HD) media is expected to contain media to be consumed at 16:9 aspect ratio. Sometimes that is wrong, most often when Standard Definition media has been incorrectly scaled to High Definition.

|width|height|notes|
|---|---|---|
|1920|1080|16:9 at usually 23.68fps, 24fps, 25fps, 29.97fps, 30fps, 47.95fps, 50fps, 59.94fps, or 60fps<br/>Often referred to as Full High Definition (FHD).|
|1440|1080|16:9 at usually 23.68fps, 24fps, 25fps, 29.97fps, 30fps, 47.95fps, 50fps, 59.94fps, or 60fps<br/>Anomorphic pixels have an aspect ratio of 3:4.|
|1280|1080|16:9 at usually 23.68fps, 24fps, 25fps, 29.97fps, 30fps, 47.95fps, 50fps, 59.94fps, or 60fps<br/>Anomorphic pixels have an aspect ratio of 2:3.|
|1280|720|16:9 at usually 23.68fps, 24fps, 25fps, 29.97fps, 30fps, 47.95fps, 50fps, 59.94fps, or 60fps<br/>May be referred to as High Definition (HD) or High Definition Ready.|
|960|720|16:9 at usually 23.68fps, 24fps, 25fps, 29.97fps, 30fps, 47.95fps, 50fps, 59.94fps, or 60fps<br/>Anomorphic pixels have an aspect ratio of 3:4.|


Note: where I have listed decimal frame rates 23.98fps, 29.97fps, 47.95fps, or 59.94fps I really mean the fractional frame rates 24/1.001fps 30/1.001fps, 48/1.001fps, or 60/1.001fps respectively.
