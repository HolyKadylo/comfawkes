# This starts openjdk container
# $1 name
# $2 args

echo "-->starting java app"
sudo docker run -d -h "$1" --name "$1" -v "$PWD":/usr/src/myapp -w /usr/src/myapp openjdk java -jar comfawkes-1.0-SNAPSHOT-jar-with-dependencies.jar "$2 $3 $4 $5 $6 $7 $8 $9"
echo "-->java app done"