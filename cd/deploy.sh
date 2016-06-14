#!/usr/bin/env bash
if [ "$TRAVIS_BRANCH" = 'master' ] && [ "$TRAVIS_PULL_REQUEST" == 'false' ]; then
    gradle uploadArchives -Psigning.secretKeyRingFile=cd/codesigning.gpg --info
fi
