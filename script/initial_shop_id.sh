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
$JAVA_PATH -classpath $CLASSPATH -Xms128m -Xmx128m com.rakuten.gep.merchant.tools.PaymentExecutor id false false d6134abb-e74f-4d9a-9715-bb98e3775a3c
$JAVA_PATH -classpath $CLASSPATH -Xms128m -Xmx128m com.rakuten.gep.merchant.tools.PaymentExecutor id false false 433634e7-c41c-4f0a-b0e8-862328a7cdd6
$JAVA_PATH -classpath $CLASSPATH -Xms128m -Xmx128m com.rakuten.gep.merchant.tools.PaymentExecutor id false false 54947df8-0e9e-4471-a2f9-9af509fb58co
$JAVA_PATH -classpath $CLASSPATH -Xms128m -Xmx128m com.rakuten.gep.merchant.tools.PaymentExecutor id false false aad374d0-ab34-11e2-9e96-0800200c9a66
$JAVA_PATH -classpath $CLASSPATH -Xms128m -Xmx128m com.rakuten.gep.merchant.tools.PaymentExecutor id false false b56e8d59-da9d-4a11-adc2-0115ca0f0f4e
$JAVA_PATH -classpath $CLASSPATH -Xms128m -Xmx128m com.rakuten.gep.merchant.tools.PaymentExecutor id false false 1bda54a9-374e-494b-807b-311032edf771

# ShippingExecutor
$JAVA_PATH -classpath $CLASSPATH -Xms128m -Xmx128m com.rakuten.gep.merchant.tools.ShippingExecutor id false true 2b522afa-ff47-41b3-9da3-eac437b13df8
$JAVA_PATH -classpath $CLASSPATH -Xms128m -Xmx128m com.rakuten.gep.merchant.tools.ShippingExecutor id false true f2b14a8d-19f1-48e1-a810-241696477e17
$JAVA_PATH -classpath $CLASSPATH -Xms128m -Xmx128m com.rakuten.gep.merchant.tools.ShippingExecutor id false true 54947df8-0e9e-4471-a2f9-9af509fb58co
