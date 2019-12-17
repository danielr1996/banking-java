#!/bin/sh -l

echo ########################
echo # Github Docker Action #
echo ########################
echo ImageName: $INPUT_IMAGENAME
echo ImageTag: $INPUT_IMAGETAG
#IMAGETAG_SLUG=$(echo $INPUT_IMAGETAG | tr / -)
#echo ImageTagSlug: $IMAGETAG_SLUG
echo DockerUser: $INPUT_DOCKERUSER
echo DockerPasword: $INPUT_DOCKERPASSWORD

# Login
echo $INPUT_DOCKERPASSWORD | docker login --username $INPUT_DOCKERUSER --password-stdin

# Build
docker build -t $INPUT_IMAGENAME:$INPUT_IMAGETAG .

# Push
docker push $INPUT_IMAGENAME:$INPUT_IMAGETAG
