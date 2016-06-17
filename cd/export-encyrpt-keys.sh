#!/usr/bin/env bash

cd cd

FILE=codesigning.gpg

gpg2 --export-secret-key 46B22432 > $FILE

travis encrypt-file $FILE 

rm $FILE