#!/bin/bash

# Create docs folder
mkdir -p javadoc

cd javadoc
javadoc --release 12 --enable-preview -notimestamp ../src/src/dev/davwheat/*.java ../src/src/dev/davwheat/enums/*.java ../src/src/dev/davwheat/exceptions/*.java

cd ..
