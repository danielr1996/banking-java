#!/bin/sh

set -e

# Extract the base64 encoded config data and write this to the KUBECONFIG
echo "$KUBE_CONFIG_DATA" | base64 -d > /tmp/config
export KUBECONFIG=/tmp/config

kubectl kustomize deployments/overlays/replace > template.yaml
envsubst '$VERSION $INSTANCE' < template.yaml > deployment.yaml
/opt/kubectl apply -f deployment.yaml
