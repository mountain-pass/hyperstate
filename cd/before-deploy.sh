#!/usr/bin/env bash
if [ "$TRAVIS_BRANCH" = 'master' ] && [ "$TRAVIS_PULL_REQUEST" == 'false' ]; then
   openssl aes-256-cbc -K $encrypted_994e8d47c447_key -iv $encrypted_994e8d47c447_iv -in codesigning.gpg.enc -out cd/codesigning.gpg -d
fi
