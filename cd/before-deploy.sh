#!/usr/bin/env bash

set -e

if [ "$TRAVIS_BRANCH" = 'master' ] && [ "$TRAVIS_PULL_REQUEST" == 'false' ]; then
   gpg --list-keys
   openssl aes-256-cbc -K $encrypted_86a80f7728b7_key -iv $encrypted_86a80f7728b7_iv -in cd/codesigning.asc.enc -out cd/codesigning.asc -d
   gpg --fast-import cd/codesigning.asc
   gpg --list-keys
fi
