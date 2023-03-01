#!/usr/bin/env bash

echo "<user_cert>" | base64 -d > user.crt
echo "<user_key>" | base64 -d > user.key