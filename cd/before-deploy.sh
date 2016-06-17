#!/usr/bin/env bash

set -e

if [ "$TRAVIS_BRANCH" = 'master' ] && [ "$TRAVIS_PULL_REQUEST" == 'false' ]; then
   openssl aes-256-cbc -K $encrypted_994e8d47c447_key -iv $encrypted_994e8d47c447_iv -in cd/codesigning.gpg.enc -out cd/codesigning.gpg -d
   openssl aes-256-cbc -K $encrypted_994e8d47c447_key -iv $encrypted_994e8d47c447_iv -in cd/codesigning.asc.enc -out cd/codesigning.asc -d
   gpg --fast-import cd/codesigning.asc
fi
