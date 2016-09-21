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

$JAVA_PATH -classpath $CLASSPATH -Xms128m -Xmx128m com.rakuten.gep.merchant.tools.ShopOneExecutor "$@"

# PaymentExecutor
$JAVA_PATH -classpath $CLASSPATH -Xms128m -Xmx128m com.rakuten.gep.merchant.tools.PaymentExecutor my false false 54947df8-0e9e-4471-a2f9-9af509fb58cc
$JAVA_PATH -classpath $CLASSPATH -Xms128m -Xmx128m com.rakuten.gep.merchant.tools.PaymentExecutor my false false 54947df8-0e9e-4471-a2f9-9af509fb58nb
$JAVA_PATH -classpath $CLASSPATH -Xms128m -Xmx128m com.rakuten.gep.merchant.tools.PaymentExecutor my false false 54947df8-0e9e-4471-a2f9-9af509fb58bw
$JAVA_PATH -classpath $CLASSPATH -Xms128m -Xmx128m com.rakuten.gep.merchant.tools.PaymentExecutor my false true cc24071f-eef2-6d33-90df-f39c14131b2d
$JAVA_PATH -classpath $CLASSPATH -Xms128m -Xmx128m com.rakuten.gep.merchant.tools.PaymentExecutor my false true 1901115c-d76d-ba76-a212-be8f89beb6a2

# ShippingExecutor
$JAVA_PATH -classpath $CLASSPATH -Xms128m -Xmx128m com.rakuten.gep.merchant.tools.ShippingExecutor my false true 1901115c-d76d-ba76-a212-be8f89beb6a2
$JAVA_PATH -classpath $CLASSPATH -Xms128m -Xmx128m com.rakuten.gep.merchant.tools.ShippingExecutor my false true d1caedd6-ca3e-dad1-4fea-3f4e6e2e60c3
