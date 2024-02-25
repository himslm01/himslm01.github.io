# FFmpeg command line basics

FFmpeg has three classes of command line option:

- global options
- options specific to an input source
- options specific to an output destination

Quoting directly from [FFmpeg's documentation](https://ffmpeg.org//ffmpeg-all.html#Synopsis), the format of a generic FFmpeg command line is:

> *ffmpeg [global_options] {[input_file_options] -i input_url} ... {[output_file_options] output_url} ...*

## Option types

Global options include generic options like getting help, and setting the logging level.

Input options include telling FFmpeg that an input is of a specific format where FFmpeg cannot guess the format from the input source, and setting an input source to repeat.

Output options include setting the encoding parameters for an output destination, and telling FFmpeg to output in a specific format where FFmpeg cannot guess the format from the output filename.

As you might have guessed, some options can be used in multiple contexts. In the examples I gave above I say it is possible to define the format of an input source and output destination. Both use the `-f <format>` option. Therefore it is vital that the option is given in the correct location in the command line.

## Which option is which type

All of the generic command line options listed [here](https://ffmpeg.org//ffmpeg-all.html#Generic-options) are global options.

Every other command line option in [FFmpeg's documentation](https://ffmpeg.org//ffmpeg-all.html#Main-options) includes a parenthasised list of the contexts the command can be used in.

![global, input, output](command_line_basics+global_input_output.png)

It is important to read the FFmpeg documentation for every command line option to understand whether the option is a global option, an input option, or an output option. And it is important to understand the context in which the command line option is being used in order to put the option in the right place in the command line.

## FFmpeg command line generators

If you are using a library to create the FFmpeg command line then it is important that you also understand how the library accepts options in order to create the command line correctly.

For instance, the popular [Jaffree](https://github.com/kokorin/Jaffree) Java FFmpeg command line wrapper has three ways to set command line options.

- global options are added using the `addArgument(...)` and `addArguments(...)` methods on the [FFmpeg class](https://javadoc.io/doc/com.github.kokorin.jaffree/jaffree/latest/com/github/kokorin/jaffree/ffmpeg/FFmpeg.html)

- input options are added using the `addArgument(...)` and `addArguments(...)` methods on an implementation of the [Input interface](https://javadoc.io/doc/com.github.kokorin.jaffree/jaffree/latest/com/github/kokorin/jaffree/ffmpeg/Input.html) via the inheritance through the [BaseInput class](https://javadoc.io/doc/com.github.kokorin.jaffree/jaffree/latest/com/github/kokorin/jaffree/ffmpeg/BaseInput.html) from the [BaseInOut class](https://javadoc.io/doc/com.github.kokorin.jaffree/jaffree/latest/com/github/kokorin/jaffree/ffmpeg/BaseInOut.html)

- output options are added using the `addArgument(...)` and `addArguments(...)` methods on an implementation of the [Output interface](https://javadoc.io/doc/com.github.kokorin.jaffree/jaffree/latest/com/github/kokorin/jaffree/ffmpeg/Output.html) via the inheritance through the [BaseOutput class](https://javadoc.io/doc/com.github.kokorin.jaffree/jaffree/latest/com/github/kokorin/jaffree/ffmpeg/BaseOutput.html) from the [BaseInOut class](https://javadoc.io/doc/com.github.kokorin.jaffree/jaffree/latest/com/github/kokorin/jaffree/ffmpeg/BaseInOut.html)

If you don't use the correct `addArgument(...)` method then the command line option may appear in the wrong place in the command line and FFmpeg may not do what you expected.
