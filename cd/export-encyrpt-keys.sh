#!/usr/bin/env bash

cd cd

FILE=codesigning.asc

gpg2 --export E3AF4ECA > $FILE
gpg2 --export-secret-key E3AF4ECA > $FILE

travis encrypt-file $FILE 

rm $FILE