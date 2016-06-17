#!/usr/bin/env bash

set -e



if [ "$TRAVIS_BRANCH" = 'master' ] && [ "$TRAVIS_PULL_REQUEST" == 'false' ]; then
   cd cd
   openssl aes-256-cbc -K $encrypted_86a80f7728b7_key -iv $encrypted_86a80f7728b7_iv -in codesigning.gpg.enc -out codesigning.gpg -d
fi
