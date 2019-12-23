#!/bin/sh

set -e

# Extract the base64 encoded config data and write this to the KUBECONFIG
echo $INPUT_KUBECONFIG | base64 -d > /tmp/config

/opt/kubectl kustomize deployment/overlays/replace > template.yaml
envsubst '$VERSION $INSTANCE' < template.yaml > deployment.yaml
cat deployment.yaml
/opt/kubectl --kubeconfig /tmp/config apply -f deployment.yaml
