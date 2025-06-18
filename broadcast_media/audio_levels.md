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

# Audio levels

## Glossary

| Acronym | Meaning | Description |
| --- | --- | --- |
|dB | decibel (tenth of a Bell) | A unit used for relative audio level measurements.<br />The dB scale is logarithmic. |
| dBFS | decibel Full Scale | A unit for measurements of signal level relative to full scale in a digital domain.<br /><br />Full Scale, the maximum level that can be represented by the digital encoding, called 0dBFS.<br /><br />Line-up level, the level of a sign-wave tone put at the start of programmes or items, currently in the BBC is 18dB lower than the Full Scale level, so is called -18dBFS.<br />Previously, stereo programmes in the BBC had the line-up level of -21dBFS, but that is deprecated. |
| dBA | decibel A-weighting | A unit for measurements of audio at 1 kHz as perceived by the human ear relative to the auditory threshold (silence).<br /><br />A very calm room 20 – 30 dB<br />Jet engine at 100 m 110 – 140 dB |
| dBu |   | A unit for measurements of analogue signal level relative to 0.775V RMS.<br /><br />0dBu - often referred to as 'zero level' - and often represents the analogue audio level of a sign-wave tone at 0.775V RMS.<br />0dBu usually maps to -18dBFS (EBU R 68) or -20dBFS (SMPTE RP 0155). |
| LU | Loudness Unit | A unit for subjective loudness differences.<br /><br />Applying a gain of 1dB to a signal will increase its loudness level by 1LU. In that sense, 1 LU is equivalent to 1 dB. |
|LUFS | Loudness Unit Full Scale | A unit for subjective loudness levels relative to full scale, measured using ITU-R BS.1770.<br /><br />EBU R 128 recommends that all programmes be normalised to an average foreground loudness level of -23 LUFS. |
|VU | Volume Unit | The scale on many "tape recorders" and often used in USA audio mixing studios.<br /><https://en.wikipedia.org/wiki/VU_meter> |
|PPM | Peak Program Meter | The scale on most broadcast audio mixing studios in the UK.<br />BBC lineup tone is 0dBu. On mono BBC PPMs lineup tone should align with PPM 4.<br /><https://en.wikipedia.org/wiki/Peak_programme_meter> |

## Audio level reduction

* -6dB = $ 1 \over 2 $ original level
* -12dB` = $ 1 \over 4 $ original level
* -18dB` = $ 1 \over 8 $ original level
* -24dB = $ 1 \over 16 $ original level
* -30dB = $ 1 \over 32 $ original level
* -36dB = $ 1 \over 64 $ original level
* -42dB = $ 1 \over 128 $ original level
* -48dB = $ 1 \over 256 $ original level
* -54dB = $ 1 \over 512 $ original level
* -60dB = $ 1 \over 1,024 $ original level
* -66dB = $ 1 \over 2,048 $ original level
* -72dB = $ 1 \over 4,096 $ original level
* -78dB = $ 1 \over 8,192 $ original level
* -84dB = $ 1 \over 16,348 $ original level
* -90dB = $ 1 \over 32,768 $ original level
* -96dB = $ 1 \over 65,536 $ original level
