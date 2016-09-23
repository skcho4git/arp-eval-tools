#!/bin/bash

export LANG=ja_JP.UTF-8

BASE_DIR=$(dirname $0)
HEAP_SIZE=$1

shift

#JAVA_HOME
if [ -z $JAVA_HOME ]; then
  JAVA_HOME="/usr/java/jre8"
fi
#JAVA_PATH
JAVA_PATH=$JAVA_HOME/bin/java

#CLASS_PATH
CLASSPATH="."
for jar in $BASE_DIR/lib/*.jar
do
  CLASSPATH=$CLASSPATH:$jar
done

export BASE_DIR
export JAVA_PATH
export CLASSPATH

$JAVA_PATH -classpath $CLASSPATH -Xms${HEAP_SIZE} -Xmx${HEAP_SIZE} com.rakuten.arp.eval.entrypoint.EvalExecutor "$@"