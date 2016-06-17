#!/usr/bin/env bash

set -e

if [ "$TRAVIS_BRANCH" = 'master' ] && [ "$TRAVIS_PULL_REQUEST" == 'false' ]; then
   cd cd
   openssl aes-256-cbc -K $encrypted_86a80f7728b7_key -iv $encrypted_86a80f7728b7_iv -in codesigning.asc.enc -out codesigning.asc -d
   gpg --fast-import codesigning.asc
   gpg --list-keys
   gpg --list-secret-keys
   ls -l $HOME/.gnupg
fi
