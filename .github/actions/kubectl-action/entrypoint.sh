#!/bin/sh

set -e

# Extract the base64 encoded config data and write this to the KUBECONFIG
echo "$KUBE_CONFIG_DATA" | base64 -d > /tmp/config
export KUBECONFIG=/tmp/config

KUBECTL_OUTPUT = $(/bin/sh -c "/opt/kubectl $*")
echo KUBECTL_OUTPUT
echo "::set-output name=kubectl::$KUBECTL_OUTPUT"
