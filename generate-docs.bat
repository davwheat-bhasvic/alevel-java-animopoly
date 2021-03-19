rem Create docs folder
IF NOT EXIST "javadoc" md javadoc

cd javadoc
javadoc.exe -notimestamp ../src/src/dev/davwheat/*.java ../src/src/dev/davwheat/enums/*.java ../src/src/dev/davwheat/exceptions/*.java

cd ..
