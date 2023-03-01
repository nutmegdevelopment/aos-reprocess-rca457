#!/usr/bin/env bash

kafka-proxy server --log-format=json --bootstrap-server-mapping=b-1.agrob-the-orc.g5cim7.c3.kafka.eu-west-1.amazonaws.com:9094,127.0.0.1:32400 --bootstrap-server-mapping=b-2.agrob-the-orc.g5cim7.c3.kafka.eu-west-1.amazonaws.com:9094,127.0.0.1:32401 --bootstrap-server-mapping=b-3.agrob-the-orc.g5cim7.c3.kafka.eu-west-1.amazonaws.com:9094,127.0.0.1:32402 --bootstrap-server-mapping=b-4.agrob-the-orc.g5cim7.c3.kafka.eu-west-1.amazonaws.com:9094,127.0.0.1:32403 --bootstrap-server-mapping=b-5.agrob-the-orc.g5cim7.c3.kafka.eu-west-1.amazonaws.com:9094,127.0.0.1:32404 --bootstrap-server-mapping=b-6.agrob-the-orc.g5cim7.c3.kafka.eu-west-1.amazonaws.com:9094,127.0.0.1:32405 --tls-enable --tls-client-cert-file=/Users/iago.depaulaquirino/workspace/workspace-nutmeg/aos-reprocess-rca457/user.crt --tls-client-key-file=/Users/iago.depaulaquirino/workspace/workspace-nutmeg/aos-reprocess-rca457/user.key