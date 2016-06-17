#!/usr/bin/env bash

cd cd

FILE=codesigning.asc

gpg2 --export --armor 46B22432 > $FILE
gpg2 --export-secret-key --armor 46B22432 >> $FILE

travis encrypt-file $FILE 

rm $FILE