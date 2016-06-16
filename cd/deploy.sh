#!/usr/bin/env bash

set -e

if [ "$TRAVIS_BRANCH" = 'master' ] && [ "$TRAVIS_PULL_REQUEST" == 'false' ]; then
    ./gradlew --daemon uploadArchives "-Psigning.keyId=$GPG_KEY_ID" "-Psigning.password=$GPG_PASS" "-Psigning.secretKeyRingFile=cd/codesigning.gpg" --info
fi
