#!/usr/bin/env bash

cd cd

gpg2 --export --armor tom@windyroad.com.au > codesigning.asc
gpg2 --export-secret-keys --armor tom@windyroad.com.au >> codesigning.asc

travis encrypt-file codesigning.asc; 

rm codesigning.asc 