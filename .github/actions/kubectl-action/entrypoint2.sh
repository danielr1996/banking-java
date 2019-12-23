#!/bin/sh

set -e
echo "##[set-output name=kubectl;]$(kubectl $*)"
