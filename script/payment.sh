#!/bin/bash

export LANG=ja_JP.UTF-8
export PAAS_JAVA_ENV_API='{"merchant":"${merchantapi.base.uri}"}'

BASE_DIR="."

#JAVA_HOME
if [ -z $JAVA_HOME ]; then
  JAVA_HOME="/usr/java/jre7"
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

$JAVA_PATH -classpath $CLASSPATH -Xms128m -Xmx128m com.rakuten.gep.merchant.tools.PaymentExecutor "$@"
