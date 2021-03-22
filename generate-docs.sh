#!/bin/bash

# Create docs folder
mkdir -p javadoc

cd javadoc
javadoc --enable-preview -notimestamp ../src/src/dev/davwheat/*.java ../src/src/dev/davwheat/enums/*.java ../src/src/dev/davwheat/exceptions/*.java

cd ..
