#!/bin/sh

set -e

# Extract the base64 encoded config data and write this to the KUBECONFIG
echo "$KUBE_CONFIG_DATA" | base64 -d > /tmp/config
export KUBECONFIG=/tmp/config

/opt/kubectl kustomize deployment/overlays/replace > template.yaml
envsubst '$VERSION $INSTANCE' < template.yaml > deployment.yaml
cat deployment.yaml
/opt/kubectl config view
/opt/kubectl apply -f deployment.yaml
