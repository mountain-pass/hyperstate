#!/usr/bin/env bash

cd cd

gpg2 --export --armor tom@windyroad.com.au > codesigning.asc
gpg2 --export-secret-keys --armor tom@windyroad.com.au >> codesigning.asc
gpg2 --export tom@windyroad.com.au > codesigning.gpg
gpg2 --export-secret-keys tom@windyroad.com.au >> codesigning.gpg

travis encrypt-file codesigning.asc; travis encrypt-file codesigning.gpg 

rm codesigning.asc codesigning.gpg