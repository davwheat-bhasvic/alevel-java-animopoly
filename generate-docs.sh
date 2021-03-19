#!/bin/bash

# Create docs folder
mkdir -p javadoc

cd javadoc
javadoc -notimestamp ../src/src/dev/davwheat/*.java ../src/src/dev/davwheat/enums/*.java ../src/src/dev/davwheat/exceptions/*.java

cd ..
