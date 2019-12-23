#!/bin/sh

set -e

# Extract the base64 encoded config data and write this to the KUBECONFIG
echo "$KUBE_CONFIG_DATA" | base64 -d > /tmp/config
export KUBECONFIG=/tmp/config

# Base64 encode and remove new lines (busybox/alpine does not support base64 -w0)
echo "##[set-output name=kubectl;]$(/opt/kubectl $* | base64 | sed ':a;N;$!ba;s/\n//g')"
