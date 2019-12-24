#!/bin/sh -l

echo ########################
echo # Github Docker Action #
echo ########################
echo ImageName: $INPUT_IMAGENAME
echo ImageTags: $INPUT_IMAGETAGS



# Login
echo $INPUT_DOCKERPASSWORD | docker login --username $INPUT_DOCKERUSER --password-stdin

# Build

docker build -t $INPUT_IMAGENAME .

for TAG in $INPUT_IMAGETAGS; do
  echo "Tagging image $TAG"
  docker tag $INPUT_IMAGENAME $INPUT_IMAGENAME:$TAG
  docker push $INPUT_IMAGENAME:$TAG
done

# Push
#docker push $INPUT_IMAGENAME:$INPUT_IMAGETAG
