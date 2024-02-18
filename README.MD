# How to use this repository

This repository is the source for <https://himslm01.github.io/>.

This repository is a Visual Studio Code devcontainer. Use the "Remote-Containers: Reopen in Container" command in VSCode.

## Rebuild site with Jekyll

This command builds the site into the folder `_site`, which is ignored by git.

```console
bundle exec jekyll build
```

## Serve site with Jekyll

This command builds the site into the folder `_site` and serves the site on port 4000 of a simple webserver.

```console
bundle exec jekyll serve
```
