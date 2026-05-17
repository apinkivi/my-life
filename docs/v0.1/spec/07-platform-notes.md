# Platform-specific Notes

We start using monorepo where platform specific apps are in subfolders under [apps](/apps/). When client apps grow big, we more to hybrid repo model where the spec and shared code stays in this repo and client app is splitted to own dependent repo.

## Java

Use Java 21 LTS and Temurin implementation where possible.