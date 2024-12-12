#!/bin/bash
# Installs the admin module
# Make sure you have updated ui_values.yaml
## Usage: ./install.sh [kubeconfig]

if [ $# -ge 1 ] ; then
  export KUBECONFIG=$1
fi

NS=admin
KNS=kernel
CHART_VERSION=1.3.0-beta.1-develop
COPY_UTIL=../copy_cm_func.sh

echo Create $NS namespace
kubectl create ns $NS

function installing_admin() {
  echo Istio label
  kubectl label ns $NS istio-injection=enabled --overwrite
  helm repo update

  echo Copy configmaps for kernel
  $COPY_UTIL configmap global default $KNS
  $COPY_UTIL configmap artifactory-share artifactory $KNS
  $COPY_UTIL configmap config-server-share config-server $KNS

  echo Copy configmaps for admin
  $COPY_UTIL configmap global default $NS
  $COPY_UTIL configmap artifactory-share artifactory $NS
  $COPY_UTIL configmap config-server-share config-server $NS

  ADMIN_HOST=$(kubectl get cm global -o jsonpath={.data.mosip-admin-host})
  echo Installing masterdata and allowing Admin UI to access masterdata services.
  helm -n $KNS install masterdata mosip/masterdata  --set istio.corsPolicy.allowOrigins\[0\].exact=https://$ADMIN_HOST  --version $CHART_VERSION

  echo Installing syncdata
  helm -n $KNS install syncdata mosip/syncdata --version $CHART_VERSION

  API_HOST=$(kubectl get cm global -o jsonpath={.data.mosip-api-internal-host})
  ADMIN_HOST=$(kubectl get cm global -o jsonpath={.data.mosip-admin-host})

  echo Installing Admin-Proxy into Masterdata and Keymanager.
  kubectl -n $NS apply -f admin-proxy.yaml

  echo Installing admin hotlist service.
  helm -n $NS install admin-hotlist mosip/admin-hotlist --version $CHART_VERSION

  echo Installing admin service. Will wait till service gets installed.
  helm -n $NS install admin-service mosip/admin-service --set istio.corsPolicy.allowOrigins\[0\].prefix=https://$ADMIN_HOST --wait --version $CHART_VERSION

  kubectl -n $NS  get deploy -o name |  xargs -n1 -t  kubectl -n $NS rollout status

  echo Installed admin services

  echo "Admin portal URL: https://$ADMIN_HOST/admin-ui/"
  return 0
}

# set commands for error handling.
set -e
set -o errexit   ## set -e : exit the script if any statement returns a non-true return value
set -o nounset   ## set -u : exit the script if you try to use an uninitialised variable
set -o errtrace  # trace ERR through 'time command' and other functions
set -o pipefail  # trace ERR through pipes
installing_admin   # calling function
