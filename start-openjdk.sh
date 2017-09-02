# This starts openjdk container
# $1 args
# $2 name

echo "-->starting java app"
sudo docker run -d -h "$2" --name "$2" -v "$PWD":/usr/src/myapp -w /usr/src/myapp openjdk java App "$1"
echo "-->java app done"