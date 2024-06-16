# Programming with BASH

## Pipe stderr to sub-process

```bash
programme1 2> >(programm2)
```

for example:

```bash
ffmpeg ... 2> >(tee -a /tmp/logs.txt)
```

## Run action on every file in a folder

Don't worry about spaces in filenames.

```bash
find . -maxdepth 1 -type f -print0 | while read -d $'\0' file; do
    printf "$file"
done
