#!/usr/bin/env bash

NUM_PLUGINS=256;

for ((i=1;  i<=$NUM_PLUGINS; i++))
do
  mvn -DskipTests -Dplugin.id=address-service-$i package
done